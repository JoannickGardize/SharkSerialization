package com.sharkhendrix.serialization.util;

public class ComponentTypeHierarchy {

    private Class<?> type;

    private ComponentTypeHierarchy elements;

    private ComponentTypeHierarchy keys;

    private ComponentTypeHierarchy values;

    public ComponentTypeHierarchy(Class<?> type) {
        super();
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public ComponentTypeHierarchy getElements() {
        return elements;
    }

    public void setElements(ComponentTypeHierarchy elements) {
        this.elements = elements;
    }

    public ComponentTypeHierarchy getKeys() {
        return keys;
    }

    public void setKeys(ComponentTypeHierarchy keys) {
        this.keys = keys;
    }

    public ComponentTypeHierarchy getValues() {
        return values;
    }

    public void setValues(ComponentTypeHierarchy values) {
        this.values = values;
    }
}