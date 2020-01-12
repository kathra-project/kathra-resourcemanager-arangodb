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

package org.kathra.resourcemanager.implementationversion.dao;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.kathra.core.model.ImplementationVersion;
import org.kathra.core.model.ImplementationVersion.*;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import java.util.List;

import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.resourcemanager.implementation.dao.ImplementationDb;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionDb;
import org.kathra.core.model.Resource.*;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryImplementationVersionEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionApiVersionEdge;


/**
 * Entity class ImplementationVersionDb implementing db resource for class ImplementationVersion
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-01-12T20:00:38.184Z
 * @author jboubechtoula
 */
@Document("ImplementationVersions")
public class ImplementationVersionDb extends AbstractResourceDb<ImplementationVersion> {

		@Relations(edges = SourceRepositoryImplementationVersionEdge.class, lazy = true) 
		private SourceRepositoryDb sourceRepo; 
		private String version; 
		@Relations(edges = ImplementationVersionImplementationEdge.class, lazy = true) 
		private ImplementationDb implementation; 
		@Relations(edges = ImplementationVersionApiVersionEdge.class, lazy = true) 
		private ApiVersionDb apiVersion; 


        public ImplementationVersionDb() {
        }
        public ImplementationVersionDb(String id) {
            super(id);
        }

		public SourceRepositoryDb getSourceRepo() { return this.sourceRepo;}
		public void setSourceRepo(SourceRepositoryDb sourceRepo) { this.sourceRepo=sourceRepo;}

		public String getVersion() { return this.version;}
		public void setVersion(String version) { this.version=version;}

		public ImplementationDb getImplementation() { return this.implementation;}
		public void setImplementation(ImplementationDb implementation) { this.implementation=implementation;}

		public ApiVersionDb getApiVersion() { return this.apiVersion;}
		public void setApiVersion(ApiVersionDb apiVersion) { this.apiVersion=apiVersion;}



}
