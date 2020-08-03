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
package org.kathra.resourcemanager;

import com.google.common.collect.ImmutableList;
import org.kathra.core.model.Resource.StatusEnum;
import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import org.kathra.resourcemanager.resource.dao.IResourceDb;
import org.junit.jupiter.api.Assertions;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class MappingTestUtils {

    public <X extends Resource,Y extends AbstractResourceDb> void testMappingClasses(Class<X> object, Class<Y> objectDb, AbstractResourceDao<X,Y,?> dao) throws Exception {

        testResourceToResourceDB(object, objectDb, dao);
        testResourceDbToResource(objectDb, object, dao);
    }

    private <X extends Resource,Y extends AbstractResourceDb>  void testResourceToResourceDB(Class<X> from, Class<Y> to, AbstractResourceDao<X,Y,?> dao) throws Exception {

        X object = from.getConstructor().newInstance();
        object.setId("azerty");
        object.setUpdatedAt((int) (System.currentTimeMillis()/1000));
        object.setCreatedAt((int) (System.currentTimeMillis()/1000) - 5000);
        object.setStatus(StatusEnum.READY);
        object.setName("a name");
        object.setCreatedBy("Mr X");
        object.setUpdatedBy("Mr Y");
        object.setMetadata(new HashMap<>());
        object.getMetadata().put("ajcdhfgf","dsqpdq$");
        generateExtraProperty(object);

        Y objectDb = dao.convertResourceToResourceDb(object);
        X objectReconverted = dao.convertResourceDbToResource(objectDb);
        checkEquals(object, objectReconverted);
    }

    private <X extends AbstractResourceDb,Y extends Resource>  void testResourceDbToResource(Class<X> from, Class<Y> to, AbstractResourceDao<Y,X,?> dao) throws Exception {

        X objectDb = from.getConstructor().newInstance();
        objectDb.setId("azerty");
        objectDb.setUpdatedAt((int) (System.currentTimeMillis()/1000));
        objectDb.setCreatedAt((int) (System.currentTimeMillis()/1000) - 5000);
        objectDb.setStatus(StatusEnum.READY);
        objectDb.setName("a name");
        objectDb.setCreatedBy("Mr X");
        objectDb.setUpdatedBy("Mr Y");
        objectDb.setMetadata(new HashMap<>());
        objectDb.getMetadata().put("ajcdhfgf","dsqpdq$");
        generateExtraProperty(objectDb);

        Y object = dao.convertResourceDbToResource(objectDb);
        X objectDbReconverted = dao.convertResourceToResourceDb(object);
        checkEquals(objectDb, objectDbReconverted);
    }

    private <X> void checkEquals(X objectA, X objectB) throws Exception {
        for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(objectA.getClass()).getPropertyDescriptors()) {
            Object propertyValueA = propertyDescriptor.getReadMethod().invoke(objectA);
            Object propertyValueB = propertyDescriptor.getReadMethod().invoke(objectB);
            if (propertyValueA == null && propertyValueB == null) {
                continue;
            }
            if (Resource.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                Assertions.assertEquals(((Resource) propertyValueA).getId(), ((Resource) propertyValueB).getId());
            } else if (Iterable.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                Iterable iterableA = (Iterable) propertyValueA;
                Iterable iterableB = (Iterable) propertyValueB;
                Iterator iteratorA = iterableA.iterator();
                Iterator iteratorB = iterableB.iterator();

                while (iteratorA.hasNext()) {
                    Object itemA = iteratorA.next();
                    Object itemB = iteratorB.next();
                    if (itemA instanceof Resource) {
                        Assertions.assertEquals(((Resource) itemA).getId(), ((Resource) itemB).getId());
                    } else {
                        Assertions.assertEquals(itemA, itemB);
                    }
                }
            } else {
                Assertions.assertEquals(propertyValueA, propertyValueB);
            }
        }
    }

    private void generateExtraProperty(Object object) throws Exception {
        ImmutableList<String> propertiesExcluded = ImmutableList.of("id", "class", "name");

        for(PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()){
            if (propertiesExcluded.contains(propertyDescriptor.getName()))
                continue;
            Object propertyValue = propertyDescriptor.getReadMethod().invoke(object);
            if (propertyValue != null) //already setted
                continue;

            if (propertyDescriptor.getWriteMethod() == null){
                throw new Exception("Unable to find write method for property '"+propertyDescriptor.getName()+"' for class '"+object.getClass().getName()+"'");
            }

            if (IResourceDb.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                propertyValue = getIResourceDbInstance(propertyDescriptor.getPropertyType());
            } else if (Resource.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                propertyValue = getResourceInstance(propertyDescriptor.getPropertyType());
            } else if (Iterable.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                Method method = propertyDescriptor.getReadMethod();
                ParameterizedType genericReturnType = (ParameterizedType)method.getGenericReturnType();
                Type[] actualTypeArguments = genericReturnType.getActualTypeArguments();
                if (actualTypeArguments.length == 0)
                    continue;
                if (IResourceDb.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                    propertyValue = ImmutableList.of(getIResourceDbInstance(propertyDescriptor.getPropertyType()));
                } else if (Resource.class.isAssignableFrom((Class)actualTypeArguments[0])) {
                    propertyValue = ImmutableList.of(getResourceInstance((Class)actualTypeArguments[0]));
                }
            } else if (String.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                propertyDescriptor.getWriteMethod().invoke(object, UUID.randomUUID().toString());
            } else if (Long.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                propertyDescriptor.getWriteMethod().invoke(object, Long.valueOf((long) Math.random()));
            } else if (Double.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                propertyDescriptor.getWriteMethod().invoke(object, Double.valueOf((double) Math.random()));
            } else if (Integer.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                propertyDescriptor.getWriteMethod().invoke(object, Integer.valueOf((int) Math.random()));
            } else if (Float.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                propertyDescriptor.getWriteMethod().invoke(object, Float.valueOf((float) Math.random()));
            } else if (Boolean.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                propertyDescriptor.getWriteMethod().invoke(object, true);
            } else if (propertyDescriptor.getPropertyType().isEnum()) {
                propertyValue = propertyDescriptor.getPropertyType().getEnumConstants()[0];
            }

            if (propertyValue != null) {
                propertyDescriptor.getWriteMethod().invoke(object, propertyValue);
            }
        }
    }

    private IResourceDb getIResourceDbInstance(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        IResourceDb resource = (IResourceDb) clazz.getConstructor().newInstance();
        resource.setId(UUID.randomUUID().toString());
        return resource;
    }

    private Resource getResourceInstance(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Resource resource = (Resource) clazz.getConstructor().newInstance();
        resource.setId(UUID.randomUUID().toString());
        return resource;
    }
}
