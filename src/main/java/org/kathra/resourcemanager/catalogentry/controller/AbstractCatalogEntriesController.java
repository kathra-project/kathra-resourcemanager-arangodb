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

package org.kathra.resourcemanager.catalogentry.controller;

import org.kathra.core.model.CatalogEntry;
import org.kathra.resourcemanager.catalogentry.service.CatalogEntryService;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.resourcemanager.service.CatalogEntriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.kathra.resourcemanager.resource.service.AbstractService;
import java.util.List;

/**
 * Abstract Controller implementing CatalogEntriesService
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:08.725Z
 * @author jboubechtoula
 */
public abstract class AbstractCatalogEntriesController implements AbstractCrudController<CatalogEntry>, CatalogEntriesService {

    @Autowired
    private CatalogEntryService service;

    @Override
    public CatalogEntryService getService() {
        return service;
    }

    public CatalogEntry addCatalogEntry(CatalogEntry object) throws Exception {
        return add(object);
    }

    public String deleteCatalogEntry(String resourceId) throws Exception {
        delete(resourceId);
        return resourceId;
    }

    public CatalogEntry getCatalogEntry(String resourceId) throws Exception {
        return get(resourceId);
    }

    public List<CatalogEntry> getCatalogEntries() throws Exception {
        return getAll();
    }

    public CatalogEntry updateCatalogEntry(String resourceId, CatalogEntry object) throws Exception {
        return update(resourceId, object);
    }

    public CatalogEntry updateCatalogEntryAttributes(String resourceId, CatalogEntry object) throws Exception {
        return patch(resourceId, object);
    }
}