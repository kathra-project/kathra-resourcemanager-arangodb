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

package org.kathra.resourcemanager.implementation.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.Implementation;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.kathra.resourcemanager.resource.utils.EdgeUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.kathra.resourcemanager.component.dao.ComponentDb;
import org.kathra.resourcemanager.implementation.dao.ImplementationComponentEdge;
import org.kathra.resourcemanager.implementation.dao.ImplementationComponentEdgeRepository;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionDb;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdgeRepository;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryImplementationEdge;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryImplementationEdgeRepository;
import org.kathra.resourcemanager.pipeline.dao.PipelineDb;
import org.kathra.resourcemanager.pipeline.dao.PipelineImplementationEdge;
import org.kathra.resourcemanager.pipeline.dao.PipelineImplementationEdgeRepository;


/**
 * Dao service to manage Implementation using ImplementationDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.0.1 at 2019-03-08T09:11:59.302Z
 * @author julien.boubechtoula
 */
@Service
public class ImplementationDao extends AbstractImplementationDao {

    public ImplementationDao(@Autowired ImplementationRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }
}
