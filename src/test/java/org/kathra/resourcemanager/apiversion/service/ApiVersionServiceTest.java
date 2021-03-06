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

package org.kathra.resourcemanager.apiversion.service;

import com.google.common.collect.ImmutableList;
import org.kathra.core.model.ApiVersion;
import org.kathra.core.model.Resource.StatusEnum;
import org.kathra.resourcemanager.component.service.ComponentService;
import org.kathra.resourcemanager.security.SessionService;
import org.kathra.utils.Session;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class testing ApiVersionsDao
 *
 * Auto-generated by resource-db-generator@1.0.0 at 2019-01-09T11:09:47.684Z
 * @author julien.boubechtoula
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ApiVersionServiceTest {

    ApiVersionService underTest;

    @Mock
    ApiVersionDao dao;
    @Mock
    SessionService sessionService;
    @Mock
    ComponentService componentService;

    private final String CALLER_NAME_MOCKED_SESSION = "stevie wonder ! i'm free !";

    @BeforeEach
    public void beforeEach(){
        Session session = Mockito.mock(Session.class);
        Mockito.when(session.getCallerName()).thenReturn(CALLER_NAME_MOCKED_SESSION);
        Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
        underTest = new ApiVersionService(dao, sessionService, componentService);
    }

    @Test
    public void given_object_when_create_then_works() throws Exception {
        ApiVersion object = new ApiVersion();
        underTest.create(object);
        Mockito.verify(dao).create(object, CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_object_when_update_then_works() throws Exception {
        ApiVersion object = new ApiVersion();
        underTest.update(object);
        Mockito.verify(dao).update(object, CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_object_when_delete_then_works() throws Exception {
        ApiVersion object = new ApiVersion();
        underTest.delete(object);
        Mockito.verify(dao).delete(object, CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_objectId_when_findById_then_works() throws Exception {
        ApiVersion excepted = new ApiVersion();
        Mockito.when(dao.findById("1789")).thenReturn(Optional.of(excepted));
        Optional<ApiVersion> returned = underTest.findById("1789");
        Assertions.assertEquals(returned.get(), excepted);
        Mockito.verify(dao).findById("1789");
    }

    @Test
    public void given_nothing_when_findAll_then_works() throws Exception {
        List<ApiVersion> excepted = ImmutableList.of(new ApiVersion().id("10dc3816-1994-4f81-b8d5-f94dbf29dea5"), new ApiVersion().id("5sf7sd4qsd46q"));
        List<String> componentIdsAuthorized = ImmutableList.of("component-id-authorized");

        Mockito.when(componentService.findAllIdsAuthorized()).thenReturn(componentIdsAuthorized);
        Mockito.when(dao.findAllByComponentIds(Mockito.eq(componentIdsAuthorized))).thenReturn(excepted.stream().map(apiVersion -> apiVersion.getId()).collect(Collectors.toList()));
        Mockito.when(dao.findAll(excepted.stream().map(apiVersion -> apiVersion.getId()).collect(Collectors.toList()))).thenReturn(excepted);

        List<ApiVersion> returned = underTest.findAll();
        Assertions.assertEquals(returned, excepted);
    }

    @Test
    public void given_partialObject_when_patch_then_return_fullObject() throws Exception {
        ApiVersion object = new ApiVersion();
        object.setId("fdf4z6f4raezf");
        object.setName("new-name");

        ApiVersion objectFull = copy(object);
        objectFull.setName("old-name");
        objectFull.setStatus(StatusEnum.READY);
        objectFull.setCreatedAt((int) (System.currentTimeMillis()/1000)-5000);
        objectFull.setUpdatedAt((int) (System.currentTimeMillis()/1000));
        objectFull.setCreatedBy("xx");
        objectFull.setUpdatedBy("yy");

        Mockito.when(dao.findById(object.getId())).thenReturn(Optional.of(objectFull));

        ArgumentCaptor<ApiVersion> objectCaptor = ArgumentCaptor.forClass(ApiVersion.class);
        ArgumentCaptor<String> authorCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(dao).update(objectCaptor.capture(), authorCaptor.capture());

        underTest.patch(object);

        ApiVersion objectCaptured = objectCaptor.getValue();
        Assertions.assertEquals(objectCaptured.getId(), objectFull.getId());
        Assertions.assertEquals(objectCaptured.getName(), object.getName());
        Assertions.assertEquals(objectCaptured.getStatus(), objectFull.getStatus());
        Assertions.assertEquals(objectCaptured.getCreatedBy(), objectFull.getCreatedBy());
        Assertions.assertEquals(objectCaptured.getCreatedAt(), objectFull.getCreatedAt());
        Assertions.assertEquals(objectCaptured.getUpdatedAt(), objectFull.getUpdatedAt());
        Assertions.assertEquals(objectCaptured.getUpdatedBy(), objectFull.getUpdatedBy());

        Assertions.assertEquals(authorCaptor.getValue(), CALLER_NAME_MOCKED_SESSION);
    }

    public ApiVersion copy(ApiVersion object){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(object, ApiVersion.class);
    }
}
