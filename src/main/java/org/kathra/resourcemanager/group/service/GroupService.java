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

package org.kathra.resourcemanager.group.service;

import org.kathra.core.model.Component;
import org.kathra.core.model.Group;
import org.kathra.resourcemanager.resource.service.AbstractService;
import org.kathra.resourcemanager.resource.service.security.ResourceSecured;
import org.kathra.resourcemanager.resource.service.security.ResourceSecuredAction;
import org.kathra.resourcemanager.resource.service.security.Scope;
import org.kathra.resourcemanager.security.SessionService;
import org.kathra.resourcemanager.group.dao.GroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business service managing Group
 *
 * Auto-generated by resource-db-generator@1.0.0 at 2019-01-09T10:45:38.750Z
 * @author julien.boubechtoula
 */
@Service
public class GroupService extends AbstractService<Group,String> {

    public GroupService(@Autowired GroupDao dao, @Autowired SessionService sessionService) {
        super(dao, sessionService);
    }

    @Override
    public void create(Group object) throws Exception {
        checkGroupPathUnicity(object);
        super.create(object);
    }

    @Override
    public void delete(Group object) throws Exception {
        super.delete(object);
    }

    @Override
    public void update(Group object) throws Exception {
        checkGroupPathUnicity(object);
        super.update(object);
    }

    @Override
    public void patch(Group object) throws Exception {
        if (object.getPath() != null) {
            checkGroupPathUnicity(object);
        }
        super.patch(object);
    }

    @Override
    public List<Group> findAll() throws Exception {
        return super.findAll();
    }

    private void checkGroupPathUnicity(Group object) throws Exception {
        if (object.getPath() == null) {
            throw new IllegalArgumentException("Group's path is null");
        } else {
            Optional<Group> groupWithSamePath = findByPath(object.getPath());
            if (findByPath(object.getPath()).isPresent() && (!groupWithSamePath.get().getId().equals(object.getId()) || object.getId() == null)) {
                throw new IllegalArgumentException("Group's path already exists");
            }
        }
    }

    public Optional<Group> findByPath(String path) throws Exception {
        return super.findAll().parallelStream().filter(group -> group.getPath().equals(path)).findFirst();
    }

    @Override
    public Optional<Group> findById(String id) throws Exception {
        return super.findById(id);
    }
}
