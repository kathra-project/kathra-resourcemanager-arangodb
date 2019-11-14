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

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import com.google.common.collect.ImmutableList;
import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import org.kathra.resourcemanager.resource.dao.IResourceDb;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * EdgeUtils
 * Tools permitting to update list with edge into repository
 *
 * @author julien.boubechtoula
 *
 * @param <X>
 */
public class EdgeUtils<X> {

    private enum Action {
        ADD,UPDATE,DELETE
    }

    private static final String PREFIX_METHOD_REPOSITORY = "findAllBy";
    private static Logger logger = Logger.getLogger(EdgeUtils.class);

    private final Class<X> typeParameterClass;
    private final PropertyDescriptor[] propertiesDescriptors;

    public EdgeUtils(Class<X> edgeClass) {
        this.typeParameterClass = edgeClass;
        try {
            propertiesDescriptors = Introspector.getBeanInfo(typeParameterClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to init edge utils for class " + typeParameterClass.getName());
        }
        checkClass(edgeClass);
    }

    private void checkClass(Class<X> edgeClass) {
        if (edgeClass.getAnnotation(Edge.class) == null) {
            throw new IllegalArgumentException("Class should annoted with "+Edge.class.getName()+".");
        }
        try {
            getEdgeAttributeFromAnnotation(From.class);
            getEdgeAttributeFromAnnotation(To.class);
        } catch(Exception e){
            throw new IllegalArgumentException("Class should have attributes annoted with "+From.class.getName()+" and "+To.class.getName()+".");
        }
    }

    public static <U> EdgeUtils<U> of(Class<U> edgeClass){
        return new EdgeUtils<>(edgeClass);
    }


    public void updateReference(AbstractResourceDb resourceDb, String propertyName, CrudRepository<X,?> repository) throws Exception {

        try {

            PropertyDescriptor property = Arrays.stream(Introspector.getBeanInfo(resourceDb.getClass()).getPropertyDescriptors()).filter(propertyDescriptor -> propertyDescriptor.getName().equals(propertyName)).findFirst().get();
            AbstractResourceDb linkedObject = (AbstractResourceDb) property.getReadMethod().invoke(resourceDb);

            Field attributeParent = null;
            Field attributeChild = null;
            if (resourceDb.getClass().equals(property.getPropertyType())){
                attributeParent = getEdgeAttribute(resourceDb.getClass(), "parent");
                attributeChild = getEdgeAttribute(property.getPropertyType(), "child");
            } else {
                attributeParent = getEdgeAttribute(resourceDb.getClass());
                attributeChild = getEdgeAttribute(property.getPropertyType());
            }


            boolean linkIsBroken = Resource.StatusEnum.DELETED.equals(resourceDb.getStatus()) || (linkedObject != null && Resource.StatusEnum.DELETED.equals(linkedObject.getStatus()));

            if (linkedObject != null && linkedObject.getId() != null && !linkIsBroken) {
                Method methodFindAllByResourceDb = repository.getClass().getMethod("findAllBy" + StringUtils.capitalize(attributeParent.getName()), String.class);
                List<X> links = (List<X>) methodFindAllByResourceDb.invoke(repository, resourceDb.toArangoIdentifier());
                boolean linkExist = false;
                if (!links.isEmpty()) {
                     PropertyDescriptor linkedObjectProperty = Arrays   .stream(Introspector.getBeanInfo(links.get(0).getClass())
                                                                        .getPropertyDescriptors())
                                                                        .filter(propertyDescriptor -> propertyDescriptor.getPropertyType().equals(linkedObject.getClass()))
                                                                        .findFirst()
                                                                        .get();
                    for(X link:links) {
                        if (!((AbstractResourceDb) linkedObjectProperty.getReadMethod().invoke(link)).getId().equals(linkedObject.getId())){
                            repository.delete(link);
                        } else {
                            linkExist = true;
                        }
                    }
                }
                if (!linkExist) {
                    X edge = getNewInstanceOfEdge(resourceDb, attributeParent, attributeChild, linkedObject);
                    repository.save(edge);
                }
            } else {
                Method methodDeleteByResourceDb = repository.getClass().getMethod("deleteBy" + StringUtils.capitalize(attributeParent.getName()), String.class);
                methodDeleteByResourceDb.invoke(repository, resourceDb.toArangoIdentifier());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to update reference");
        }
    }

    private Class<? extends Resource> getResourceClassFromResourceDb(IResourceDb resourceDb) {
        return (Class<? extends Resource>) ((ParameterizedType) resourceDb.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Update link of resource with list
     *
     * @param resourceDb
     * @param itemsToUpdate
     * @param repository
     */
    public void updateList(AbstractResourceDb resourceDb, List<? extends AbstractResourceDb> itemsToUpdate, CrudRepository<X,?> repository) throws Exception {
        HashMap<Action, List<X>> actionsToDo = actionsToDo(resourceDb, itemsToUpdate, repository);
        actionsToDo.get(Action.ADD).forEach(toAdd -> repository.save(toAdd));
        actionsToDo.get(Action.UPDATE).forEach(toUpdate -> repository.save(toUpdate));
        actionsToDo.get(Action.DELETE).forEach(toDelete -> repository.delete(toDelete));
    }

    private HashMap<Action,List<X>> actionsToDo(AbstractResourceDb resourceDb, List<? extends AbstractResourceDb> resourcesItemsNew, Repository<X,?> repository) throws Exception {

        HashMap<Action,List<X>> actionsToDo = new HashMap<>();

        Field attributeParent = getEdgeAttribute(resourceDb.getClass());
        if (attributeParent.getAnnotation(From.class) == null && attributeParent.getAnnotation(To.class) == null) {
            throw new IllegalStateException("Attribute "+attributeParent.getName()+" class "+typeParameterClass.getClass()+" doesn't have annotation @From or @To");
        }
        Field attributeChild = getEdgeAttributeFromAnnotation((attributeParent.getAnnotation(From.class) != null) ? To.class : From.class );

        List<X> existingEdges = getExistingEdge(resourceDb, repository, attributeParent);

        actionsToDo.put(Action.DELETE, getEdgesToDelete(resourcesItemsNew, attributeChild, existingEdges));
        actionsToDo.put(Action.UPDATE, ImmutableList.of());
        actionsToDo.put(Action.ADD, getEdgesToAdd(resourceDb, resourcesItemsNew, attributeParent, attributeChild, existingEdges));

        if (Resource.StatusEnum.DELETED.equals(resourceDb.getStatus())) {
            actionsToDo.get(Action.DELETE).addAll(actionsToDo.get(Action.ADD));
            actionsToDo.put(Action.ADD, ImmutableList.of());
            actionsToDo.get(Action.DELETE).addAll(actionsToDo.get(Action.UPDATE));
            actionsToDo.put(Action.UPDATE, ImmutableList.of());
        }
        return actionsToDo;
    }

    private List<X> getExistingEdge(AbstractResourceDb object, Repository<X, ?> repository, Field attributeParent) throws Exception {
        String methodNameExcepted = PREFIX_METHOD_REPOSITORY + StringUtils.capitalize(attributeParent.getName());

        try {
            Method methodFindAllByResource = repository.getClass().getMethod(methodNameExcepted, String.class);
            try {
                return (List<X>) methodFindAllByResource.invoke(repository, object.toArangoIdentifier());
            } catch (Exception e) {
                logger.error("Unable to execute method " + methodNameExcepted + "(\"" + object.toArangoIdentifier() + "\") on instance of " + repository.getClass().getName() + ".", e);
                throw new RuntimeException(e);
            }
        } catch(Exception e) {
            throw new IllegalStateException("Unable to find method : " + methodNameExcepted + "(String object) into class " + repository.getClass().getName());
        }
    }

    private List<X> getEdgesToAdd(AbstractResourceDb resourceDb, List<? extends AbstractResourceDb> resourcesItemsNew, Field attributeParent, Field attributeChild, List existingEdges) {
        return resourcesItemsNew.stream()
                    .filter(defined -> !Resource.StatusEnum.DELETED.equals(defined.getStatus()) && existingEdges.stream().noneMatch(existingChild -> {
                        try {
                            return ((AbstractResourceDb) attributeChild.get(existingChild)).getId().equals(defined.getId());
                        } catch (Exception e) {
                            logger.error("Unable to get instance from property "+attributeChild.getName()+" into class "+existingChild.getClass().toString(), e);
                            throw new RuntimeException(e);
                        }
                    }))
                    .map(childToAdd -> getNewInstanceOfEdge(resourceDb, attributeParent, attributeChild, childToAdd))
                    .collect(Collectors.toList());
    }

    private List<X> getEdgesToDelete(List<? extends AbstractResourceDb> resourcesItemsNew, Field attributeChild, List existingEdges) {
        return (List<X>) existingEdges.stream()
                        .filter(existing -> {
                                return resourcesItemsNew.stream().filter(i -> !Resource.StatusEnum.DELETED.equals(i.getStatus())).noneMatch(defined -> {
                                    try {
                                        return defined.getId().equals(((AbstractResourceDb) attributeChild.get(existing)).getId());
                                    } catch (Exception e) {
                                        logger.error("Unable to get instance from property "+attributeChild.getName()+" into class "+existing.getClass().toString(), e);
                                        throw new RuntimeException(e);
                                    }
                                });
                        }).collect(Collectors.toList());
    }


    private PropertyDescriptor getPropertyDescriptorFromField(Field field) {
        return Arrays.stream(propertiesDescriptors).filter(propertyDescriptor ->    propertyDescriptor.getPropertyType().isAssignableFrom(field.getType()) &&
                                                                                    propertyDescriptor.getName().equals(field.getName()))
                                                    .findFirst()
                                                    .orElseThrow(() -> new IllegalArgumentException("Unable to find property descriptor for field "+field.getName()+" into class "+typeParameterClass.getName()));
    }

    /**
     * Create new instance of X and set attribute from constructor (should be defined)
     *
     * @param resourceDb
     * @param attributeParent
     * @param attributeChild
     * @param childToAdd
     * @return
     */
    private X getNewInstanceOfEdge(AbstractResourceDb resourceDb, Field attributeParent, Field attributeChild, AbstractResourceDb childToAdd) {
        try {

            final List<Object> objectsToConstructor = ImmutableList.of(resourceDb, childToAdd);
            PropertyDescriptor propertyParent = getPropertyDescriptorFromField(attributeParent);
            PropertyDescriptor propertyChild = getPropertyDescriptorFromField(attributeChild);

            X edge;

            if (attributeParent.getType().equals(attributeChild.getType())) {
                Constructor<X> defaultConstructor = typeParameterClass.getConstructor();
                edge = defaultConstructor.newInstance();
                propertyParent.getWriteMethod().invoke(edge, resourceDb);
                propertyChild.getWriteMethod().invoke(edge, childToAdd);
            } else {
                Pair<? extends Constructor<?>, HashMap<Integer, Object>> constructorFound = Arrays.stream(typeParameterClass.getConstructors()).map(constructor -> {
                    HashMap<Integer, Object> args = new HashMap<>();
                    int i = 0;
                    for (Class<?> parameterizedType : constructor.getParameterTypes()) {
                        args.put(i, objectsToConstructor.stream()
                                .filter(object -> parameterizedType.equals(object.getClass())).findFirst().orElse(null));
                        i++;
                    }
                    return Pair.of(constructor, args);
                }).filter(pairConstructAndArgs -> pairConstructAndArgs.getValue().values().containsAll(objectsToConstructor))
                        .sorted(Comparator.comparing(pairConstructAndArgs -> pairConstructAndArgs.getValue().size()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unable to find constructor"));

                edge = (X) constructorFound.getKey().newInstance(constructorFound.getValue().values().toArray());
            }
            if (propertyParent.getReadMethod() != null) {
                if (!resourceDb.equals(propertyParent.getReadMethod().invoke(edge))) {
                    throw new IllegalStateException("Property " + resourceDb.getName() + " of class " + edge.getClass().getName() + " is not matching");
                }
            }
            if (propertyChild.getReadMethod() != null) {
                if (!childToAdd.equals(propertyChild.getReadMethod().invoke(edge))) {
                    throw new IllegalStateException("Property " + attributeChild.getName() + " of class " + edge.getClass().getName() + " is not matching");
                }
            }
            return edge;
        } catch (Exception e) {
            logger.error("Unable to create new edge instance of class "+this.typeParameterClass.getName()+
                            ", add constructor taking 2 arguments: "+
                            attributeChild.getType().getName()+" "+attributeChild.getName()+",  "+
                            attributeParent.getType().getName()+" "+attributeParent.getName()+",  ", e);
            throw new RuntimeException(e);
        }
    }

    private Field getEdgeAttribute(Class<?> targetClass) {
        List<Field> fields = Arrays.stream(this.typeParameterClass.getDeclaredFields()).filter(field ->
                field.getType().equals(targetClass) || field.getType().isAssignableFrom(targetClass)).collect(Collectors.toList());

        if (fields.size() == 0) {
            throw new IllegalArgumentException("Unable to find attribute of "+targetClass.getName()+" into class "+this.typeParameterClass.getName());
        } else if (fields.size() > 1) {
            throw new IllegalArgumentException("Unable to find uniq attribute of "+targetClass.getName()+" into class "+this.typeParameterClass.getName());
        } else {
            return fields.get(0);
        }
    }

    private Field getEdgeAttribute(Class<?> targetClass, String nameField) {
        List<Field> fields = Arrays.stream(this.typeParameterClass.getDeclaredFields()).filter(field ->
                field.getType().equals(targetClass) || field.getType().isAssignableFrom(targetClass)).filter(field -> field.getName().equals(nameField)).collect(Collectors.toList());

        if (fields.size() == 0) {
            throw new IllegalArgumentException("Unable to find attribute of "+targetClass.getName()+" into class "+this.typeParameterClass.getName());
        } else if (fields.size() > 1) {
            throw new IllegalArgumentException("Unable to find uniq attribute of "+targetClass.getName()+" into class "+this.typeParameterClass.getName());
        } else {
            return fields.get(0);
        }
    }

    private Field getEdgeAttributeFromAnnotation(Class<? extends Annotation> annotationClass){
        return Arrays.stream(this.typeParameterClass.getDeclaredFields()).filter( field ->
                Arrays.stream(field.getAnnotations())
                        .anyMatch(fieldAnnotation -> fieldAnnotation.annotationType().equals(annotationClass)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unable to find attribute with annotation "+ annotationClass.getName()+" into class "+this.typeParameterClass.getName()));
    }
}