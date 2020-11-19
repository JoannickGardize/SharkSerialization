package com.sharkhendrix.serialization.factory;

import com.sharkhendrix.serialization.util.ComponentTypeHierarchy;

public class ConfigurationNodeTypeMerger {

    private ConfigurationNodeTypeMerger() {

    }

    public static ConfigurationNode merge(ConfigurationNode node, ComponentTypeHierarchy hierarchy) {
        if (node.getType() == null) {
            node.setType(hierarchy.getType());
        }
        if (hierarchy.getElements() != null) {
            if (node.getElementsConfiguration() == null) {
                node.setElementsConfiguration(new ConfigurationNode());
            }
            merge(node.getElementsConfiguration(), hierarchy.getElements());
        }
        if (hierarchy.getKeys() != null) {
            if (node.getKeysConfiguration() == null) {
                node.setKeysConfiguration(new ConfigurationNode());
            }
            merge(node.getKeysConfiguration(), hierarchy.getKeys());
        }
        if (hierarchy.getValues() != null) {
            if (node.getValuesConfiguration() == null) {
                node.setValuesConfiguration(new ConfigurationNode());
            }
            merge(node.getValuesConfiguration(), hierarchy.getValues());
        }
        return node;
    }
}
