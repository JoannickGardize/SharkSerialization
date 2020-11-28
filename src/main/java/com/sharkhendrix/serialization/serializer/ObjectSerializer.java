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
import com.sharkhendrix.serialization.SharkSerializationConfigurationException;
import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.serializer.fieldaccess.FieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.ObjectFieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.PrimitiveFieldAccessors;
import com.sharkhendrix.serialization.util.ReflectionUtils;

public class ObjectSerializer<T> implements Serializer<T> {

    private Class<T> type;
    private Supplier<? extends T> newInstanceSupplier;
    private FieldAccessor[] fieldAccessors;
    private Map<String, ConfigurationNode> fieldConfigurations = new HashMap<>();

    public ObjectSerializer(Class<T> type, Supplier<? extends T> newInstanceSupplier) {
        this.type = type;
        this.newInstanceSupplier = newInstanceSupplier;
    }

    public Class<T> getType() {
        return type;
    }

    public ConfigurationNode configure(String fieldName) {
        ConfigurationNode node = fieldConfigurations.get(fieldName);
        if (node != null) {
            return node;
        }
        for (Field field : ReflectionUtils.getAllFields(type)) {
            if (field.getName().equals(fieldName)) {
                node = new ConfigurationNode();
                fieldConfigurations.put(field.getName(), node);
                return node;
            }
        }
        throw new SharkSerializationConfigurationException("No field with the name " + fieldName + " for the class " + type);
    }

    @Override
    public void initialize(SerializationContext context) {
        if (!ReflectionUtils.isInstanciable(type)) {
            throw new IllegalArgumentException("Cannot initialize ObjectSerializer for the class " + type.getName() + ", it must be a regular class");
        }
        List<FieldAccessor> fieldRecordList = new ArrayList<>();
        registerFields(context, fieldRecordList);
        fieldAccessors = fieldRecordList.toArray(FieldAccessor[]::new);
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

    private void registerFields(SerializationContext context, List<FieldAccessor> fieldRecordList) {
        for (Field field : ReflectionUtils.getAllFields(type)) {
            // TODO Make this part configurable
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (field.getType().isPrimitive()) {
                fieldRecordList.add(PrimitiveFieldAccessors.get(field));
            } else {
                ConfigurationNode configuration = fieldConfigurations.get(field.getName());
                if (configuration == null || !configuration.isIgnore()) {
                    fieldRecordList.add(new ObjectFieldAccessor(field, context.getSerializerFactory().build(field, configuration)));
                }
            }
        }
    }
}
