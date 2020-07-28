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

package org.kathra.resourcemanager.keypair.dao;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.kathra.core.model.KeyPair;
import org.kathra.core.model.KeyPair.*;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import java.util.List;

import org.kathra.resourcemanager.group.dao.GroupDb;
import org.kathra.core.model.Resource.*;
import org.kathra.resourcemanager.keypair.dao.KeyPairGroupEdge;


/**
 * Entity class KeyPairDb implementing db resource for class KeyPair
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:17.708Z
 * @author jboubechtoula
 */
@Document("KeyPairs")
public class KeyPairDb extends AbstractResourceDb<KeyPair> {

		private String privateKey; 
		private String publicKey; 
		@Relations(edges = KeyPairGroupEdge.class, lazy = true) 
		private GroupDb group; 


        public KeyPairDb() {
        }
        public KeyPairDb(String id) {
            super(id);
        }

		public String getPrivateKey() { return this.privateKey;}
		public void setPrivateKey(String privateKey) { this.privateKey=privateKey;}

		public String getPublicKey() { return this.publicKey;}
		public void setPublicKey(String publicKey) { this.publicKey=publicKey;}

		public GroupDb getGroup() { return this.group;}
		public void setGroup(GroupDb group) { this.group=group;}



}
