package com.sharkhendrix.serialization.factory;

import java.lang.reflect.Field;
import java.util.function.UnaryOperator;

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
        this(field, t -> null);
    }

    public FieldConfiguration(Field field, UnaryOperator<Class<?>> componentTypeGetter) {
        type = field.getType();
        sharedReference = field.isAnnotationPresent(SharedReference.class);
        undefinedType = field.isAnnotationPresent(UndefinedType.class);
        FieldConfiguration currentNode = this;
        Class<?> componentType = componentTypeGetter.apply(currentNode.type);
        int configurationIndex = 0;
        ElementsConfiguration[] elementConfigurations = getElementsConfigurations(field);
        while (componentType != null) {
            FieldConfiguration nextNode = new FieldConfiguration();
            nextNode.type = componentType;
            if (configurationIndex < elementConfigurations.length) {
                ElementsConfiguration elementsConfiguration = elementConfigurations[configurationIndex];
                nextNode.sharedReference = elementsConfiguration.sharedReference();
                nextNode.undefinedType = elementsConfiguration.undefinedType();
            }
            currentNode.next = nextNode;
            currentNode = nextNode;
            componentType = componentTypeGetter.apply(currentNode.type);
            configurationIndex++;
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

    private ElementsConfiguration[] getElementsConfigurations(Field field) {
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
}
