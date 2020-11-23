package com.sharkhendrix.serialization.util;

/**
 * <p>
 * Represents the type of a class and its eventual components type hierarchy.
 * <p>
 * If the type is an array or a collection, the method {@link #getElements()}
 * will return its component type.
 * <p>
 * If the type is a map, the method {@link #getKeys()} will return its keys type
 * and {@link #getValues()} will return its values type.
 * <p>
 * If the type is not a container type, all these methods will return null.
 *
 * @author Joannick Gardize
 *
 */
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