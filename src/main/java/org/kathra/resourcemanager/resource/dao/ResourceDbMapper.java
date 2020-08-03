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
package org.kathra.resourcemanager.resource.dao;

import java.util.Map;

public interface ResourceDbMapper {

    final class CustomCreatedAt {
        public Integer map(long createdAt){
            return createdAt == 0 ? null : Long.valueOf(createdAt).intValue();
        }
        public long map(Integer createdAt){
            return createdAt == null ? 0 : Long.valueOf(createdAt).intValue();
        }
    }

    final class CustomUpdatedAt {
        public Integer map(long updatedAt){
            return updatedAt == 0 ? null : Long.valueOf(updatedAt).intValue();
        }
        public long map(Integer updatedAt){
            return updatedAt == null ? 0 : Long.valueOf(updatedAt).intValue();
        }
    }
    final class CustomMetadata {
        public Map<String,Object> map(Map<String,Object> metadata){
            return metadata;
        }
    }
}
