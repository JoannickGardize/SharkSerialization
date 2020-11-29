package com.sharkhendrix.serialization.serializer;

import com.sharkhendrix.serialization.annotation.VarLenStrategy;

/**
 * Serialization configuration of a class field and its array / collection / map
 * sub-elements.
 * 
 * @author Joannick Gardize
 *
 */
public class ConfigurationNode {

    private Class<?> type = null;

    private boolean sharedReference = false;
    private boolean undefinedType = false;
    private boolean ignore = false;
    private VarLenStrategy varLenStrategy = VarLenStrategy.NORMAL;

    private ConfigurationNode elementsConfiguration = null;
    private ConfigurationNode keysConfiguration = null;
    private ConfigurationNode valuesConfiguration = null;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isSharedReference() {
        return sharedReference;
    }

    public void setSharedReference(boolean sharedReference) {
        this.sharedReference = sharedReference;
    }

    public boolean isUndefinedType() {
        return undefinedType;
    }

    public void setUndefinedType(boolean undefinedType) {
        this.undefinedType = undefinedType;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public VarLenStrategy getVarLenStrategy() {
        return varLenStrategy;
    }

    public void setVarLenStrategy(VarLenStrategy varLenStrategy) {
        this.varLenStrategy = varLenStrategy;
    }

    public ConfigurationNode getElementsConfiguration() {
        return elementsConfiguration;
    }

    public void setElementsConfiguration(ConfigurationNode elementsConfiguration) {
        this.elementsConfiguration = elementsConfiguration;
    }

    public ConfigurationNode getKeysConfiguration() {
        return keysConfiguration;
    }

    public void setKeysConfiguration(ConfigurationNode keysConfiguration) {
        this.keysConfiguration = keysConfiguration;
    }

    public ConfigurationNode getValuesConfiguration() {
        return valuesConfiguration;
    }

    public void setValuesConfiguration(ConfigurationNode valuesConfiguration) {
        this.valuesConfiguration = valuesConfiguration;
    }
}
