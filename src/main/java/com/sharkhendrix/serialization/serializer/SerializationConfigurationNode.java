package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;

import com.sharkhendrix.serialization.CollectionConfiguration;
import com.sharkhendrix.serialization.CollectionConfigurationGroup;
import com.sharkhendrix.serialization.SharedReference;
import com.sharkhendrix.serialization.UndefinedType;

public class SerializationConfigurationNode {

    private static final SerializationConfigurationNode DEFAULT_NODE = new SerializationConfigurationNode();

    private boolean sharedReference = false;
    private boolean undefinedType = false;

    private SerializationConfigurationNode next = null;

    public SerializationConfigurationNode() {
    }

    public SerializationConfigurationNode(Field field) {
        sharedReference = field.isAnnotationPresent(SharedReference.class);
        undefinedType = field.isAnnotationPresent(UndefinedType.class);
        SerializationConfigurationNode currentNode = this;
        for (CollectionConfiguration collectionConfiguration : getCollectionConfigurations(field)) {
            SerializationConfigurationNode node = new SerializationConfigurationNode();
            node.sharedReference = collectionConfiguration.sharedReference();
            node.undefinedType = collectionConfiguration.undefinedType();
            currentNode.next = node;
            currentNode = node;
        }
    }

    public boolean isSharedReference() {
        return sharedReference;
    }

    public boolean isUndefinedType() {
        return undefinedType;
    }

    public SerializationConfigurationNode nextOrDefault() {
        return next == null ? DEFAULT_NODE : next;
    }

    private CollectionConfiguration[] getCollectionConfigurations(Field field) {
        CollectionConfigurationGroup collectionConfigurationGroup = field.getAnnotation(CollectionConfigurationGroup.class);
        if (collectionConfigurationGroup != null) {
            return collectionConfigurationGroup.value();
        }
        CollectionConfiguration collectionConfiguration = field.getAnnotation(CollectionConfiguration.class);
        if (collectionConfiguration != null) {
            return new CollectionConfiguration[] { collectionConfiguration };
        }
        return new CollectionConfiguration[0];
    }
}
