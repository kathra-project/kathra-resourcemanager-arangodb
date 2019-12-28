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

package org.kathra.resourcemanager.implementationversion.controller;

import org.kathra.core.model.ImplementationVersion;
import org.kathra.resourcemanager.implementationversion.service.ImplementationVersionService;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.resourcemanager.service.ImplementationVersionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.kathra.resourcemanager.resource.service.AbstractService;
import java.util.List;

/**
 * Abstract Controller implementing ImplementationVersionsService
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-28T17:06:34.894Z
 * @author Julien Boubechtoula
 */
public abstract class AbstractImplementationVersionsController implements AbstractCrudController<ImplementationVersion>, ImplementationVersionsService {

    @Autowired
    private ImplementationVersionService service;

    @Override
    public ImplementationVersionService getService() {
        return service;
    }

    public ImplementationVersion addImplementationVersion(ImplementationVersion object) throws Exception {
        return add(object);
    }

    public String deleteImplementationVersion(String resourceId) throws Exception {
        delete(resourceId);
        return resourceId;
    }

    public ImplementationVersion getImplementationVersion(String resourceId) throws Exception {
        return get(resourceId);
    }

    public List<ImplementationVersion> getImplementationVersions() throws Exception {
        return getAll();
    }

    public ImplementationVersion updateImplementationVersion(String resourceId, ImplementationVersion object) throws Exception {
        return update(resourceId, object);
    }

    public ImplementationVersion updateImplementationVersionAttributes(String resourceId, ImplementationVersion object) throws Exception {
        return patch(resourceId, object);
    }
}
