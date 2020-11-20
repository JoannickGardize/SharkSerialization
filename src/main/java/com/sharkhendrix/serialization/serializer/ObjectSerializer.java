package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.serializer.fieldaccess.FieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.MethodFieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.ObjectReflectionFieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.PrimitiveReflectionFieldAccessors;
import com.sharkhendrix.serialization.util.ReflectionUtils;

public class ObjectSerializer<T> implements Serializer<T> {

    private Class<T> type;
    private Supplier<? extends T> newInstanceSupplier;
    private FieldAccessor[] fieldAccessors;
    private Map<String, ObjectFieldConfiguration> fieldConfigurations = new HashMap<>();

    public ObjectSerializer(Class<T> type, Supplier<? extends T> newInstanceSupplier) {
        this.type = type;
        this.newInstanceSupplier = newInstanceSupplier;
    }

    public Class<T> getType() {
        return type;
    }

    public void addFieldConfiguration(ObjectFieldConfiguration configuration) {
        fieldConfigurations.put(configuration.getFieldName(), configuration);
    }

    public ObjectFieldConfiguration getFieldConfiguration(String fieldName) {
        return fieldConfigurations.get(fieldName);
    }

    public ObjectSerializerConfigurator<T> configure() {
        return new ObjectSerializerConfigurator<>(this);
    }

    @Override
    public void initialize(SerializationContext context) {
        if (!ReflectionUtils.isInstanciable(type)) {
            throw new IllegalArgumentException("Cannot initialize ObjectSerializer for the class " + type.getName() + ", it must be a regular class");
        }
        List<FieldAccessor> fieldRecordList = new ArrayList<>();
        registerFields(context, fieldRecordList);
        fieldAccessors = fieldRecordList.toArray(FieldAccessor[]::new);
        fieldConfigurations = null;
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        try {
            for (FieldAccessor record : fieldAccessors) {
                record.writeField(buffer, object);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new SharkSerializationException(e);
        }
    }

    @Override
    public T read(ByteBuffer buffer, Consumer<T> intermediateConsumer) {
        T object = newInstanceSupplier.get();
        intermediateConsumer.accept(object);
        try {
            for (FieldAccessor record : fieldAccessors) {
                record.readField(buffer, object);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new SharkSerializationException(e);
        }
        return object;
    }

    @Override
    public T read(ByteBuffer buffer) {
        return read(buffer, o -> {
        });
    }

    @SuppressWarnings("unchecked")
    private void registerFields(SerializationContext context, List<FieldAccessor> fieldRecordList) {
        for (Field field : ReflectionUtils.getAllFields(type)) {
            // TODO Make this part configurable
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            ObjectFieldConfiguration configuration = fieldConfigurations.get(field.getName());
            // TODO refactor this
            if (configuration != null && configuration.getGetter() != null && configuration.getSetter() != null) {
                fieldRecordList
                        .add(new MethodFieldAccessor(configuration.getGetter(), configuration.getSetter(), (Serializer<Object>) context.getSerializerFactory().build(field)));
            } else {
                if (field.getType().isPrimitive()) {
                    fieldRecordList.add(PrimitiveReflectionFieldAccessors.get(field));
                } else {
                    fieldRecordList.add(new ObjectReflectionFieldAccessor(field, context.getSerializerFactory().build(field)));
                }
            }
        }
    }
}
