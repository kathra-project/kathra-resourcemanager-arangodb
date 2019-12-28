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
import org.kathra.resourcemanager.binaryrepository.dao.BinaryRepositoryDb;


/**
 * Edge linking Group and BinaryRepository
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-28T17:06:21.876Z
 * @author Julien Boubechtoula
 */
@Edge
public class GroupBinaryRepositoryEdge {

    @Id
    public String id;

    @From(lazy = true)
    public GroupDb group;

    @To(lazy = true)
    public BinaryRepositoryDb binaryRepository;

    public GroupBinaryRepositoryEdge(){

    }

    public GroupBinaryRepositoryEdge(final GroupDb group, final BinaryRepositoryDb binaryRepository) {
        this.binaryRepository = binaryRepository;
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

    public BinaryRepositoryDb getBinaryRepository() {
        return binaryRepository;
    }

    public void setBinaryRepository(BinaryRepositoryDb binaryRepository) {
        this.binaryRepository = binaryRepository;
    }
}
