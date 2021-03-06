/*
 * Copyright (c) 2020. The Kathra Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *    IRT SystemX (https://www.kathra.org/)
 *
 */

package org.kathra.resourcemanager.catalogentry.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.CatalogEntry;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.kathra.resourcemanager.resource.utils.LeanResourceDbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.kathra.resourcemanager.resource.utils.EdgeUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import fr.xebia.extras.selma.Selma;
import java.util.stream.Stream;

import org.kathra.resourcemanager.catalogentrypackage.dao.CatalogEntryPackageDb;
import org.kathra.resourcemanager.catalogentrypackage.dao.CatalogEntryPackageCatalogEntryEdge;
import org.kathra.resourcemanager.catalogentrypackage.dao.CatalogEntryPackageCatalogEntryEdgeRepository;


/**
 * Abtrasct dao service to manage CatalogEntry using CatalogEntryDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:08.724Z
 * @author jboubechtoula
 */
public abstract class AbstractCatalogEntryDao extends AbstractResourceDao<CatalogEntry, CatalogEntryDb, String> {

	@Autowired
	CatalogEntryPackageCatalogEntryEdgeRepository catalogEntryPackageCatalogEntryEdgeRepository;


    public AbstractCatalogEntryDao(@Autowired CatalogEntryRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new CatalogEntryDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.catalogEntryPackageCatalogEntryEdgeRepository.count();

    }

    @Override
    public void create(CatalogEntry object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(CatalogEntry object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(CatalogEntry object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(CatalogEntry object) throws Exception {
        CatalogEntryDb resourceDb = this.convertResourceToResourceDb(object);
        if (object.getPackages() != null) {
            List packagesItemsToUpdate = object.getPackages().parallelStream().filter(Objects::nonNull).map(i -> new CatalogEntryPackageDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(CatalogEntryPackageCatalogEntryEdge.class).updateList(resourceDb, packagesItemsToUpdate, catalogEntryPackageCatalogEntryEdgeRepository);
        }

    }


    CatalogEntryMapper mapper = Selma.mapper(CatalogEntryMapper.class);

    public CatalogEntryDb convertResourceToResourceDb(CatalogEntry object) {
        return mapper.asCatalogEntryDb(object);
    }

    public CatalogEntry convertResourceDbToResource(CatalogEntryDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asCatalogEntry((CatalogEntryDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<CatalogEntry> convertResourceDbToResource(Stream<CatalogEntryDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (CatalogEntryDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asCatalogEntry(i));
    }
}
