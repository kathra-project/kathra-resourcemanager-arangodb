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

package org.kathra.resourcemanager.sourcerepository.dao;

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ArangoDb Edge Repository linking SourceRepository and ImplementationVersion
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-01-12T20:00:38.183Z
 * @author jboubechtoula
 */
@Repository
public interface SourceRepositoryImplementationVersionEdgeRepository extends ArangoRepository<SourceRepositoryImplementationVersionEdge, String> {

    public List<SourceRepositoryImplementationVersionEdge> findAllBySourceRepository(String from);
    public List<SourceRepositoryImplementationVersionEdge> findAllByImplementationVersion(String to);
    public void deleteBySourceRepository(String from);
    public void deleteByImplementationVersion(String to);
}
