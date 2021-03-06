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

package org.kathra.resourcemanager.assignation.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.Assignation;
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



/**
 * Abtrasct dao service to manage Assignation using AssignationDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:07.141Z
 * @author jboubechtoula
 */
public abstract class AbstractAssignationDao extends AbstractResourceDao<Assignation, AssignationDb, String> {



    public AbstractAssignationDao(@Autowired AssignationRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new AssignationDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void create(Assignation object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(Assignation object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(Assignation object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(Assignation object) throws Exception {
        AssignationDb resourceDb = this.convertResourceToResourceDb(object);

    }


    AssignationMapper mapper = Selma.mapper(AssignationMapper.class);

    public AssignationDb convertResourceToResourceDb(Assignation object) {
        return mapper.asAssignationDb(object);
    }

    public Assignation convertResourceDbToResource(AssignationDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asAssignation((AssignationDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<Assignation> convertResourceDbToResource(Stream<AssignationDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (AssignationDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asAssignation(i));
    }
}
