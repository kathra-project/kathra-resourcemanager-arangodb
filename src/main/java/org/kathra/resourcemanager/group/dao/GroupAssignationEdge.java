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

package org.kathra.resourcemanager.group.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.group.dao.GroupDb;
import org.kathra.resourcemanager.assignation.dao.AssignationDb;


/**
 * Edge linking Group and Assignation
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T18:47:37.183Z
 * @author jboubechtoula
 */
@Edge
public class GroupAssignationEdge {

    @Id
    public String id;

    @From(lazy = true)
    public GroupDb group;

    @To(lazy = true)
    public AssignationDb assignation;

    public GroupAssignationEdge(){

    }

    public GroupAssignationEdge(final GroupDb group, final AssignationDb assignation) {
        this.assignation = assignation;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupDb getGroup() {
        return group;
    }

    public void setGroup(GroupDb group) {
        this.group = group;
    }

    public AssignationDb getAssignation() {
        return assignation;
    }

    public void setAssignation(AssignationDb assignation) {
        this.assignation = assignation;
    }
}
