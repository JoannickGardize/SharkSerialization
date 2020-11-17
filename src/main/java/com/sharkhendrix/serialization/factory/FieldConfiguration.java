package com.sharkhendrix.serialization.factory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.NullType;

import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.annotation.ConcreteType;
import com.sharkhendrix.serialization.annotation.ElementsConfiguration;
import com.sharkhendrix.serialization.annotation.ElementsConfigurationGroup;
import com.sharkhendrix.serialization.annotation.SharedReference;
import com.sharkhendrix.serialization.annotation.UndefinedType;

public class FieldConfiguration {

    private Class<?> type = null;

    private boolean sharedReference = false;
    private boolean undefinedType = false;

    private FieldConfiguration next = null;

    private FieldConfiguration() {
    }

    public FieldConfiguration(Field field) {
        ConcreteType concreteType = field.getAnnotation(ConcreteType.class);
        if (concreteType != null) {
            type = concreteType.value();
        } else {
            type = field.getType();
        }
        sharedReference = field.isAnnotationPresent(SharedReference.class);
        undefinedType = field.isAnnotationPresent(UndefinedType.class);
    }

    public static FieldConfiguration forArray(Field field) {
        return forArrayOrCollection(field, getDeclaredTypesForArray(field));
    }

    public static FieldConfiguration forCollection(Field field) {
        return forArrayOrCollection(field, getDeclaredTypesForCollection(field));
    }

    public static FieldConfiguration forArrayOrCollection(Field field, Class<?>[] declaredTypes) {
        FieldConfiguration configuration = new FieldConfiguration(field);
        ElementsConfiguration[] elementsConfigurations = getElementsConfigurations(field);
        FieldConfiguration currentNode = configuration;
        for (int i = 0; i < declaredTypes.length || i < elementsConfigurations.length; i++) {
            FieldConfiguration nextNode = new FieldConfiguration();
            if (i < declaredTypes.length) {
                nextNode.type = declaredTypes[i];
            }
            if (i < elementsConfigurations.length) {
                ElementsConfiguration elementsConfiguration = elementsConfigurations[i];
                nextNode.sharedReference = elementsConfiguration.sharedReference();
                nextNode.undefinedType = elementsConfiguration.undefinedType();
                if (elementsConfiguration.type() != NullType.class) {
                    nextNode.type = elementsConfiguration.type();
                }
            }
            if (nextNode.type == null && nextNode.undefinedType == false) {
                throw new SharkSerializationException("No defined type for a sub-element the field " + field);
            }
            currentNode.next = nextNode;
            currentNode = nextNode;
        }
        return configuration;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isSharedReference() {
        return sharedReference;
    }

    public boolean isUndefinedType() {
        return undefinedType;
    }

    public FieldConfiguration next() {
        return next;
    }

    public FieldConfiguration getNext() {
        return next;
    }

    public void setNext(FieldConfiguration next) {
        this.next = next;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setSharedReference(boolean sharedReference) {
        this.sharedReference = sharedReference;
    }

    public void setUndefinedType(boolean undefinedType) {
        this.undefinedType = undefinedType;
    }

    private static ElementsConfiguration[] getElementsConfigurations(Field field) {
        ElementsConfigurationGroup elementsConfigurationGroup = field.getAnnotation(ElementsConfigurationGroup.class);
        if (elementsConfigurationGroup != null) {
            return elementsConfigurationGroup.value();
        }
        ElementsConfiguration elementsConfiguration = field.getAnnotation(ElementsConfiguration.class);
        if (elementsConfiguration != null) {
            return new ElementsConfiguration[] { elementsConfiguration };
        }
        return new ElementsConfiguration[0];
    }

    private static Class<?>[] getDeclaredTypesForArray(Field field) {
        List<Class<?>> types = new ArrayList<>();
        Class<?> type = field.getType().getComponentType();
        while (type != null) {
            types.add(type);
            type = type.getComponentType();
        }
        return types.toArray(Class<?>[]::new);
    }

    private static Class<?>[] getDeclaredTypesForCollection(Field field) {
        List<Class<?>> types = new ArrayList<>();
        Type type = field.getGenericType();
        while (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (type instanceof Class) {
                types.add((Class<?>) type);
            } else {
                types.add((Class<?>) ((ParameterizedType) type).getRawType());
            }
        }
        return types.toArray(Class<?>[]::new);
    }
}
