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

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.CatalogEntryPackage;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.kathra.resourcemanager.resource.utils.LeanResourceDbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.kathra.resourcemanager.resource.utils.EdgeUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.kathra.resourcemanager.catalogentry.dao.CatalogEntryDb;
import org.kathra.resourcemanager.catalogentrypackage.dao.CatalogEntryPackageCatalogEntryEdge;
import org.kathra.resourcemanager.catalogentrypackage.dao.CatalogEntryPackageCatalogEntryEdgeRepository;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryDb;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryCatalogEntryPackageEdge;
import org.kathra.resourcemanager.sourcerepository.dao.SourceRepositoryCatalogEntryPackageEdgeRepository;
import org.kathra.resourcemanager.pipeline.dao.PipelineDb;
import org.kathra.resourcemanager.pipeline.dao.PipelineCatalogEntryPackageEdge;
import org.kathra.resourcemanager.pipeline.dao.PipelineCatalogEntryPackageEdgeRepository;


/**
 * Dao service to manage CatalogEntryPackage using CatalogEntryPackageDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T23:49:10.399Z
 * @author Julien Boubechtoula
 */
@Service
public class CatalogEntryPackageDao extends AbstractCatalogEntryPackageDao {

    public CatalogEntryPackageDao(@Autowired CatalogEntryPackageRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }
}
