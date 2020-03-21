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

package org.kathra.resourcemanager.catalogentrypackage.service;

import org.kathra.core.model.CatalogEntryPackage;
import org.kathra.resourcemanager.resource.service.AbstractService;
import org.kathra.resourcemanager.security.SessionService;
import org.kathra.resourcemanager.catalogentrypackage.dao.CatalogEntryPackageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Business service managing CatalogEntryPackage
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T23:49:10.419Z
 * @author Julien Boubechtoula
 */
@Service
public class CatalogEntryPackageService extends AbstractService<CatalogEntryPackage,String> {

    public CatalogEntryPackageService(@Autowired CatalogEntryPackageDao dao, @Autowired SessionService sessionService) {
        super(dao, sessionService);
    }
}
