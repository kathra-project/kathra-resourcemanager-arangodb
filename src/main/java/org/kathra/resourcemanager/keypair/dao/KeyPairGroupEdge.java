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

package org.kathra.resourcemanager.keypair.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.keypair.dao.KeyPairDb;
import org.kathra.resourcemanager.group.dao.GroupDb;


/**
 * Edge linking KeyPair and Group
 *
 * Auto-generated by resource-db-generator@1.1.1 at 2019-04-12T13:51:03.259Z
 * @author julien.boubechtoula
 */
@Edge
public class KeyPairGroupEdge {

    @Id
    public String id;

    @From(lazy = true)
    public KeyPairDb keyPair;

    @To(lazy = true)
    public GroupDb group;

    public KeyPairGroupEdge(){

    }

    public KeyPairGroupEdge(final KeyPairDb keyPair, final GroupDb group) {
        this.group = group;
        this.keyPair = keyPair;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public KeyPairDb getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPairDb keyPair) {
        this.keyPair = keyPair;
    }

    public GroupDb getGroup() {
        return group;
    }

    public void setGroup(GroupDb group) {
        this.group = group;
    }
}
