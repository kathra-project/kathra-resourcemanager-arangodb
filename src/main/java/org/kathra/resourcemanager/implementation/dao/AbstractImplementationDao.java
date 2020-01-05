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

package org.kathra.resourcemanager.implementation.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.Implementation;
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
import org.kathra.resourcemanager.implementation.dao.ImplementationComponentEdge;
import org.kathra.resourcemanager.implementation.dao.ImplementationComponentEdgeRepository;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionDb;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdgeRepository;
import org.kathra.resourcemanager.binaryrepository.dao.BinaryRepositoryDb;
import org.kathra.resourcemanager.implementation.dao.ImplementationBinaryRepositoryEdge;
import org.kathra.resourcemanager.implementation.dao.ImplementationBinaryRepositoryEdgeRepository;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryImplementationEdge;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryImplementationEdgeRepository;
import org.kathra.resourcemanager.pipeline.dao.PipelineDb;
import org.kathra.resourcemanager.pipeline.dao.PipelineImplementationEdge;
import org.kathra.resourcemanager.pipeline.dao.PipelineImplementationEdgeRepository;
import org.kathra.resourcemanager.catalogentry.dao.CatalogEntryDb;
import org.kathra.resourcemanager.implementation.dao.ImplementationCatalogEntryEdge;
import org.kathra.resourcemanager.implementation.dao.ImplementationCatalogEntryEdgeRepository;


/**
 * Abtrasct dao service to manage Implementation using ImplementationDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2019-12-28T18:27:23.234Z
 * @author Julien Boubechtoula
 */
public abstract class AbstractImplementationDao extends AbstractResourceDao<Implementation, ImplementationDb, String> {

	@Autowired
	ImplementationComponentEdgeRepository implementationComponentEdgeRepository;
	@Autowired
	ImplementationVersionImplementationEdgeRepository implementationVersionImplementationEdgeRepository;
	@Autowired
	ImplementationBinaryRepositoryEdgeRepository implementationBinaryRepositoryEdgeRepository;
	@Autowired
	SourceRepositoryImplementationEdgeRepository sourceRepositoryImplementationEdgeRepository;
	@Autowired
	PipelineImplementationEdgeRepository pipelineImplementationEdgeRepository;
	@Autowired
	ImplementationCatalogEntryEdgeRepository implementationCatalogEntryEdgeRepository;


    public AbstractImplementationDao(@Autowired ImplementationRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new ImplementationDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.implementationComponentEdgeRepository.count();
		this.implementationVersionImplementationEdgeRepository.count();
		this.implementationBinaryRepositoryEdgeRepository.count();
		this.sourceRepositoryImplementationEdgeRepository.count();
		this.pipelineImplementationEdgeRepository.count();
		this.implementationCatalogEntryEdgeRepository.count();

    }

    @Override
    public void create(Implementation object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(Implementation object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(Implementation object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(Implementation object) throws Exception {
        ImplementationDb resourceDb = this.convertResourceToResourceDb(object);
        EdgeUtils.of(ImplementationComponentEdge.class).updateReference(resourceDb, "component", implementationComponentEdgeRepository);
        if (object.getVersions() != null) {
            List versionsItemsToUpdate = object.getVersions().parallelStream().filter(Objects::nonNull).map(i -> new ImplementationVersionDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(ImplementationVersionImplementationEdge.class).updateList(resourceDb, versionsItemsToUpdate, implementationVersionImplementationEdgeRepository);
        }
        EdgeUtils.of(ImplementationBinaryRepositoryEdge.class).updateReference(resourceDb, "binaryRepository", implementationBinaryRepositoryEdgeRepository);
        EdgeUtils.of(SourceRepositoryImplementationEdge.class).updateReference(resourceDb, "sourceRepository", sourceRepositoryImplementationEdgeRepository);
        EdgeUtils.of(PipelineImplementationEdge.class).updateReference(resourceDb, "pipeline", pipelineImplementationEdgeRepository);
        if (object.getCatalogEntries() != null) {
            List catalogEntriesItemsToUpdate = object.getCatalogEntries().parallelStream().filter(Objects::nonNull).map(i -> new CatalogEntryDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(ImplementationCatalogEntryEdge.class).updateList(resourceDb, catalogEntriesItemsToUpdate, implementationCatalogEntryEdgeRepository);
        }

    }


    ImplementationMapper mapper = Selma.mapper(ImplementationMapper.class);

    public ImplementationDb convertResourceToResourceDb(Implementation object) {
        return mapper.asImplementationDb(object);
    }

    public Implementation convertResourceDbToResource(ImplementationDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asImplementation((ImplementationDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<Implementation> convertResourceDbToResource(Stream<ImplementationDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (ImplementationDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asImplementation(i));
    }
}
