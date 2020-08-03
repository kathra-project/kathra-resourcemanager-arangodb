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

import com.google.common.collect.ImmutableList;
import org.kathra.core.model.Resource;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

/**
 * Lean Resource instance to export
 * @author julien.boubechtoula
 *
 */
public class LeanResourceUtils<X extends Resource> {

    public static ImmutableList<String> propertiesExcluded = ImmutableList.of("id", "class", "name");

    public static <T> LeanResourceUtils of(Class<? extends T> clazz) {
        try {
            return LeanResourceUtils.class.getConstructor(clazz).newInstance();
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
    /**
     * Lean instance
     * @param object
     * @return
     */
    public X leanObject(X object) {
        try {
            for(PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()){
                if (propertyDescriptor.getReadMethod() == null) {
                    throw new Exception("Unable to find getter for property " + propertyDescriptor.getName() + " into class " + object.getClass().getName());
                }
                Object propertyObject = propertyDescriptor.getReadMethod().invoke(object);
                if (propertyObject == null)
                    continue;
                if (Resource.class.isAssignableFrom(propertyObject.getClass())) {
                    cleanExceptObject((Resource) propertyObject);
                } else if (Iterable.class.isAssignableFrom(propertyObject.getClass())) {
                    Iterator it = ((Iterable) propertyObject).iterator();
                    while (it.hasNext()) {
                        Object item = it.next();
                        if (item == null) {
                            it.remove();
                        } else if (item instanceof Resource) {
                            cleanExceptObject((Resource) item);
                        }
                    }
                } else if (Map.class.isAssignableFrom(propertyObject.getClass())) {
                    for(Object entry : ((Map) propertyObject).entrySet()){
                        if (entry instanceof Map.Entry){
                            if (((Map.Entry)entry).getValue() == null) {
                                ((Map) propertyObject).remove(((Map.Entry)entry).getKey());
                            } else if(((Map.Entry)entry).getValue() instanceof Resource){
                                cleanExceptObject((Resource) ((Map.Entry)entry).getValue());
                            }
                            if(((Map.Entry)entry).getKey() instanceof Resource){
                                cleanExceptObject((Resource) ((Map.Entry)entry).getKey());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to lean object.", e);
        }

        return object;
    }

    /**
     * Set to null all properties of resource instance except identifier
     * @param object
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     */
    private void cleanExceptObject(Resource object) {
        try {
            for(PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()){
                if (propertiesExcluded.contains(propertyDescriptor.getName()))
                    continue;
                else
                    propertyDescriptor.getWriteMethod().invoke(object, new Object[]{null});
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to lean object.", e);
        }
    }
}
