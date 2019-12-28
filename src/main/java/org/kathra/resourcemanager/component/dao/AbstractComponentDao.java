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

package org.kathra.resourcemanager.component.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.Component;
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

import org.kathra.resourcemanager.apiversion.dao.ApiVersionDb;
import org.kathra.resourcemanager.component.dao.ComponentApiVersionEdge;
import org.kathra.resourcemanager.component.dao.ComponentApiVersionEdgeRepository;
import org.kathra.resourcemanager.library.dao.LibraryDb;
import org.kathra.resourcemanager.library.dao.LibraryComponentEdge;
import org.kathra.resourcemanager.library.dao.LibraryComponentEdgeRepository;
import org.kathra.resourcemanager.implementation.dao.ImplementationDb;
import org.kathra.resourcemanager.implementation.dao.ImplementationComponentEdge;
import org.kathra.resourcemanager.implementation.dao.ImplementationComponentEdgeRepository;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryComponentEdge;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryComponentEdgeRepository;


/**
 * Abtrasct dao service to manage Component using ComponentDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2019-12-28T18:27:17.849Z
 * @author Julien Boubechtoula
 */
public abstract class AbstractComponentDao extends AbstractResourceDao<Component, ComponentDb, String> {

	@Autowired
	ComponentApiVersionEdgeRepository componentApiVersionEdgeRepository;
	@Autowired
	LibraryComponentEdgeRepository libraryComponentEdgeRepository;
	@Autowired
	ImplementationComponentEdgeRepository implementationComponentEdgeRepository;
	@Autowired
	SourceRepositoryComponentEdgeRepository sourceRepositoryComponentEdgeRepository;


    public AbstractComponentDao(@Autowired ComponentRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new ComponentDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.componentApiVersionEdgeRepository.count();
		this.libraryComponentEdgeRepository.count();
		this.implementationComponentEdgeRepository.count();
		this.sourceRepositoryComponentEdgeRepository.count();

    }

    @Override
    public void create(Component object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(Component object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(Component object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(Component object) throws Exception {
        ComponentDb resourceDb = this.convertResourceToResourceDb(object);
        if (object.getVersions() != null) {
            List versionsItemsToUpdate = object.getVersions().stream().map(i -> new ApiVersionDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(ComponentApiVersionEdge.class).updateList(resourceDb, versionsItemsToUpdate, componentApiVersionEdgeRepository);
        }
        if (object.getLibraries() != null) {
            List librariesItemsToUpdate = object.getLibraries().stream().map(i -> new LibraryDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(LibraryComponentEdge.class).updateList(resourceDb, librariesItemsToUpdate, libraryComponentEdgeRepository);
        }
        if (object.getImplementations() != null) {
            List implementationsItemsToUpdate = object.getImplementations().stream().map(i -> new ImplementationDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(ImplementationComponentEdge.class).updateList(resourceDb, implementationsItemsToUpdate, implementationComponentEdgeRepository);
        }
        EdgeUtils.of(SourceRepositoryComponentEdge.class).updateReference(resourceDb, "apiRepository", sourceRepositoryComponentEdgeRepository);

    }


    ComponentMapper mapper = Selma.mapper(ComponentMapper.class);

    public ComponentDb convertResourceToResourceDb(Component object) {
        return mapper.asComponentDb(object);
    }

    public Component convertResourceDbToResource(ComponentDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asComponent((ComponentDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<Component> convertResourceDbToResource(Stream<ComponentDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (ComponentDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asComponent(i));
    }
}
