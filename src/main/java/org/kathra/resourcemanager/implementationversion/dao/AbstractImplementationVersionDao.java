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

package org.kathra.resourcemanager.implementationversion.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.ImplementationVersion;
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

import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryImplementationVersionEdge;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryImplementationVersionEdgeRepository;
import org.kathra.resourcemanager.implementation.dao.ImplementationDb;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdgeRepository;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionDb;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionApiVersionEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionApiVersionEdgeRepository;


/**
 * Abtrasct dao service to manage ImplementationVersion using ImplementationVersionDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T12:41:55.385Z
 * @author jboubechtoula
 */
public abstract class AbstractImplementationVersionDao extends AbstractResourceDao<ImplementationVersion, ImplementationVersionDb, String> {

	@Autowired
	SourceRepositoryImplementationVersionEdgeRepository sourceRepositoryImplementationVersionEdgeRepository;
	@Autowired
	ImplementationVersionImplementationEdgeRepository implementationVersionImplementationEdgeRepository;
	@Autowired
	ImplementationVersionApiVersionEdgeRepository implementationVersionApiVersionEdgeRepository;


    public AbstractImplementationVersionDao(@Autowired ImplementationVersionRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new ImplementationVersionDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.sourceRepositoryImplementationVersionEdgeRepository.count();
		this.implementationVersionImplementationEdgeRepository.count();
		this.implementationVersionApiVersionEdgeRepository.count();

    }

    @Override
    public void create(ImplementationVersion object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(ImplementationVersion object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(ImplementationVersion object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(ImplementationVersion object) throws Exception {
        ImplementationVersionDb resourceDb = this.convertResourceToResourceDb(object);
        EdgeUtils.of(SourceRepositoryImplementationVersionEdge.class).updateReference(resourceDb, "sourceRepo", sourceRepositoryImplementationVersionEdgeRepository);
        EdgeUtils.of(ImplementationVersionImplementationEdge.class).updateReference(resourceDb, "implementation", implementationVersionImplementationEdgeRepository);
        EdgeUtils.of(ImplementationVersionApiVersionEdge.class).updateReference(resourceDb, "apiVersion", implementationVersionApiVersionEdgeRepository);

    }


    ImplementationVersionMapper mapper = Selma.mapper(ImplementationVersionMapper.class);

    public ImplementationVersionDb convertResourceToResourceDb(ImplementationVersion object) {
        return mapper.asImplementationVersionDb(object);
    }

    public ImplementationVersion convertResourceDbToResource(ImplementationVersionDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asImplementationVersion((ImplementationVersionDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<ImplementationVersion> convertResourceDbToResource(Stream<ImplementationVersionDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (ImplementationVersionDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asImplementationVersion(i));
    }
}
