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

package org.kathra.resourcemanager.component.controller;

import org.kathra.core.model.Component;
import org.kathra.resourcemanager.component.service.ComponentService;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.resourcemanager.service.ComponentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.kathra.resourcemanager.resource.service.AbstractService;
import java.util.List;

/**
 * Abstract Controller implementing ComponentsService
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-28T17:06:29.718Z
 * @author Julien Boubechtoula
 */
public abstract class AbstractComponentsController implements AbstractCrudController<Component>, ComponentsService {

    @Autowired
    private ComponentService service;

    @Override
    public ComponentService getService() {
        return service;
    }

    public Component addComponent(Component object) throws Exception {
        return add(object);
    }

    public String deleteComponent(String resourceId) throws Exception {
        delete(resourceId);
        return resourceId;
    }

    public Component getComponent(String resourceId) throws Exception {
        return get(resourceId);
    }

    public List<Component> getComponents() throws Exception {
        return getAll();
    }

    public Component updateComponent(String resourceId, Component object) throws Exception {
        return update(resourceId, object);
    }

    public Component updateComponentAttributes(String resourceId, Component object) throws Exception {
        return patch(resourceId, object);
    }
}
