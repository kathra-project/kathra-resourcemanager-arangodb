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

package org.kathra.resourcemanager.assignation.controller;

import org.kathra.core.model.Assignation;
import org.kathra.resourcemanager.assignation.service.AssignationService;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.resourcemanager.service.AssignationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.kathra.resourcemanager.resource.service.AbstractService;
import java.util.List;

/**
 * Abstract Controller implementing AssignationsService
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-01-12T20:00:27.079Z
 * @author jboubechtoula
 */
public abstract class AbstractAssignationsController implements AbstractCrudController<Assignation>, AssignationsService {

    @Autowired
    private AssignationService service;

    @Override
    public AssignationService getService() {
        return service;
    }

    public Assignation addAssignation(Assignation object) throws Exception {
        return add(object);
    }

    public String deleteAssignation(String resourceId) throws Exception {
        delete(resourceId);
        return resourceId;
    }

    public Assignation getAssignation(String resourceId) throws Exception {
        return get(resourceId);
    }

    public List<Assignation> getAssignations() throws Exception {
        return getAll();
    }

    public Assignation updateAssignation(String resourceId, Assignation object) throws Exception {
        return update(resourceId, object);
    }

    public Assignation updateAssignationAttributes(String resourceId, Assignation object) throws Exception {
        return patch(resourceId, object);
    }
}
