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
package org.kathra.resourcemanager.resource.dao;

import com.arangodb.ArangoCursor;
import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.MappingTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AbstractResourceDaoTest {

    AbstractResourceDaoImplTest underTest;

    ArangoOperations operations;
    CrudRepository<ResourceTestDb, String> repository;

    private final String CALLER_NAME_MOCKED_SESSION = "stevie wonder ! i'm free !";

    private class AbstractResourceDaoImplTest extends AbstractResourceDao<ResourceTest, ResourceTestDb, String> {

        public AbstractResourceDaoImplTest(CrudRepository<ResourceTestDb, String> repository, ArangoOperations operations) {
            super(repository, operations);
        }
    }

    @Test
    public void check_convertion_test() throws Exception {
        new MappingTestUtils().testMappingClasses(ResourceTest.class, ResourceTestDb.class, underTest);
    }

    @BeforeEach
    public void init(){
        repository = Mockito.mock(CrudRepository.class);
        operations = Mockito.mock(ArangoOperations.class);
        underTest = new AbstractResourceDaoImplTest(repository, operations);
    }



    @Test
    public void given_existing_id_when_findById_then_should_return_optional_with_object(){
        String id = "123456";
        ResourceTestDb resourceDb = new ResourceTestDb();
        resourceDb.setId(id);
        Optional<ResourceTestDb> resourceDbOpt = Optional.of(resourceDb);
        Mockito.when(repository.findById(id)).thenReturn(resourceDbOpt);

        Optional<ResourceTest> result = underTest.findById(id);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(ResourceTest.class, result.get().getClass());
        Assertions.assertEquals(id, result.get().getId());
    }


    @Test
    public void given_no_existing_id_when_findById_then_should_return_optional_empty(){
        String id = "123456";
        Optional<ResourceTestDb> resourceDbOpt = Optional.empty();
        Mockito.when(repository.findById(id)).thenReturn(resourceDbOpt);
        Optional<ResourceTest> result = underTest.findById(id);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void given_existing_object_when_update_then_call_method_save_of_repository() throws Exception {
        String id = "123456";
        ResourceTest resource = new ResourceTest();
        resource.setId(id);
        ArgumentCaptor<ResourceTestDb> argumentCaptor = ArgumentCaptor.forClass(ResourceTestDb.class);
        ResourceTestDb resourceDbGenerated = new ResourceTestDb();
        Mockito.when(repository.save(argumentCaptor.capture())).thenReturn(resourceDbGenerated);
        underTest.update(resource, CALLER_NAME_MOCKED_SESSION);

        ResourceTestDb resourceDb = argumentCaptor.getValue();
        Assertions.assertEquals(id, resourceDb.getId());
        Assertions.assertNotNull(resourceDb.getUpdatedAt());
        Assertions.assertNotNull(resourceDb.getUpdatedBy());
    }

    @Test
    public void given_existing_object_when_create_then_call_method_save_of_repository_and_generate_uuid() throws Exception {
        ResourceTest object = new ResourceTest();

        ResourceTestDb objectDb = new ResourceTestDb();
        ArgumentCaptor<?> operationsInsertCapture = ArgumentCaptor.forClass(Object.class);

        ArangoCursor<ResourceReference> noExistingUUID = Mockito.mock(ArangoCursor.class);
        Mockito.when(noExistingUUID.count()).thenReturn((long) 0);
        Mockito.when(operations.query(Mockito.anyString(), Mockito.eq(ResourceReference.class))).thenReturn(noExistingUUID);

        Mockito.when(operations.insert(operationsInsertCapture.capture())).thenReturn(null);

        underTest.create(object, CALLER_NAME_MOCKED_SESSION);

        List<?> valuesCaptured = operationsInsertCapture.getAllValues();
        ResourceReference resourceReferenceCaptured = (ResourceReference) valuesCaptured.get(0);
        ResourceTestDb objectDbCaptured = (ResourceTestDb) valuesCaptured.get(1);

        Mockito.verify(operations).query(Mockito.anyString(),  Mockito.eq(ResourceReference.class));
        Mockito.verify(operations).insert(Mockito.any(ResourceReference.class));
        Mockito.verify(operations).insert(Mockito.any(ResourceTestDb.class));


        Assertions.assertNotNull(resourceReferenceCaptured.uuid);
        Assertions.assertEquals(objectDbCaptured.getId(), resourceReferenceCaptured.uuid);
        Assertions.assertEquals(objectDbCaptured.getStatus(), Resource.StatusEnum.PENDING);
        Assertions.assertNotNull(objectDbCaptured.getCreatedAt());
        Assertions.assertEquals(objectDbCaptured.getUpdatedAt(), 0);

        Assertions.assertEquals(object.getId(), resourceReferenceCaptured.uuid);
        Assertions.assertEquals(object.getStatus(), Resource.StatusEnum.PENDING);
        Assertions.assertNotNull(object.getCreatedAt());
        Assertions.assertEquals(object.getUpdatedAt(), null);

        Assertions.assertEquals(object.getCreatedBy(), CALLER_NAME_MOCKED_SESSION);
        Assertions.assertEquals(objectDbCaptured.getCreatedBy(), CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_object_when_update_then_call_method_save_of_repository() throws Exception {

        ResourceTest object = new ResourceTest();
        object.id("fd6fdsf45s6");
        object.setCreatedAt((int) (System.currentTimeMillis()/1000) - 1000);
        object.setUpdatedAt(object.getCreatedAt());

        ResourceTestDb objectDb = new ResourceTestDb();
        objectDb.setId("fd6fdsf45s6");
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

        ResourceTest object = new ResourceTest();
        object.id("fd6fdsf45s6");
        object.setCreatedAt((int) (System.currentTimeMillis()/1000) - 10000);
        object.setUpdatedAt(object.getCreatedAt());

        ResourceTestDb objectDb = new ResourceTestDb();
        objectDb.setId("fd6fdsf45s6");
        objectDb.setCreatedAt(object.getCreatedAt());
        objectDb.setUpdatedAt(object.getCreatedAt());

        Mockito.when(repository.findById(object.getId())).thenReturn(Optional.of(objectDb));

        ArgumentCaptor<?> saveCapture = ArgumentCaptor.forClass(Object.class);

        Mockito.when(operations.update(Mockito.anyString(), saveCapture.capture())).thenReturn(null);

        underTest.delete(object, CALLER_NAME_MOCKED_SESSION);

        ResourceReference resourceReference = (ResourceReference) saveCapture.getValue();

        Mockito.verify(repository).save(objectDb);
        Mockito.verify(operations).update(object.getId(), resourceReference);

        Assertions.assertEquals(resourceReference.status, Resource.StatusEnum.DELETED);
        Assertions.assertEquals(object.getStatus(), Resource.StatusEnum.DELETED);
        Assertions.assertEquals(objectDb.getStatus(), Resource.StatusEnum.DELETED);
        Assertions.assertEquals(objectDb.getCreatedAt(), object.getCreatedAt().longValue());
        Assertions.assertNotEquals(object.getCreatedAt(), object.getUpdatedAt());
        Assertions.assertEquals(objectDb.getUpdatedAt(), object.getUpdatedAt().longValue());

        Assertions.assertEquals(object.getUpdatedBy(), CALLER_NAME_MOCKED_SESSION);
        Assertions.assertEquals(objectDb.getUpdatedBy(), CALLER_NAME_MOCKED_SESSION);

    }


}
