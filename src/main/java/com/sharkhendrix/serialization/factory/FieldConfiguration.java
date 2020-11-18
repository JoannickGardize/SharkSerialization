package com.sharkhendrix.serialization.factory;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.lang.model.type.NullType;

import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.annotation.ConcreteType;
import com.sharkhendrix.serialization.annotation.ElementsConfiguration;
import com.sharkhendrix.serialization.annotation.ElementsConfigurationGroup;
import com.sharkhendrix.serialization.annotation.SharedReference;
import com.sharkhendrix.serialization.annotation.UndefinedType;
import com.sharkhendrix.serialization.util.ReflectionUtils;

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
        if (type.isArray() || Collection.class.isAssignableFrom(type)) {
            appendElements(field);
        }
    }

    private void appendElements(Field field) {
        Class<?>[] declaredTypes = ReflectionUtils.getComponentTypeHierarchy(field);
        ElementsConfiguration[] elementsConfigurations = getElementsConfigurations(field);
        FieldConfiguration currentNode = this;
        if (declaredTypes.length == 0 && elementsConfigurations.length == 0) {
            throw new SharkSerializationException("Undefined type for the elements of the array / collection field " + field);
        }
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
                throw new SharkSerializationException("Undefined type for a sub-element of the array / collection field " + field);
            }
            currentNode.next = nextNode;
            currentNode = nextNode;
        }
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

//    private static Class<?>[] getDeclaredTypesForArray(Class<?> arrayType) {
//        List<Class<?>> types = new ArrayList<>();
//        Class<?> type = arrayType.getComponentType();
//        while (type != null) {
//            types.add(type);
//            type = type.getComponentType();
//        }
//        return types.toArray(Class<?>[]::new);
//    }

}
