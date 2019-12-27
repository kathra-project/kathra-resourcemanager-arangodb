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

package org.kathra.resourcemanager.binaryrepository.dao;

import com.arangodb.ArangoCursor;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.BinaryRepository;
import org.kathra.core.model.Resource.StatusEnum;
import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.MappingTestUtils;
import org.kathra.resourcemanager.binaryrepository.dao.BinaryRepositoryDao;
import org.kathra.resourcemanager.binaryrepository.dao.BinaryRepositoryDb;
import org.kathra.resourcemanager.binaryrepository.dao.BinaryRepositoryRepository;
import org.kathra.resourcemanager.resource.dao.ResourceReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import org.kathra.resourcemanager.group.dao.GroupBinaryRepositoryEdge;
import org.kathra.resourcemanager.group.dao.GroupBinaryRepositoryEdgeRepository;


/**
 * Class testing BinaryRepositoriesDao
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-27T00:08:51.152Z
 * @author Julien Boubechtoula
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BinaryRepositoryDaoTest {

    @InjectMocks
    BinaryRepositoryDao underTest;

	@Mock
	GroupBinaryRepositoryEdgeRepository groupBinaryRepositoryEdgeRepository;


    @Mock
    BinaryRepositoryRepository repository;
    @Mock
    ArangoOperations operations;

    private final String CALLER_NAME_MOCKED_SESSION = "stevie wonder ! i'm free !";

    @BeforeEach
    public void beforeEach(){
		underTest.groupBinaryRepositoryEdgeRepository = groupBinaryRepositoryEdgeRepository;

        Mockito.reset();
        Mockito.when(operations.collection(ResourceReference.class.getAnnotationsByType(Document.class)[0].value())).thenReturn(null);
    }

    @Test
    public void check_convertion_test() throws Exception {
        new MappingTestUtils().testMappingClasses(BinaryRepository.class, BinaryRepositoryDb.class, underTest);
    }

    @Test
    public void given_object_when_create_then_work() throws Exception {
        BinaryRepository object = new BinaryRepository();

        BinaryRepositoryDb objectDb = new BinaryRepositoryDb();
        ArgumentCaptor<?> operationsInsertCapture = ArgumentCaptor.forClass(Object.class);

        ArangoCursor<ResourceReference> noExistingUUID = Mockito.mock(ArangoCursor.class);
        Mockito.when(noExistingUUID.count()).thenReturn((long) 0);
        Mockito.when(operations.query(Mockito.anyString(), Mockito.eq(ResourceReference.class))).thenReturn(noExistingUUID);

        Mockito.when(operations.insert(operationsInsertCapture.capture())).thenReturn(null);

        underTest.create(object, CALLER_NAME_MOCKED_SESSION);

        List<?> valuesCaptured = operationsInsertCapture.getAllValues();
        ResourceReference resourceReferenceCaptured = (ResourceReference) valuesCaptured.get(0);
        BinaryRepositoryDb objectDbCaptured = (BinaryRepositoryDb) valuesCaptured.get(1);

        Mockito.verify(operations).query(Mockito.anyString(),  Mockito.eq(ResourceReference.class));
        Mockito.verify(operations).insert(Mockito.any(ResourceReference.class));
        Mockito.verify(operations).insert(Mockito.any(BinaryRepositoryDb.class));


        Assertions.assertNotNull(resourceReferenceCaptured.uuid);
        Assertions.assertEquals(objectDbCaptured.getId(), resourceReferenceCaptured.uuid);
        Assertions.assertEquals(objectDbCaptured.getStatus(), StatusEnum.PENDING);
        Assertions.assertNotNull(objectDbCaptured.getCreatedAt());
        Assertions.assertEquals(objectDbCaptured.getUpdatedAt(), 0);
        Assertions.assertEquals(objectDbCaptured.getCreatedBy(), CALLER_NAME_MOCKED_SESSION);

        Assertions.assertEquals(object.getId(), resourceReferenceCaptured.uuid);
        Assertions.assertEquals(object.getStatus(), StatusEnum.PENDING);
        Assertions.assertNotNull(object.getCreatedAt());
        Assertions.assertEquals(object.getUpdatedAt(), null);
        Assertions.assertEquals(object.getCreatedBy(), CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_object_when_update_then_works() throws Exception {

        BinaryRepository object = new BinaryRepository();
        object.id("10dc3816-1994-4f81-b8d5-f94dbf29dea5");
        object.setCreatedAt((int) (System.currentTimeMillis()/1000) - 1000);
        object.setUpdatedAt(object.getCreatedAt());

        BinaryRepositoryDb objectDb = new BinaryRepositoryDb();
        objectDb.setId("10dc3816-1994-4f81-b8d5-f94dbf29dea5");
        objectDb.setCreatedAt(object.getCreatedAt());
        objectDb.setUpdatedAt(object.getCreatedAt());
        Mockito.when(repository.findById(object.getId())).thenReturn(Optional.of(objectDb));

        ArgumentCaptor<?> saveCapture = ArgumentCaptor.forClass(Object.class);
        Mockito.when(operations.update(Mockito.anyString(), saveCapture.capture())).thenReturn(null);

        underTest.update(object, CALLER_NAME_MOCKED_SESSION);

        ResourceReference resourceReference = (ResourceReference) saveCapture.getValue();

        Mockito.verify(repository).save(objectDb);
        Mockito.verify(operations).update(object.getId(), resourceReference);
        Assertions.assertEquals(object.getUpdatedBy(), CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_object_when_delete_then_works() throws Exception {

        BinaryRepository object = new BinaryRepository();
        object.id("10dc3816-1994-4f81-b8d5-f94dbf29dea5");
        object.setCreatedAt((int) (System.currentTimeMillis()/1000) - 10000);
        object.setUpdatedAt(object.getCreatedAt());

        BinaryRepositoryDb objectDb = new BinaryRepositoryDb();
        objectDb.setId("10dc3816-1994-4f81-b8d5-f94dbf29dea5");
        objectDb.setCreatedAt(object.getCreatedAt());
        objectDb.setUpdatedAt(object.getCreatedAt());

        Mockito.when(repository.findById(object.getId())).thenReturn(Optional.of(objectDb));

        ArgumentCaptor<?> saveCapture = ArgumentCaptor.forClass(Object.class);

        Mockito.when(operations.update(Mockito.anyString(), saveCapture.capture())).thenReturn(null);

        underTest.delete(object, CALLER_NAME_MOCKED_SESSION);

        ResourceReference resourceReference = (ResourceReference) saveCapture.getValue();

        Mockito.verify(repository).save(objectDb);
        Mockito.verify(operations).update(object.getId(), resourceReference);

        Assertions.assertEquals(resourceReference.status, StatusEnum.DELETED);
        Assertions.assertEquals(object.getStatus(), StatusEnum.DELETED);
        Assertions.assertEquals(objectDb.getStatus(), StatusEnum.DELETED);
        Assertions.assertEquals(objectDb.getCreatedAt(), object.getCreatedAt().longValue());
        Assertions.assertNotEquals(object.getCreatedAt(), object.getUpdatedAt());
        Assertions.assertEquals(objectDb.getUpdatedAt(), object.getUpdatedAt().longValue());

        Assertions.assertEquals(object.getUpdatedBy(), CALLER_NAME_MOCKED_SESSION);
        Assertions.assertEquals(objectDb.getUpdatedBy(), CALLER_NAME_MOCKED_SESSION);
    }
}
