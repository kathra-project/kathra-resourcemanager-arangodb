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

package org.kathra.resourcemanager.apiversion.dao;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.kathra.core.model.ApiVersion;
import org.kathra.core.model.ApiVersion.*;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import java.util.List;

import org.kathra.resourcemanager.component.dao.ComponentDb;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionDb;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionDb;
import org.kathra.core.model.Resource.*;
import org.kathra.resourcemanager.component.dao.ComponentApiVersionEdge;
import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionApiVersionEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionApiVersionEdge;


/**
 * Entity class ApiVersionDb implementing db resource for class ApiVersion
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-11-14T21:10:29.451Z
 * @author jboubechtoula
 */
@Document("ApiVersions")
public class ApiVersionDb extends AbstractResourceDb<ApiVersion> {

		@Relations(edges = ComponentApiVersionEdge.class, lazy = true) 
		private ComponentDb component; 
		private Boolean released; 
		private String version; 
		private ApiRepositoryStatusEnum apiRepositoryStatus; 
		@Relations(edges = LibraryApiVersionApiVersionEdge.class, lazy = true) 
		private List<LibraryApiVersionDb> librariesApiVersions; 
		@Relations(edges = ImplementationVersionApiVersionEdge.class, lazy = true) 
		private List<ImplementationVersionDb> implementationsVersions; 


        public ApiVersionDb() {
        }
        public ApiVersionDb(String id) {
            super(id);
        }

		public ComponentDb getComponent() { return this.component;}
		public void setComponent(ComponentDb component) { this.component=component;}

		public Boolean getReleased() { return this.released;}
		public void setReleased(Boolean released) { this.released=released;}

		public String getVersion() { return this.version;}
		public void setVersion(String version) { this.version=version;}

		public ApiRepositoryStatusEnum getApiRepositoryStatus() { return this.apiRepositoryStatus;}
		public void setApiRepositoryStatus(ApiRepositoryStatusEnum apiRepositoryStatus) { this.apiRepositoryStatus=apiRepositoryStatus;}

		public List<LibraryApiVersionDb> getLibrariesApiVersions() { return this.librariesApiVersions;}
		public void setLibrariesApiVersions(List<LibraryApiVersionDb> librariesApiVersions) { this.librariesApiVersions=librariesApiVersions;}

		public List<ImplementationVersionDb> getImplementationsVersions() { return this.implementationsVersions;}
		public void setImplementationsVersions(List<ImplementationVersionDb> implementationsVersions) { this.implementationsVersions=implementationsVersions;}



}
