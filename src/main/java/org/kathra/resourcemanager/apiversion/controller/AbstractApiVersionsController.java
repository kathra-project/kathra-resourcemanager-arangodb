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

package org.kathra.resourcemanager.apiversion.controller;

import org.kathra.core.model.ApiVersion;
import org.kathra.resourcemanager.apiversion.service.ApiVersionService;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.resourcemanager.service.ApiVersionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.kathra.resourcemanager.resource.service.AbstractService;
import java.util.List;

/**
 * Abstract Controller implementing ApiVersionsService
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T18:47:39.852Z
 * @author jboubechtoula
 */
public abstract class AbstractApiVersionsController implements AbstractCrudController<ApiVersion>, ApiVersionsService {

    @Autowired
    private ApiVersionService service;

    @Override
    public ApiVersionService getService() {
        return service;
    }

    public ApiVersion addApiVersion(ApiVersion object) throws Exception {
        return add(object);
    }

    public String deleteApiVersion(String resourceId) throws Exception {
        delete(resourceId);
        return resourceId;
    }

    public ApiVersion getApiVersion(String resourceId) throws Exception {
        return get(resourceId);
    }

    public List<ApiVersion> getApiVersions() throws Exception {
        return getAll();
    }

    public ApiVersion updateApiVersion(String resourceId, ApiVersion object) throws Exception {
        return update(resourceId, object);
    }

    public ApiVersion updateApiVersionAttributes(String resourceId, ApiVersion object) throws Exception {
        return patch(resourceId, object);
    }
}
