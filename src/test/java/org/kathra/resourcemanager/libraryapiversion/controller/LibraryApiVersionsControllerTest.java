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

package org.kathra.resourcemanager.libraryapiversion.controller;

import com.google.common.collect.ImmutableList;
import org.kathra.core.model.LibraryApiVersion;
import org.kathra.resourcemanager.libraryapiversion.service.LibraryApiVersionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
 * Class testing LibraryApiVersionsService
 *
 * Auto-generated by resource-db-generator@1.1.1 at 2019-04-12T13:51:00.494Z
 * @author julien.boubechtoula
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LibraryApiVersionsControllerTest {

    @InjectMocks
    LibraryApiVersionsController underTest = new LibraryApiVersionsController();

    @Mock
    LibraryApiVersionService service;

    @Test
    public void given_object_when_create_then_works() throws Exception {
        LibraryApiVersion object = new LibraryApiVersion();

        LibraryApiVersion objectWithID = copy(object);
        objectWithID.id("10dc3816-1994-4f81-b8d5-f94dbf29dea5");

        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) throws Exception {
                Object[] args = invocation.getArguments();
                ((LibraryApiVersion)invocation.getArgument(0)).setId(objectWithID.getId());
                Mockito.when(service.findById(objectWithID.getId())).thenReturn(Optional.of(objectWithID));
                return null;
            }
        }).when(service).create(Mockito.eq(object));

        LibraryApiVersion returned = underTest.addLibraryApiVersion(object);
        Assertions.assertEquals(returned, objectWithID);
    }

    @Test
    public void given_nothing_when_findAll_then_return_list() throws Exception {
        List<LibraryApiVersion> excepted = ImmutableList.of(new LibraryApiVersion().id("10dc3816-1994-4f81-b8d5-f94dbf29dea5"), new LibraryApiVersion().id("5sf7sd4qsd46q"));
        Mockito.when(service.findAll()).thenReturn(excepted);
        List<LibraryApiVersion> returned = underTest.getLibraryApiVersions();
        Assertions.assertEquals(returned, excepted);
    }


    @Test
    public void given_object_when_update_then_works() throws Exception {
        LibraryApiVersion object = new LibraryApiVersion();
        object.id("10dc3816-1994-4f81-b8d5-f94dbf29dea5");

        final Integer updatedAt = (int) (System.currentTimeMillis()/1000);
        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) throws Exception {
                Object[] args = invocation.getArguments();
                ((LibraryApiVersion)invocation.getArgument(0)).setUpdatedAt(updatedAt);
                Mockito.when(service.findById(object.getId())).thenReturn(Optional.of(((LibraryApiVersion)invocation.getArgument(0))));
                return null;
            }
        }).when(service).update(Mockito.eq(object));

        LibraryApiVersion returned = underTest.updateLibraryApiVersion(object.getId(), object);
        Assertions.assertEquals(returned.getUpdatedAt(), updatedAt);
    }

    @Test
    public void given_object_when_patch_then_works() throws Exception {
        LibraryApiVersion object = new LibraryApiVersion();
        object.id("10dc3816-1994-4f81-b8d5-f94dbf29dea5");
        Mockito.doReturn(Optional.of(object)).when(service).findById("10dc3816-1994-4f81-b8d5-f94dbf29dea5");
        final Integer updatedAt = (int) (System.currentTimeMillis()/1000);
        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) throws Exception {
                Object[] args = invocation.getArguments();
                ((LibraryApiVersion)invocation.getArgument(0)).setUpdatedAt(updatedAt);
                Mockito.when(service.findById(object.getId())).thenReturn(Optional.of(((LibraryApiVersion)invocation.getArgument(0))));
                return null;
            }
        }).when(service).patch(Mockito.eq(object));

        LibraryApiVersion returned = underTest.updateLibraryApiVersionAttributes(object.getId(), object);
        Assertions.assertEquals(returned.getUpdatedAt(), updatedAt);
    }

    @Test
    public void given_object_when_delete_then_works() throws Exception {
        LibraryApiVersion object = new LibraryApiVersion();
        object.id("10dc3816-1994-4f81-b8d5-f94dbf29dea5");
        Mockito.when(service.findById(object.getId())).thenReturn(Optional.of(object));
        underTest.deleteLibraryApiVersion(object.getId());
        Mockito.verify(service).delete(Mockito.argThat(i -> i.getId().equals(object.getId())));
    }

    public LibraryApiVersion copy(LibraryApiVersion object){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(object, LibraryApiVersion.class);
    }
}
