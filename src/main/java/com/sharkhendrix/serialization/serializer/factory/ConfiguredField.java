package com.sharkhendrix.serialization.serializer.factory;

import java.lang.reflect.Field;

import com.sharkhendrix.serialization.serializer.ConfigurationNode;

/**
 * Transfer class of a Field and its configuration.
 * 
 * @author Joannick Gardize
 *
 */
public class ConfiguredField {

    private Field field;
    private ConfigurationNode configuration;

    public ConfiguredField(Field field, ConfigurationNode configuration) {
        this.field = field;
        this.configuration = configuration;
    }

    public Field getField() {
        return field;
    }

    public ConfigurationNode getConfiguration() {
        return configuration;
    }
}
