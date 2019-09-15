/* 
 * Copyright 2019 The Kathra Authors.
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
 *
 *    IRT SystemX (https://www.kathra.org/)    
 *
 */

package org.kathra.resourcemanager.libraryapiversion.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.LibraryApiVersion;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.kathra.resourcemanager.resource.utils.LeanResourceDbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.kathra.resourcemanager.resource.utils.EdgeUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import fr.xebia.extras.selma.Selma;
import java.util.stream.Stream;

import org.kathra.resourcemanager.library.dao.LibraryDb;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionLibraryEdge;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionLibraryEdgeRepository;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionDb;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionApiVersionEdge;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionApiVersionEdgeRepository;


/**
 * Abtrasct dao service to manage LibraryApiVersion using LibraryApiVersionDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.1.1 at 2019-04-12T13:51:00.468Z
 * @author julien.boubechtoula
 */
public abstract class AbstractLibraryApiVersionDao extends AbstractResourceDao<LibraryApiVersion, LibraryApiVersionDb, String> {

	@Autowired
	LibraryApiVersionLibraryEdgeRepository libraryApiVersionLibraryEdgeRepository;
	@Autowired
	LibraryApiVersionApiVersionEdgeRepository libraryApiVersionApiVersionEdgeRepository;


    public AbstractLibraryApiVersionDao(@Autowired LibraryApiVersionRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new LibraryApiVersionDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.libraryApiVersionLibraryEdgeRepository.count();
		this.libraryApiVersionApiVersionEdgeRepository.count();

    }

    @Override
    public void create(LibraryApiVersion object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(LibraryApiVersion object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    private void updateReferences(LibraryApiVersion object) throws Exception {
        EdgeUtils.of(LibraryApiVersionLibraryEdge.class).updateReference(this.convertResourceToResourceDb(object), "library", libraryApiVersionLibraryEdgeRepository);
        EdgeUtils.of(LibraryApiVersionApiVersionEdge.class).updateReference(this.convertResourceToResourceDb(object), "apiVersion", libraryApiVersionApiVersionEdgeRepository);

    }


    LibraryApiVersionMapper mapper = Selma.mapper(LibraryApiVersionMapper.class);

    public LibraryApiVersionDb convertResourceToResourceDb(LibraryApiVersion object) {
        return mapper.asLibraryApiVersionDb(object);
    }

    public LibraryApiVersion convertResourceDbToResource(LibraryApiVersionDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asLibraryApiVersion((LibraryApiVersionDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<LibraryApiVersion> convertResourceDbToResource(Stream<LibraryApiVersionDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (LibraryApiVersionDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asLibraryApiVersion(i));
    }
}