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

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.kathra.core.model.Assignation;
import org.kathra.core.model.Assignation.*;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import java.util.List;

import org.kathra.core.model.Resource.*;


/**
 * Entity class AssignationDb implementing db resource for class Assignation
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T13:40:30.801Z
 * @author jboubechtoula
 */
@Document("Assignations")
public class AssignationDb extends AbstractResourceDb<Assignation> {

		private String role; 
		private String fte; 


        public AssignationDb() {
        }
        public AssignationDb(String id) {
            super(id);
        }

		public String getRole() { return this.role;}
		public void setRole(String role) { this.role=role;}

		public String getFte() { return this.fte;}
		public void setFte(String fte) { this.fte=fte;}



}
