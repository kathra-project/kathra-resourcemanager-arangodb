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
package org.kathra.resourcemanager.resource.utils;


import com.arangodb.springframework.annotation.Document;
import com.google.common.collect.ImmutableList;
import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EdgeUtilsTest {

    private EdgeUtils<ChildParentLnk> edgeUtils;
    private EdgeUtils<ChildParentLnk> edgeUtilsChildParentRecursiveLnk;

    public class ExampleParent extends Resource {
        private List<ExampleChild> list;
    }

    @Document("parentDoc")
    public class ExampleParentDb extends AbstractResourceDb<ExampleParent> {

        private List<ExampleChildDb> list;
        private ExampleParentDb recursive;

        public ExampleParentDb(String id){
            super(id);
        }

        public ExampleParentDb getRecursive() {
            return recursive;
        }

        public void setRecursive(ExampleParentDb recursive) {
            this.recursive = recursive;
        }

        public List<ExampleChildDb> getList() {
            return list;
        }

        public void setList(List<ExampleChildDb> list) {
            this.list = list;
        }
    }

    public class ExampleChild extends Resource {

    }

    @Document("childDoc")
    public class ExampleChildDb extends AbstractResourceDb<ExampleChild> {
        public ExampleChildDb(String id){
            super(id);
            setStatus(Resource.StatusEnum.READY);
        }

    }

    public interface RepositoryChildParentLnk extends CrudRepository<ChildParentLnk, String> {

        public List<ChildParentLnk> findAllByParent(String x);
    }
    public interface RepositoryChildParentRecursiveLnk extends CrudRepository<ChildParentRecursiveLnk, String> {

        public List<ChildParentRecursiveLnk> findAllByParent(String x);
        public void deleteByParent(String x);
    }


    @Test
    public void given_parent_with_no_child_then_add_2_childs_and_updateReference_then_call_save_twice() throws Exception {

        EdgeUtils<ChildParentRecursiveLnk> childParentRecursiveLnk = EdgeUtils.of(ChildParentRecursiveLnk.class);
        RepositoryChildParentRecursiveLnk repository = getRepositoryChildParentRecursiveLnk();

        ExampleParentDb parent = getExampleParentDb("parent-1");
        ExampleParentDb child = getExampleParentDb("child-0");

        mockExistingChild(parent, ImmutableList.of(), repository);

        parent.setRecursive(child);

        childParentRecursiveLnk.updateReference(parent, "recursive", repository);

        verifySave(repository, parent, child, 1);
        verifyDelete(repository, parent, child, 0);
    }

    @Test
    public void given_parent_with_no_child_then_add_child_with_status_deleted_and_updateReference_then_no_call_save() throws Exception {

        EdgeUtils<ChildParentRecursiveLnk> childParentRecursiveLnk = EdgeUtils.of(ChildParentRecursiveLnk.class);
        RepositoryChildParentRecursiveLnk repository = getRepositoryChildParentRecursiveLnk();

        ExampleParentDb parent = getExampleParentDb("parent-1");
        ExampleParentDb child = getExampleParentDb("child-0");
        child.setStatus(Resource.StatusEnum.DELETED);

        mockExistingChild(parent, ImmutableList.of(), repository);

        parent.setRecursive(child);

        childParentRecursiveLnk.updateReference(parent, "recursive", repository);

        verifySave(repository, parent, child, 0);
        verifyDelete(repository, parent, child, 0);
    }

    @Test
    public void given_parent_with_no_child_then_add_2_childs_and_update_then_call_save_twice() throws Exception {

        edgeUtils = EdgeUtils.of(ChildParentLnk.class);
        RepositoryChildParentLnk repository = getRepositoryChildParentLnk();

        ExampleParentDb parent = getExampleParentDb("parent-1");
        List<ExampleChildDb> childs = ImmutableList.of(getExampleChildDb("child-0"), getExampleChildDb("child-1"));

        mockExistingChild(parent, ImmutableList.of(), repository);
        
        edgeUtils.updateList(parent, childs, repository);


        verifySave(repository, parent, childs.get(0), 1);
        verifySave(repository, parent, childs.get(1), 1);

        verifyDelete(repository, parent, childs.get(0), 0);
        verifyDelete(repository, parent, childs.get(1), 0);
    }

    @Test
    public void given_parent_with_existing_child_then_add_1_childs_and_update_then_call_save_once() throws Exception {

        edgeUtils = EdgeUtils.of(ChildParentLnk.class);
        RepositoryChildParentLnk repository = getRepositoryChildParentLnk();

        ExampleParentDb parent = getExampleParentDb("parent-1");
        List<ExampleChildDb> childsExisting = ImmutableList.of(getExampleChildDb("child-0"));
        List<ExampleChildDb> childs = ImmutableList.of(getExampleChildDb("child-0"), getExampleChildDb("child-1"));

        mockExistingChild(parent, childsExisting, repository);

        edgeUtils.updateList(parent, childs, repository);


        verifySave(repository, parent, childs.get(0), 0);
        verifySave(repository, parent, childs.get(1), 1);

        verifyDelete(repository, parent, childs.get(0), 0);
        verifyDelete(repository, parent, childs.get(1), 0);
    }

    @Test
    public void given_parent_with_existing_child_then_child_is_deleted_and_update_then_call_delete_once() throws Exception {

        edgeUtils = EdgeUtils.of(ChildParentLnk.class);
        RepositoryChildParentLnk repository = getRepositoryChildParentLnk();

        ExampleParentDb parent = getExampleParentDb("parent-1");
        List<ExampleChildDb> childsExisting = ImmutableList.of(getExampleChildDb("child-0"));
        List<ExampleChildDb> childs = ImmutableList.of(getExampleChildDb("child-0"), getExampleChildDb("child-1"));
        mockExistingChild(parent, childsExisting, repository);

        childs.get(0).setStatus(Resource.StatusEnum.DELETED);

        edgeUtils.updateList(parent, childs, repository);


        verifySave(repository, parent, childs.get(0), 0);
        verifySave(repository, parent, childs.get(1), 1);

        verifyDelete(repository, parent, childs.get(0), 1);
        verifyDelete(repository, parent, childs.get(1), 0);
    }

    @Test
    public void given_parent_with_existing_child_then_new_child_is_deleted_and_update_then_no_change() throws Exception {

        edgeUtils = EdgeUtils.of(ChildParentLnk.class);
        RepositoryChildParentLnk repository = getRepositoryChildParentLnk();

        ExampleParentDb parent = getExampleParentDb("parent-1");
        List<ExampleChildDb> childsExisting = ImmutableList.of(getExampleChildDb("child-0"));
        List<ExampleChildDb> childs = ImmutableList.of(getExampleChildDb("child-0"), getExampleChildDb("child-1"));
        mockExistingChild(parent, childsExisting, repository);

        childs.get(1).setStatus(Resource.StatusEnum.DELETED);

        edgeUtils.updateList(parent, childs, repository);


        verifySave(repository, parent, childs.get(0), 0);
        verifySave(repository, parent, childs.get(1), 0);

        verifyDelete(repository, parent, childs.get(0), 0);
        verifyDelete(repository, parent, childs.get(1), 0);
    }

    @Test
    public void given_parent_with_existing_2_childs_then_remove_1_child_and_update_then_call_delete_once() throws Exception {

        edgeUtils = EdgeUtils.of(ChildParentLnk.class);
        RepositoryChildParentLnk repository = getRepositoryChildParentLnk();

        ExampleParentDb parent = getExampleParentDb("parent-1");
        List<ExampleChildDb> childsExisting = ImmutableList.of(getExampleChildDb("child-0"), getExampleChildDb("child-1"));
        List<ExampleChildDb> childs = ImmutableList.of(getExampleChildDb("child-0"));

        mockExistingChild(parent, childsExisting, repository);

        edgeUtils.updateList(parent, childs, repository);


        verifySave(repository, parent, childsExisting.get(0), 0);
        verifySave(repository, parent, childsExisting.get(1), 0);

        verifyDelete(repository, parent, childsExisting.get(0), 0);
        verifyDelete(repository, parent, childsExisting.get(1), 1);
    }

    @Test
    public void given_parent_with_existing_2_childs_then_add_2_child_and_remove_2_child_and_update_then_call_delete_twice_and_add_twice() throws Exception {

        edgeUtils = EdgeUtils.of(ChildParentLnk.class);
        RepositoryChildParentLnk repository = getRepositoryChildParentLnk();

        ExampleParentDb parent = getExampleParentDb("parent-1");
        List<ExampleChildDb> childsExisting = ImmutableList.of(getExampleChildDb("child-0"), getExampleChildDb("child-1"));
        List<ExampleChildDb> childs = ImmutableList.of(getExampleChildDb("child-2"), getExampleChildDb("child-3"));

        mockExistingChild(parent, childsExisting, repository);

        edgeUtils.updateList(parent, childs, repository);


        verifySave(repository, parent, childs.get(0), 1);
        verifySave(repository, parent, childs.get(1), 1);

        verifyDelete(repository, parent, childsExisting.get(0), 1);
        verifyDelete(repository, parent, childsExisting.get(1), 1);
    }


    private void verifySave(RepositoryChildParentLnk repository, ExampleParentDb parent, ExampleChildDb child, int times) {
        Mockito.verify(repository, Mockito.times(times)).save(Mockito.argThat(lnk -> lnk.parent.getId().equals(parent.getId()) && lnk.child.getId().equals(child.getId())));
    }
    private void verifyDelete(RepositoryChildParentLnk repository, ExampleParentDb parent, ExampleChildDb child, int times) {
        Mockito.verify(repository, Mockito.times(times)).delete(Mockito.argThat(lnk -> lnk.parent.getId().equals(parent.getId()) && lnk.child.getId().equals(child.getId())));
    }

    private void verifySave(RepositoryChildParentRecursiveLnk repository, ExampleParentDb parent, ExampleParentDb child, int times) {
        Mockito.verify(repository, Mockito.times(times)).save(Mockito.argThat(lnk -> lnk.parent.getId().equals(parent.getId()) && lnk.child.getId().equals(child.getId())));
    }
    private void verifyDelete(RepositoryChildParentRecursiveLnk repository, ExampleParentDb parent, ExampleParentDb child, int times) {
        Mockito.verify(repository, Mockito.times(times)).delete(Mockito.argThat(lnk -> lnk.parent.getId().equals(parent.getId()) && lnk.child.getId().equals(child.getId())));
    }

    private void mockExistingChild(ExampleParentDb parent, List<ExampleChildDb> childs, RepositoryChildParentLnk repositoryChildParentLnk) {
        Mockito.when(repositoryChildParentLnk.findAllByParent(parent.toArangoIdentifier())).thenReturn(childs.stream().map(child -> new ChildParentLnk(parent, child)).collect(Collectors.toList()));
    }

    private void mockExistingChild(ExampleParentDb parent, List<ExampleParentDb> childs, RepositoryChildParentRecursiveLnk repositoryChildParentRecursiveLnk) {
        Mockito.when(repositoryChildParentRecursiveLnk.findAllByParent(parent.toArangoIdentifier())).thenReturn(childs.stream().map(child -> new ChildParentRecursiveLnk(parent, child)).collect(Collectors.toList()));
    }



    private ExampleParentDb getExampleParentDb(String id) {
        return new ExampleParentDb(id);
    }

    private ExampleChildDb getExampleChildDb(String id) {
        return new ExampleChildDb(id);
    }

    private RepositoryChildParentLnk getRepositoryChildParentLnk() {
        return Mockito.mock(RepositoryChildParentLnk.class);
    }

    private RepositoryChildParentRecursiveLnk getRepositoryChildParentRecursiveLnk() {
        return Mockito.mock(RepositoryChildParentRecursiveLnk.class);
    }


}
