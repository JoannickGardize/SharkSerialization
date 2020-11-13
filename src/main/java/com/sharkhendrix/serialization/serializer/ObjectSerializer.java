package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.serializer.field.FieldAccessor;
import com.sharkhendrix.serialization.serializer.field.ObjectFieldAccessor;
import com.sharkhendrix.serialization.serializer.field.PrimitiveFieldAccessors;
import com.sharkhendrix.serialization.util.ReflectionUtils;

public class ObjectSerializer<T> implements Serializer<T> {

    private Class<T> type;
    private Supplier<? extends T> newInstanceSupplier;
    private FieldAccessor[] fieldRecords;

    public ObjectSerializer(Class<T> type, Supplier<? extends T> newInstanceSupplier) {
        this.type = type;
        this.newInstanceSupplier = newInstanceSupplier;
    }

    @Override
    public void initialize(SerializationContext context) {
        if (!ReflectionUtils.isInstanciable(type)) {
            throw new IllegalArgumentException("Cannot initialize ObjectSerializer for the class " + type.getName() + ", it must be a regular class");
        }
        Class<?> currentClass = type;
        List<FieldAccessor> fieldRecordList = new ArrayList<>();
        while (currentClass != Object.class) {
            registerFields(context, currentClass, fieldRecordList);
            currentClass = currentClass.getSuperclass();
        }
        fieldRecords = fieldRecordList.toArray(FieldAccessor[]::new);
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        try {
            for (FieldAccessor record : fieldRecords) {
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
            for (FieldAccessor record : fieldRecords) {
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

    private void registerFields(SerializationContext context, Class<?> currentClass, List<FieldAccessor> fieldRecordList) {
        for (Field field : currentClass.getDeclaredFields()) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (field.getType().isPrimitive()) {
                fieldRecordList.add(PrimitiveFieldAccessors.get(field));
            } else {
                fieldRecordList.add(new ObjectFieldAccessor(field, FieldSerializerFactory.get(field, context)));
            }
        }
    }
}
