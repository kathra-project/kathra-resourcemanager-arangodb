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
package org.kathra.resourcemanager.resource.converter;

import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.resource.dao.IResourceDb;
import org.kathra.resourcemanager.resource.utils.LeanResourceDbUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ConverterResourceToResourceDb<X extends Resource,V extends IResourceDb> {

    default V convertResourceToResourceDb(X object) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(object, ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }

    default X convertResourceDbToResource(V object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(leanResourceDbUtils.leanResourceDb(object), ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    default List<X> convertResourceDbToResource(List<V> objects){
        return convertResourceDbToResource(objects.stream()).collect(Collectors.toList());
    }

    default Stream<X> convertResourceDbToResource(Stream<V> objectsStream){
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<V> lean = objectsStream.map(i -> (V) leanUtils.leanResourceDb(i)).collect(Collectors.toList());
        return lean.parallelStream().map(i -> modelMapper.map(i, type));
    }

}
