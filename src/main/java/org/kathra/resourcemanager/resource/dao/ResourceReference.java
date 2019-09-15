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

import com.arangodb.springframework.annotation.Document;
import org.kathra.core.model.Resource;
import org.springframework.data.annotation.Id;

@Document("Resources")
public class ResourceReference {

    @Id
    public String uuid;
    public String type;
    public String name;
    public Resource.StatusEnum status;

    public String createdBy;
    public long createdAt;
    public String updatedBy;
    public long updatedAt;


    public ResourceReference(){

    }

    public ResourceReference(String uuid){
        this.uuid = uuid;
    }
}