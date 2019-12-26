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

package org.kathra.resourcemanager.sourcerepository.controller;

import org.kathra.core.model.SourceRepository;
import org.kathra.resourcemanager.sourcerepository.service.SourceRepositoryService;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.resourcemanager.service.SourceRepositoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.kathra.resourcemanager.resource.service.AbstractService;
import java.util.List;

/**
 * Abstract Controller implementing SourceRepositoriesService
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T18:48:13.978Z
 * @author jboubechtoula
 */
public abstract class AbstractSourceRepositoriesController implements AbstractCrudController<SourceRepository>, SourceRepositoriesService {

    @Autowired
    private SourceRepositoryService service;

    @Override
    public SourceRepositoryService getService() {
        return service;
    }

    public SourceRepository addSourceRepository(SourceRepository object) throws Exception {
        return add(object);
    }

    public String deleteSourceRepository(String resourceId) throws Exception {
        delete(resourceId);
        return resourceId;
    }

    public SourceRepository getSourceRepository(String resourceId) throws Exception {
        return get(resourceId);
    }

    public List<SourceRepository> getSourceRepositories() throws Exception {
        return getAll();
    }

    public SourceRepository updateSourceRepository(String resourceId, SourceRepository object) throws Exception {
        return update(resourceId, object);
    }

    public SourceRepository updateSourceRepositoryAttributes(String resourceId, SourceRepository object) throws Exception {
        return patch(resourceId, object);
    }
}
