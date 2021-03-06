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

package org.kathra.resourcemanager.keypair.dao;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.KeyPair;
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

import org.kathra.resourcemanager.group.dao.GroupDb;
import org.kathra.resourcemanager.keypair.dao.KeyPairGroupEdge;
import org.kathra.resourcemanager.keypair.dao.KeyPairGroupEdgeRepository;


/**
 * Abtrasct dao service to manage KeyPair using KeyPairDb with ArangoRepository
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:17.707Z
 * @author jboubechtoula
 */
public abstract class AbstractKeyPairDao extends AbstractResourceDao<KeyPair, KeyPairDb, String> {

	@Autowired
	KeyPairGroupEdgeRepository keyPairGroupEdgeRepository;


    public AbstractKeyPairDao(@Autowired KeyPairRepository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new KeyPairDb("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//init edge repositories 
		this.keyPairGroupEdgeRepository.count();

    }

    @Override
    public void create(KeyPair object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update(KeyPair object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete(KeyPair object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences(KeyPair object) throws Exception {
        KeyPairDb resourceDb = this.convertResourceToResourceDb(object);
        EdgeUtils.of(KeyPairGroupEdge.class).updateReference(resourceDb, "group", keyPairGroupEdgeRepository);

    }


    KeyPairMapper mapper = Selma.mapper(KeyPairMapper.class);

    public KeyPairDb convertResourceToResourceDb(KeyPair object) {
        return mapper.asKeyPairDb(object);
    }

    public KeyPair convertResourceDbToResource(KeyPairDb object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.asKeyPair((KeyPairDb) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<KeyPair> convertResourceDbToResource(Stream<KeyPairDb> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> (KeyPairDb) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.asKeyPair(i));
    }
}
