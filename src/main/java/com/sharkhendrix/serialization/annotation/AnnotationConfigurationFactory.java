package com.sharkhendrix.serialization.annotation;

import java.lang.reflect.Field;

import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.factory.ConfigurationNode;

public class AnnotationConfigurationFactory {

    private AnnotationConfigurationFactory() {
    }

    public static ConfigurationNode build(Field field) {
        ConfigurationNode node = createRootNode(field);
        processNextNode(node, getElementsConfigurations(field), 0);
        return node;
    }

    private static ConfigurationNode createRootNode(Field field) {
        ConfigurationNode node = new ConfigurationNode();
        ConcreteType concreteType = field.getAnnotation(ConcreteType.class);
        if (concreteType != null) {
            node.setType(concreteType.value());
        } else {
            node.setType(field.getType());
        }
        node.setSharedReference(field.isAnnotationPresent(SharedReference.class));
        node.setUndefinedType(field.isAnnotationPresent(UndefinedType.class));
        return node;
    }

    private static int processNextNode(ConfigurationNode node, ElementsConfiguration[] annotations, int index) {
        boolean requiresValues = false;
        for (int i = index; i < annotations.length; i++) {
            ElementsConfiguration annotation = annotations[i];
            switch (annotation.type()) {
            case ELEMENTS:
                ConfigurationNode elementsNode = createNode(annotation);
                node.setElementsConfiguration(elementsNode);
                return processNextNode(elementsNode, annotations, i + 1);
            case KEYS:
                ConfigurationNode keysNode = createNode(annotation);
                node.setKeysConfiguration(keysNode);
                i = processNextNode(keysNode, annotations, i + 1);
                requiresValues = true;
                break;
            case VALUES:
                if (requiresValues) {
                    ConfigurationNode valuesNode = createNode(annotation);
                    node.setValuesConfiguration(valuesNode);
                    return processNextNode(valuesNode, annotations, i + 1);
                } else {
                    return i - 1;
                }
            }
        }
        if (requiresValues) {
            throw new SharkSerializationException("Malformed configuration annotations : an ElementsConfiguration of type KEYS is missing it's VALUES pair");
        }
        return annotations.length;
    }

    private static ConfigurationNode createNode(ElementsConfiguration annotation) {
        ConfigurationNode node = new ConfigurationNode();
        if (annotation.concreteType() != void.class) {
            node.setType(annotation.concreteType());
        }
        node.setSharedReference(annotation.sharedReference());
        node.setUndefinedType(annotation.undefinedType());
        return node;
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
}
