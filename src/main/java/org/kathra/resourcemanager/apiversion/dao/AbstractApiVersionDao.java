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

package org.kathra.resourcemanager.apiversion.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.ApiVersion;
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

import org.kathra.resourcemanager.component.dao.ComponentDb;
import org.kathra.resourcemanager.component.dao.ComponentApiVersionEdge;
import org.kathra.resourcemanager.component.dao.ComponentApiVersionEdgeRepository;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionDb;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionApiVersionEdge;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionApiVersionEdgeRepository;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionDb;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionApiVersionEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionApiVersionEdgeRepository;


/**
 * Abtrasct dao service to manage ApiVersion using ApiVersionDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:07.038Z
 * @author jboubechtoula
 */
public abstract class AbstractApiVersionDao extends AbstractResourceDao<ApiVersion, ApiVersionDb, String> {

	@Autowired
	ComponentApiVersionEdgeRepository componentApiVersionEdgeRepository;
	@Autowired
	LibraryApiVersionApiVersionEdgeRepository libraryApiVersionApiVersionEdgeRepository;
	@Autowired
	ImplementationVersionApiVersionEdgeRepository implementationVersionApiVersionEdgeRepository;


    public AbstractApiVersionDao(@Autowired ApiVersionRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new ApiVersionDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.componentApiVersionEdgeRepository.count();
		this.libraryApiVersionApiVersionEdgeRepository.count();
		this.implementationVersionApiVersionEdgeRepository.count();

    }

    @Override
    public void create(ApiVersion object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(ApiVersion object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(ApiVersion object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(ApiVersion object) throws Exception {
        ApiVersionDb resourceDb = this.convertResourceToResourceDb(object);
        EdgeUtils.of(ComponentApiVersionEdge.class).updateReference(resourceDb, "component", componentApiVersionEdgeRepository);
        if (object.getLibrariesApiVersions() != null) {
            List librariesApiVersionsItemsToUpdate = object.getLibrariesApiVersions().parallelStream().filter(Objects::nonNull).map(i -> new LibraryApiVersionDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(LibraryApiVersionApiVersionEdge.class).updateList(resourceDb, librariesApiVersionsItemsToUpdate, libraryApiVersionApiVersionEdgeRepository);
        }
        if (object.getImplementationsVersions() != null) {
            List implementationsVersionsItemsToUpdate = object.getImplementationsVersions().parallelStream().filter(Objects::nonNull).map(i -> new ImplementationVersionDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(ImplementationVersionApiVersionEdge.class).updateList(resourceDb, implementationsVersionsItemsToUpdate, implementationVersionApiVersionEdgeRepository);
        }

    }


    ApiVersionMapper mapper = Selma.mapper(ApiVersionMapper.class);

    public ApiVersionDb convertResourceToResourceDb(ApiVersion object) {
        return mapper.asApiVersionDb(object);
    }

    public ApiVersion convertResourceDbToResource(ApiVersionDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asApiVersion((ApiVersionDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<ApiVersion> convertResourceDbToResource(Stream<ApiVersionDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (ApiVersionDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asApiVersion(i));
    }
}
