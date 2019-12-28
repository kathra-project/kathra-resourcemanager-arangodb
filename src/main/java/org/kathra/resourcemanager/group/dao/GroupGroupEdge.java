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
import org.kathra.resourcemanager.group.dao.GroupDb;


/**
 * Edge linking Group and Group
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2019-12-28T18:27:02.168Z
 * @author Julien Boubechtoula
 */
@Edge
public class GroupGroupEdge {

    @Id
    public String id;

    @From(lazy = true)
    public GroupDb parent;

    @To(lazy = true)
    public GroupDb child;

    public GroupGroupEdge(){

    }

    public GroupGroupEdge(final GroupDb parent, final GroupDb child) {
        this.child = child;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupDb getParent() {
        return parent;
    }

    public void setParent(GroupDb parent) {
        this.parent = parent;
    }

    public GroupDb getChild() {
        return child;
    }

    public void setChild(GroupDb child) {
        this.child = child;
    }
}
