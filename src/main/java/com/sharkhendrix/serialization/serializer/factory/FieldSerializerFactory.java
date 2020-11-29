package com.sharkhendrix.serialization.serializer.factory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.serializer.ConfigurationNode;
import com.sharkhendrix.serialization.serializer.fieldaccess.FieldAccessor;
import com.sharkhendrix.util.ConditionalFactory;

/**
 * Skeleton of factory of {@link Serializer}s and {@link FieldAccessor}s.
 * 
 * @author Joannick Gardize
 *
 */
public class FieldSerializerFactory {

    private Map<Class<?>, IntFunction<?>> sizeableConstructorRecordSet = new HashMap<>();
    private ConditionalFactory<ConfigurationNode, Serializer<?>> serializerFactory = new ConditionalFactory<>();
    private ConditionalFactory<ConfiguredField, FieldAccessor> fieldAccessorFactory = new ConditionalFactory<>();
    private BiFunction<Field, ConfigurationNode, ConfigurationNode> fieldConfigurator;

    public <T> void registerSizeableConstructor(Class<T> type, IntFunction<? extends T> constructor) {
        sizeableConstructorRecordSet.put(type, constructor);
    }

    @SuppressWarnings("unchecked")
    public <T> IntFunction<? extends T> getSizeableConstructorRecord(Class<T> type) {
        return (IntFunction<? extends T>) sizeableConstructorRecordSet.get(type);
    }

    /**
     * Set the field configurator used by the {@code fieldAccessorFactory}. The
     * function takes in parameters the concerned field and its eventual default
     * configuration, and returns the final configuration to be passed as argument
     * to the {@code fieldAccessorFactory} when calling
     * {@link #buildFieldAccessor(Field, ConfigurationNode)}.
     * 
     * @param fieldConfigurator
     */
    public void setFieldConfigurator(BiFunction<Field, ConfigurationNode, ConfigurationNode> fieldConfigurator) {
        this.fieldConfigurator = fieldConfigurator;
    }

    public ConditionalFactory<ConfigurationNode, Serializer<?>> getSerializerFactory() {
        return serializerFactory;
    }

    public ConditionalFactory<ConfiguredField, FieldAccessor> getFieldAccessorFactory() {
        return fieldAccessorFactory;
    }

    public FieldAccessor buildFieldAccessor(Field field, ConfigurationNode defaultConfiguration) {
        return fieldAccessorFactory.build(new ConfiguredField(field, fieldConfigurator.apply(field, defaultConfiguration)));
    }

    public Serializer<?> buildSerializer(ConfigurationNode configuration) {
        return serializerFactory.build(configuration);
    }
}
