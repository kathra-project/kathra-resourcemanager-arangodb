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

package org.kathra.resourcemanager.user.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.User;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.kathra.resourcemanager.resource.utils.LeanResourceDbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.kathra.resourcemanager.resource.utils.EdgeUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import fr.xebia.extras.selma.Selma;
import java.util.stream.Stream;

import org.kathra.resourcemanager.assignation.dao.AssignationDb;
import org.kathra.resourcemanager.user.dao.UserAssignationEdge;
import org.kathra.resourcemanager.user.dao.UserAssignationEdgeRepository;


/**
 * Abtrasct dao service to manage User using UserDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:22.184Z
 * @author jboubechtoula
 */
public abstract class AbstractUserDao extends AbstractResourceDao<User, UserDb, String> {

	@Autowired
	UserAssignationEdgeRepository userAssignationEdgeRepository;


    public AbstractUserDao(@Autowired UserRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new UserDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.userAssignationEdgeRepository.count();

    }

    @Override
    public void create(User object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(User object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(User object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(User object) throws Exception {
        UserDb resourceDb = this.convertResourceToResourceDb(object);
        if (object.getGroups() != null) {
            List groupsItemsToUpdate = object.getGroups().parallelStream().filter(Objects::nonNull).map(i -> new AssignationDb(i.getId())).collect(Collectors.toList());
            EdgeUtils.of(UserAssignationEdge.class).updateList(resourceDb, groupsItemsToUpdate, userAssignationEdgeRepository);
        }

    }


    UserMapper mapper = Selma.mapper(UserMapper.class);

    public UserDb convertResourceToResourceDb(User object) {
        return mapper.asUserDb(object);
    }

    public User convertResourceDbToResource(UserDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asUser((UserDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<User> convertResourceDbToResource(Stream<UserDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (UserDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asUser(i));
    }
}
