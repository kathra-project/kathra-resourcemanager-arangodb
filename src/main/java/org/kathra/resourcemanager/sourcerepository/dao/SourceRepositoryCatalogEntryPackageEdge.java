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

package org.kathra.resourcemanager.sourcerepository.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.resourcemanager.catalogentrypackage.dao.CatalogEntryPackageDb;


/**
 * Edge linking SourceRepository and CatalogEntryPackage
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-27T00:08:49.569Z
 * @author Julien Boubechtoula
 */
@Edge
public class SourceRepositoryCatalogEntryPackageEdge {

    @Id
    public String id;

    @From(lazy = true)
    public SourceRepositoryDb sourceRepository;

    @To(lazy = true)
    public CatalogEntryPackageDb catalogEntryPackage;

    public SourceRepositoryCatalogEntryPackageEdge(){

    }

    public SourceRepositoryCatalogEntryPackageEdge(final SourceRepositoryDb sourceRepository, final CatalogEntryPackageDb catalogEntryPackage) {
        this.catalogEntryPackage = catalogEntryPackage;
        this.sourceRepository = sourceRepository;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SourceRepositoryDb getSourceRepository() {
        return sourceRepository;
    }

    public void setSourceRepository(SourceRepositoryDb sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public CatalogEntryPackageDb getCatalogEntryPackage() {
        return catalogEntryPackage;
    }

    public void setCatalogEntryPackage(CatalogEntryPackageDb catalogEntryPackage) {
        this.catalogEntryPackage = catalogEntryPackage;
    }
}
