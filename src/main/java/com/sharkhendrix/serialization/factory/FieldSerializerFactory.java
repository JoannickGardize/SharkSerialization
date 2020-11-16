package com.sharkhendrix.serialization.factory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.util.Record;
import com.sharkhendrix.serialization.util.RecordSet;

public class FieldSerializerFactory {

    private RecordSet<Class<?>, IntFunction<?>> sizeableConstructorRecordSet = new RecordSet<>();

    private Map<String, FieldSerializerFactoryEntry> caseEntries = new HashMap<>();
    private Function<FieldConfiguration, Serializer<?>> defaultBuilder;
    private Function<Field, FieldConfiguration> fieldConfigurator;

    public <T> void registerSizeableConstructor(Class<T> type, IntFunction<? extends T> constructor) {
        sizeableConstructorRecordSet.register(type, constructor);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> Record<IntFunction<? extends T>> getSizeableConstructorRecord(Class<T> type) {
        return (Record) sizeableConstructorRecordSet.get(type);
    }

    public void addCase(String caseName, Predicate<FieldConfiguration> condition, Function<FieldConfiguration, Serializer<?>> builder) {
        caseEntries.put(caseName, new FieldSerializerFactoryEntry(condition, builder));
    }

    public void removeCase(String caseName) {
        caseEntries.remove(caseName);
    }

    public void setDefault(Function<FieldConfiguration, Serializer<?>> defaultBuilder) {
        this.defaultBuilder = defaultBuilder;
    }

    public void setFieldConfigurator(Function<Field, FieldConfiguration> fieldConfigurator) {
        this.fieldConfigurator = fieldConfigurator;
    }

    public Serializer<?> build(Field field) {
        return build(fieldConfigurator.apply(field));
    }

    public Serializer<?> build(FieldConfiguration fieldConfiguration) {
        for (FieldSerializerFactoryEntry entry : caseEntries.values()) {
            if (entry.test(fieldConfiguration)) {
                return entry.build(fieldConfiguration);
            }
        }
        return defaultBuilder.apply(fieldConfiguration);
    }

}
