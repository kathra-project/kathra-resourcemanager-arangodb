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

package org.kathra.resourcemanager.pipeline.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.Pipeline;
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
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryPipelineEdge;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryPipelineEdgeRepository;


/**
 * Abtrasct dao service to manage Pipeline using PipelineDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T13:41:00.864Z
 * @author jboubechtoula
 */
public abstract class AbstractPipelineDao extends AbstractResourceDao<Pipeline, PipelineDb, String> {

	@Autowired
	SourceRepositoryPipelineEdgeRepository sourceRepositoryPipelineEdgeRepository;


    public AbstractPipelineDao(@Autowired PipelineRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new PipelineDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.sourceRepositoryPipelineEdgeRepository.count();

    }

    @Override
    public void create(Pipeline object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(Pipeline object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(Pipeline object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(Pipeline object) throws Exception {
        PipelineDb resourceDb = this.convertResourceToResourceDb(object);
        EdgeUtils.of(SourceRepositoryPipelineEdge.class).updateReference(resourceDb, "sourceRepository", sourceRepositoryPipelineEdgeRepository);

    }


    PipelineMapper mapper = Selma.mapper(PipelineMapper.class);

    public PipelineDb convertResourceToResourceDb(Pipeline object) {
        return mapper.asPipelineDb(object);
    }

    public Pipeline convertResourceDbToResource(PipelineDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asPipeline((PipelineDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<Pipeline> convertResourceDbToResource(Stream<PipelineDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (PipelineDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asPipeline(i));
    }
}
