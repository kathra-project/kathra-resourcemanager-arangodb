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

package org.kathra.resourcemanager.group.dao;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import org.kathra.core.model.Group;
import org.kathra.core.model.Group.*;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import java.util.List;

import org.kathra.resourcemanager.assignation.dao.AssignationDb;
import org.kathra.core.model.Resource.*;
import org.kathra.resourcemanager.group.dao.GroupAssignationEdge;
import org.kathra.resourcemanager.group.dao.GroupGroupEdge;


/**
 * Entity class GroupDb implementing db resource for class Group
 *
 * Auto-generated by resource-db-generator@1.1.1 at 2019-04-12T13:50:55.986Z
 * @author julien.boubechtoula
 */
@Document("Groups")
public class GroupDb extends AbstractResourceDb<Group> {

		private String path; 
		@Relations(edges = GroupAssignationEdge.class, lazy = true) 
		private List<AssignationDb> members; 
		@Relations(edges = GroupGroupEdge.class, lazy = true) 
		private GroupDb parent; 
		private SourceRepositoryStatusEnum sourceRepositoryStatus; 
		private SourceMembershipStatusEnum sourceMembershipStatus; 
		private PipelineFolderStatusEnum pipelineFolderStatus; 
		private BinaryRepositoryStatusEnum binaryRepositoryStatus; 


        public GroupDb() {
        }
        public GroupDb(String id) {
            super(id);
        }

		public String getPath() { return this.path;}
		public void setPath(String path) { this.path=path;}

		public List<AssignationDb> getMembers() { return this.members;}
		public void setMembers(List<AssignationDb> members) { this.members=members;}

		public GroupDb getParent() { return this.parent;}
		public void setParent(GroupDb parent) { this.parent=parent;}

		public SourceRepositoryStatusEnum getSourceRepositoryStatus() { return this.sourceRepositoryStatus;}
		public void setSourceRepositoryStatus(SourceRepositoryStatusEnum sourceRepositoryStatus) { this.sourceRepositoryStatus=sourceRepositoryStatus;}

		public SourceMembershipStatusEnum getSourceMembershipStatus() { return this.sourceMembershipStatus;}
		public void setSourceMembershipStatus(SourceMembershipStatusEnum sourceMembershipStatus) { this.sourceMembershipStatus=sourceMembershipStatus;}

		public PipelineFolderStatusEnum getPipelineFolderStatus() { return this.pipelineFolderStatus;}
		public void setPipelineFolderStatus(PipelineFolderStatusEnum pipelineFolderStatus) { this.pipelineFolderStatus=pipelineFolderStatus;}

		public BinaryRepositoryStatusEnum getBinaryRepositoryStatus() { return this.binaryRepositoryStatus;}
		public void setBinaryRepositoryStatus(BinaryRepositoryStatusEnum binaryRepositoryStatus) { this.binaryRepositoryStatus=binaryRepositoryStatus;}



}