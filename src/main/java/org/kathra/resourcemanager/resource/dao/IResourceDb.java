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

import org.kathra.core.model.Resource.StatusEnum;
import org.kathra.core.model.Resource;

import java.util.Map;

public interface IResourceDb<ID,X extends Resource> {
    public ID getId();
    public void setId(ID identifier);
    public String getName();
    public String getCreatedBy();
    public String getUpdatedBy();
    public StatusEnum getStatus();
    public long getUpdatedAt();
    public long getCreatedAt();

    public void setCreatedBy(String author);
    public void setUpdatedBy(String author);
    public void setStatus(StatusEnum state);
    public void setUpdatedAt(long timestamp);
    public void setCreatedAt(long timestamp);

    public Map<String, Object> getMetadata();

}
