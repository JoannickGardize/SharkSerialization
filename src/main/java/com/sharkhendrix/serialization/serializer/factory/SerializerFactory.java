package com.sharkhendrix.serialization.serializer.factory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.serializer.ConfigurationNode;

public class SerializerFactory {

    private Map<Class<?>, IntFunction<?>> sizeableConstructorRecordSet = new HashMap<>();

    private Map<String, SerializerFactoryEntry> caseEntries = new HashMap<>();
    private Function<ConfigurationNode, Serializer<?>> defaultBuilder;
    private BiFunction<Field, ConfigurationNode, ConfigurationNode> nodeConfigurator;

    public <T> void registerSizeableConstructor(Class<T> type, IntFunction<? extends T> constructor) {

        sizeableConstructorRecordSet.put(type, constructor);
    }

    @SuppressWarnings("unchecked")
    public <T> IntFunction<? extends T> getSizeableConstructorRecord(Class<T> type) {
        return (IntFunction<? extends T>) sizeableConstructorRecordSet.get(type);
    }

    public void addCase(String caseName, Predicate<ConfigurationNode> condition, Function<ConfigurationNode, Serializer<?>> builder) {
        caseEntries.put(caseName, new SerializerFactoryEntry(condition, builder));
    }

    public void removeCase(String caseName) {
        caseEntries.remove(caseName);
    }

    public void setDefault(Function<ConfigurationNode, Serializer<?>> defaultBuilder) {
        this.defaultBuilder = defaultBuilder;
    }

    public void setNodeConfigurator(BiFunction<Field, ConfigurationNode, ConfigurationNode> fieldConfigurator) {
        this.nodeConfigurator = fieldConfigurator;
    }

    public Serializer<?> build(Field field, ConfigurationNode defaultConfiguration) {
        return build(nodeConfigurator.apply(field, defaultConfiguration));
    }

    public Serializer<?> build(ConfigurationNode fieldConfiguration) {
        for (SerializerFactoryEntry entry : caseEntries.values()) {
            if (entry.test(fieldConfiguration)) {
                return entry.build(fieldConfiguration);
            }
        }
        return defaultBuilder.apply(fieldConfiguration);
    }

}
