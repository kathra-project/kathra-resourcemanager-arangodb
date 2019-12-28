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

package org.kathra.resourcemanager.implementation.dao;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.kathra.core.model.Implementation;
import org.kathra.core.model.Implementation.*;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import java.util.List;

import org.kathra.resourcemanager.component.dao.ComponentDb;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionDb;
import org.kathra.core.model.Asset.*;
import org.kathra.resourcemanager.implementation.dao.ImplementationComponentEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdge;


/**
 * Entity class ImplementationDb implementing db resource for class Implementation
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-28T17:06:31.486Z
 * @author Julien Boubechtoula
 */
@Document("Implementations")
public class ImplementationDb extends AbstractResourceDb<Implementation> {

		@Relations(edges = ImplementationComponentEdge.class, lazy = true) 
		private ComponentDb component; 
		private String description; 
		private LanguageEnum language; 
		private String title; 
		@Relations(edges = ImplementationVersionImplementationEdge.class, lazy = true) 
		private List<ImplementationVersionDb> versions; 


        public ImplementationDb() {
        }
        public ImplementationDb(String id) {
            super(id);
        }

		public ComponentDb getComponent() { return this.component;}
		public void setComponent(ComponentDb component) { this.component=component;}

		public String getDescription() { return this.description;}
		public void setDescription(String description) { this.description=description;}

		public LanguageEnum getLanguage() { return this.language;}
		public void setLanguage(LanguageEnum language) { this.language=language;}

		public String getTitle() { return this.title;}
		public void setTitle(String title) { this.title=title;}

		public List<ImplementationVersionDb> getVersions() { return this.versions;}
		public void setVersions(List<ImplementationVersionDb> versions) { this.versions=versions;}



}
