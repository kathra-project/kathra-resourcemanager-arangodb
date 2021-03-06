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

package org.kathra.resourcemanager.pipeline.dao;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.kathra.core.model.Pipeline;
import org.kathra.core.model.Pipeline.*;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import java.util.List;

import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.core.model.Resource.*;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryPipelineEdge;


/**
 * Entity class PipelineDb implementing db resource for class Pipeline
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:21.622Z
 * @author jboubechtoula
 */
@Document("Pipelines")
public class PipelineDb extends AbstractResourceDb<Pipeline> {

		private String provider; 
		private String providerId; 
		private String credentialId; 
		private String path; 
		@Relations(edges = SourceRepositoryPipelineEdge.class, lazy = true) 
		private SourceRepositoryDb sourceRepository; 
		private TemplateEnum template; 


        public PipelineDb() {
        }
        public PipelineDb(String id) {
            super(id);
        }

		public String getProvider() { return this.provider;}
		public void setProvider(String provider) { this.provider=provider;}

		public String getProviderId() { return this.providerId;}
		public void setProviderId(String providerId) { this.providerId=providerId;}

		public String getCredentialId() { return this.credentialId;}
		public void setCredentialId(String credentialId) { this.credentialId=credentialId;}

		public String getPath() { return this.path;}
		public void setPath(String path) { this.path=path;}

		public SourceRepositoryDb getSourceRepository() { return this.sourceRepository;}
		public void setSourceRepository(SourceRepositoryDb sourceRepository) { this.sourceRepository=sourceRepository;}

		public TemplateEnum getTemplate() { return this.template;}
		public void setTemplate(TemplateEnum template) { this.template=template;}



}
