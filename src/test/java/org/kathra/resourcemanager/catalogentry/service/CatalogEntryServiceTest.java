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

package org.kathra.resourcemanager.catalogentry.service;

import com.google.common.collect.ImmutableList;
import org.kathra.core.model.CatalogEntry;
import org.kathra.core.model.Resource.StatusEnum;
import org.kathra.resourcemanager.security.SessionService;
import org.kathra.utils.Session;
import org.kathra.resourcemanager.catalogentry.dao.CatalogEntryDao;
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

/**
 * Class testing CatalogEntriesDao
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T23:40:38.325Z
 * @author Julien Boubechtoula
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CatalogEntryServiceTest {

    CatalogEntryService underTest;

    @Mock
    CatalogEntryDao dao;
    @Mock
    SessionService sessionService;

    private final String CALLER_NAME_MOCKED_SESSION = "stevie wonder ! i'm free !";

    @BeforeEach
    public void beforeEach(){
        Session session = Mockito.mock(Session.class);
        Mockito.when(session.getCallerName()).thenReturn(CALLER_NAME_MOCKED_SESSION);
        Mockito.when(sessionService.getCurrentSession()).thenReturn(session);
        underTest = new CatalogEntryService(dao, sessionService);
    }

    @Test
    public void given_object_when_create_then_works() throws Exception {
        CatalogEntry object = new CatalogEntry();
        underTest.create(object);
        Mockito.verify(dao).create(object, CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_object_when_update_then_works() throws Exception {
        CatalogEntry object = new CatalogEntry();
        underTest.update(object);
        Mockito.verify(dao).update(object, CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_object_when_delete_then_works() throws Exception {
        CatalogEntry object = new CatalogEntry();
        underTest.delete(object);
        Mockito.verify(dao).delete(object, CALLER_NAME_MOCKED_SESSION);
    }

    @Test
    public void given_objectId_when_findById_then_works() throws Exception {
        CatalogEntry excepted = new CatalogEntry();
        Mockito.when(dao.findById("1789")).thenReturn(Optional.of(excepted));
        Optional<CatalogEntry> returned = underTest.findById("1789");
        Assertions.assertEquals(returned.get(), excepted);
        Mockito.verify(dao).findById("1789");
    }

    @Test
    public void given_nothing_when_findAll_then_works() throws Exception {
        List<CatalogEntry> excepted = ImmutableList.of(new CatalogEntry().id("5sf76zf4raezf3z"), new CatalogEntry().id("5sf7sd4qsd46q"));
        Mockito.when(dao.findAll()).thenReturn(excepted);
        List<CatalogEntry> returned = underTest.findAll();
        Assertions.assertEquals(returned, excepted);
    }

    @Test
    public void given_partialObject_when_patch_then_return_fullObject() throws Exception {
        CatalogEntry object = new CatalogEntry();
        object.setId("fdf4z6f4raezf");
        object.setName("new-name");

        CatalogEntry objectFull = copy(object);
        objectFull.setName("old-name");
        objectFull.setStatus(StatusEnum.READY);
        objectFull.setCreatedAt((int) (System.currentTimeMillis()/1000)-5000);
        objectFull.setUpdatedAt((int) (System.currentTimeMillis()/1000));
        objectFull.setCreatedBy("xx");
        objectFull.setUpdatedBy("yy");

        Mockito.when(dao.findById(object.getId())).thenReturn(Optional.of(objectFull));

        ArgumentCaptor<CatalogEntry> objectCaptor = ArgumentCaptor.forClass(CatalogEntry.class);
        ArgumentCaptor<String> authorCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(dao).update(objectCaptor.capture(), authorCaptor.capture());

        underTest.patch(object);

        CatalogEntry objectCaptured = objectCaptor.getValue();
        Assertions.assertEquals(objectCaptured.getId(), objectFull.getId());
        Assertions.assertEquals(objectCaptured.getName(), object.getName());
        Assertions.assertEquals(objectCaptured.getStatus(), objectFull.getStatus());
        Assertions.assertEquals(objectCaptured.getCreatedBy(), objectFull.getCreatedBy());
        Assertions.assertEquals(objectCaptured.getCreatedAt(), objectFull.getCreatedAt());
        Assertions.assertEquals(objectCaptured.getUpdatedAt(), objectFull.getUpdatedAt());
        Assertions.assertEquals(objectCaptured.getUpdatedBy(), objectFull.getUpdatedBy());

        Assertions.assertEquals(authorCaptor.getValue(), CALLER_NAME_MOCKED_SESSION);
    }

    public CatalogEntry copy(CatalogEntry object){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(object, CatalogEntry.class);
    }
}
