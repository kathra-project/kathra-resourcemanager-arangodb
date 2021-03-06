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

package org.kathra.resourcemanager.catalogentrypackage.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.catalogentrypackage.dao.CatalogEntryPackageDb;
import org.kathra.resourcemanager.binaryrepository.dao.BinaryRepositoryDb;


/**
 * Edge linking CatalogEntryPackage and BinaryRepository
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:10.905Z
 * @author jboubechtoula
 */
@Edge
public class CatalogEntryPackageBinaryRepositoryEdge {

    @Id
    public String id;

    @From(lazy = true)
    public CatalogEntryPackageDb catalogEntryPackage;

    @To(lazy = true)
    public BinaryRepositoryDb binaryRepository;

    public CatalogEntryPackageBinaryRepositoryEdge(){

    }

    public CatalogEntryPackageBinaryRepositoryEdge(final CatalogEntryPackageDb catalogEntryPackage, final BinaryRepositoryDb binaryRepository) {
        this.binaryRepository = binaryRepository;
        this.catalogEntryPackage = catalogEntryPackage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CatalogEntryPackageDb getCatalogEntryPackage() {
        return catalogEntryPackage;
    }

    public void setCatalogEntryPackage(CatalogEntryPackageDb catalogEntryPackage) {
        this.catalogEntryPackage = catalogEntryPackage;
    }

    public BinaryRepositoryDb getBinaryRepository() {
        return binaryRepository;
    }

    public void setBinaryRepository(BinaryRepositoryDb binaryRepository) {
        this.binaryRepository = binaryRepository;
    }
}
