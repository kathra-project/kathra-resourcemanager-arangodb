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

package org.kathra.resourcemanager.user.controller;

import org.kathra.core.model.User;
import org.kathra.resourcemanager.user.service.UserService;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.resourcemanager.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.kathra.resourcemanager.resource.service.AbstractService;
import java.util.List;

/**
 * Abstract Controller implementing UsersService
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T18:48:14.820Z
 * @author jboubechtoula
 */
public abstract class AbstractUsersController implements AbstractCrudController<User>, UsersService {

    @Autowired
    private UserService service;

    @Override
    public UserService getService() {
        return service;
    }

    public User addUser(User object) throws Exception {
        return add(object);
    }

    public String deleteUser(String resourceId) throws Exception {
        delete(resourceId);
        return resourceId;
    }

    public User getUser(String resourceId) throws Exception {
        return get(resourceId);
    }

    public List<User> getUsers() throws Exception {
        return getAll();
    }

    public User updateUser(String resourceId, User object) throws Exception {
        return update(resourceId, object);
    }

    public User updateUserAttributes(String resourceId, User object) throws Exception {
        return patch(resourceId, object);
    }
}
