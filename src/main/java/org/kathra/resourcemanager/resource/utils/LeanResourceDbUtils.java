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
package org.kathra.resourcemanager.resource.utils;

import com.google.common.collect.ImmutableList;
import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.resource.dao.IResourceDb;
import org.apache.commons.lang3.StringUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lean Resource instance to export
 * @author julien.boubechtoula
 *
 */
public class LeanResourceDbUtils<X extends IResourceDb> {

    private int deepLevel = 1;
    private ConcurrentHashMap<String,IResourceDb> objectsIdentified = new ConcurrentHashMap();
    private static ConcurrentHashMap<Class,PropertyDescriptor[]> propertyDescriptors = new ConcurrentHashMap();
    private static ConcurrentHashMap<Class,Constructor> constructors = new ConcurrentHashMap();

    public static ImmutableList<String> propertiesExcluded = ImmutableList.of("id", "class", "name");

    public static <T> LeanResourceDbUtils of(Class<? extends T> clazz) {
        try {
            return LeanResourceDbUtils.class.getConstructor(clazz).newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public X leanResourceDb(X object) {
        return (X) leanResourceDb(object, deepLevel);
    }

    /**
     * Lean instance
     * @param object
     * @return
     */
    private IResourceDb leanResourceDb(IResourceDb object, int level) {
        try {

            if (level == 0)
                return getSimpleResourceDb(object);
            int currentLevel = level - 1;
            PropertyDescriptor[] propertiesDescriptors = propertyDescriptors.get(object.getClass());
            if (propertiesDescriptors == null) {
                propertiesDescriptors = Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors();
                propertyDescriptors.put(object.getClass(), propertiesDescriptors);
            }

            for(PropertyDescriptor propertyDescriptor : propertiesDescriptors){
                if (propertyDescriptor.getReadMethod() == null) {
                    throw new Exception("Unable to find getter for property " + propertyDescriptor.getName() + " into class " + object.getClass().getName());
                }
                Object propertyObject = propertyDescriptor.getReadMethod().invoke(object);
                if (propertyObject == null)
                    continue;

                if (IResourceDb.class.isAssignableFrom(propertyObject.getClass())) {
                    propertyDescriptor.getWriteMethod().invoke(object, leanResourceDb((IResourceDb) propertyObject, currentLevel));
                } else if (List.class.isAssignableFrom(propertyObject.getClass())) {
                    List list = ((List) propertyObject);
                    ListIterator listIterator = list.listIterator();
                    while (listIterator.hasNext()) {
                        Object item = listIterator.next();
                        if (item instanceof IResourceDb) {
                            listIterator.set(leanResourceDb((IResourceDb) item, currentLevel));
                        }
                    }
                } else if (Map.class.isAssignableFrom(propertyObject.getClass())) {
                    for(Object entry : ((Map) propertyObject).entrySet()){
                        if (entry instanceof Map.Entry){
                            if(((Map.Entry)entry).getValue() instanceof IResourceDb){
                                ((Map.Entry)entry).setValue(leanResourceDb((IResourceDb) ((Map.Entry)entry).getValue(), currentLevel));
                            }
                            if(((Map.Entry)entry).getKey() instanceof IResourceDb){
                                IResourceDb newKey = leanResourceDb((IResourceDb) ((Map.Entry) entry).getKey(), currentLevel);
                                ((Map) propertyObject).remove(((Map.Entry)entry).getKey());
                                ((Map) propertyObject).put(newKey, ((Map.Entry)entry).getValue());
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {

        }
        return object;
    }

    private IResourceDb getSimpleResourceDb(IResourceDb object) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String resourceUuid = (String) object.getId();
        if (StringUtils.isEmpty(resourceUuid)) {
            return null;
        }
        IResourceDb objectFound = objectsIdentified.get(resourceUuid);
        if (objectFound != null) {
            return objectFound;
        }

        Constructor constructor = constructors.get(object.getClass());
        if (constructor == null) {
            constructor = object.getClass().getConstructor();
            constructors.put(object.getClass(), constructor);
        }
        IResourceDb newO = (IResourceDb) constructor.newInstance();
        newO.setId(resourceUuid);
        objectsIdentified.put(resourceUuid, newO);
        return newO;
    }
}
