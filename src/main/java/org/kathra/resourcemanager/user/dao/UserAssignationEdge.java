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

package org.kathra.resourcemanager.user.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.user.dao.UserDb;
import org.kathra.resourcemanager.assignation.dao.AssignationDb;


/**
 * Edge linking User and Assignation
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-27T00:08:43.593Z
 * @author Julien Boubechtoula
 */
@Edge
public class UserAssignationEdge {

    @Id
    public String id;

    @From(lazy = true)
    public UserDb user;

    @To(lazy = true)
    public AssignationDb assignation;

    public UserAssignationEdge(){

    }

    public UserAssignationEdge(final UserDb user, final AssignationDb assignation) {
        this.assignation = assignation;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDb getUser() {
        return user;
    }

    public void setUser(UserDb user) {
        this.user = user;
    }

    public AssignationDb getAssignation() {
        return assignation;
    }

    public void setAssignation(AssignationDb assignation) {
        this.assignation = assignation;
    }
}
