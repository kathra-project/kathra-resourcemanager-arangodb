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
import org.kathra.resourcemanager.catalogentry.dao.CatalogEntryDb;
import org.kathra.resourcemanager.binaryrepository.dao.BinaryRepositoryDb;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.resourcemanager.pipeline.dao.PipelineDb;
import org.kathra.core.model.Asset.*;
import org.kathra.resourcemanager.implementation.dao.ImplementationComponentEdge;
import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionImplementationEdge;
import org.kathra.resourcemanager.implementation.dao.ImplementationCatalogEntryEdge;
import org.kathra.resourcemanager.implementation.dao.ImplementationBinaryRepositoryEdge;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryImplementationEdge;
import org.kathra.resourcemanager.pipeline.dao.PipelineImplementationEdge;


/**
 * Entity class ImplementationDb implementing db resource for class Implementation
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:15.475Z
 * @author jboubechtoula
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
		@Relations(edges = ImplementationCatalogEntryEdge.class, lazy = true) 
		private List<CatalogEntryDb> catalogEntries; 
		@Relations(edges = ImplementationBinaryRepositoryEdge.class, lazy = true) 
		private BinaryRepositoryDb binaryRepository; 
		@Relations(edges = SourceRepositoryImplementationEdge.class, lazy = true) 
		private SourceRepositoryDb sourceRepository; 
		@Relations(edges = PipelineImplementationEdge.class, lazy = true) 
		private PipelineDb pipeline; 


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

		public List<CatalogEntryDb> getCatalogEntries() { return this.catalogEntries;}
		public void setCatalogEntries(List<CatalogEntryDb> catalogEntries) { this.catalogEntries=catalogEntries;}

		public BinaryRepositoryDb getBinaryRepository() { return this.binaryRepository;}
		public void setBinaryRepository(BinaryRepositoryDb binaryRepository) { this.binaryRepository=binaryRepository;}

		public SourceRepositoryDb getSourceRepository() { return this.sourceRepository;}
		public void setSourceRepository(SourceRepositoryDb sourceRepository) { this.sourceRepository=sourceRepository;}

		public PipelineDb getPipeline() { return this.pipeline;}
		public void setPipeline(PipelineDb pipeline) { this.pipeline=pipeline;}



}
