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

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ArangoDb Edge Repository linking Component and ApiVersion
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T13:40:43.022Z
 * @author jboubechtoula
 */
@Repository
public interface ComponentApiVersionEdgeRepository extends ArangoRepository<ComponentApiVersionEdge, String> {

    public List<ComponentApiVersionEdge> findAllByComponent(String from);
    public List<ComponentApiVersionEdge> findAllByApiVersion(String to);
    public void deleteByComponent(String from);
    public void deleteByApiVersion(String to);
}
