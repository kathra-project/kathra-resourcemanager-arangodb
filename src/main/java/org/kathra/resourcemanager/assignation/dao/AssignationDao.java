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

package org.kathra.resourcemanager.assignation.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.Assignation;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.kathra.resourcemanager.resource.utils.EdgeUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;



/**
 * Dao service to manage Assignation using AssignationDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.0.1 at 2019-03-08T09:12:10.692Z
 * @author julien.boubechtoula
 */
@Service
public class AssignationDao extends AbstractAssignationDao {

    public AssignationDao(@Autowired AssignationRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

}
