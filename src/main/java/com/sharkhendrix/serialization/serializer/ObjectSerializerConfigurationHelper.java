package com.sharkhendrix.serialization.serializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility object to configure an {@link ObjectSerializer} in a chaining way.
 * This is an alternative and equivalent method of configuration to the
 * annotation way.
 * 
 * @author Joannick Gardize
 *
 */
public class ObjectSerializerConfigurationHelper {

    private ObjectSerializer<?> objectSerializer;
    private ConfigurationNode currentNode;
    private List<ConfigurationNode> mapStack = new ArrayList<>();

    /**
     * Create a configuration helper for the given ObjectSerializer.
     * 
     * @param objectSerializer the ObjectSerializer to configure
     */
    public ObjectSerializerConfigurationHelper(ObjectSerializer<?> objectSerializer) {
        this.objectSerializer = objectSerializer;
    }

    /**
     * Start to configure the given field. And set the current node the field's root
     * (this matters for array / collection / map types).
     * 
     * @param fieldName the field name as it is declared in the class
     * @return this, for chaining
     */
    public ObjectSerializerConfigurationHelper configure(String fieldName) {
        currentNode = objectSerializer.configure(fieldName);
        return this;
    }

    /**
     * Mark the current node as shared reference.
     * 
     * @return this, for chaining
     */
    public ObjectSerializerConfigurationHelper sharedReference() {
        currentNode.setSharedReference(true);
        return this;
    }

    /**
     * Mark the current node as undefined type.
     * 
     * @return this, for chaining
     */
    public ObjectSerializerConfigurationHelper undefinedType() {
        currentNode.setUndefinedType(true);
        return this;
    }

    /**
     * Specify the concrete type of the current node.
     * 
     * @param type the concre type to use for the current node
     * @return this, for chaining
     */
    public ObjectSerializerConfigurationHelper concreteType(Class<?> type) {
        currentNode.setType(type);
        return this;
    }

    /**
     * Start to configure the elements of the current array / collection.
     * 
     * @return this, for chaining
     */
    public ObjectSerializerConfigurationHelper elements() {
        if (currentNode.getElementsConfiguration() == null) {
            currentNode.setElementsConfiguration(new ConfigurationNode());
        }
        currentNode = currentNode.getElementsConfiguration();
        return this;
    }

    /**
     * Start to configure the keys of the current map. Must always precede a call to
     * {@link #values()}
     * 
     * @return this, for chaining
     */
    public ObjectSerializerConfigurationHelper keys() {
        if (currentNode.getKeysConfiguration() == null) {
            currentNode.setKeysConfiguration(new ConfigurationNode());
        }
        mapStack.add(currentNode);
        currentNode = currentNode.getKeysConfiguration();
        return this;
    }

    /**
     * Start to configure the values of the previous map node corresponding to the
     * previous {@link #keys()} call.
     * 
     * @return this, for chaining
     * @throws IllegalStateException if this call does not succeed a {@link #keys()}
     *                               call
     */

    public ObjectSerializerConfigurationHelper values() {
        if (mapStack.isEmpty()) {
            throw new IllegalStateException("No keys before values");
        }
        currentNode = mapStack.remove(mapStack.size() - 1);
        if (currentNode.getValuesConfiguration() == null) {
            currentNode.setValuesConfiguration(new ConfigurationNode());
        }
        currentNode = currentNode.getValuesConfiguration();
        return this;
    }
}
