package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.serializer.fieldaccess.FieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.MethodFieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.ObjectFieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.PrimitiveFieldAccessors;
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

    /**
     * Calls {@link #access(String, Function, BiConsumer)} with the next
     * unconfigured field. Takes field from first to last, as it is declared in the
     * class. If it has superclass, its fields are taken after this class.
     * 
     * @param <U>    the field's type
     * @param getter the getter method
     * @param setter the setter method
     * @return this ObjectSerializer for chaining
     */
    public <U> ObjectSerializer<T> access(Function<T, U> getter, BiConsumer<T, U> setter) {
        for (Field field : ReflectionUtils.getAllFields(type)) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (!fieldConfigurations.containsKey(field.getName())) {
                return access(field.getName(), getter, setter);
            }
        }
        throw new IllegalStateException("No more field to configure");
    }

    /**
     * Configure the field to use the provided getter and setter instead of Field
     * reflection access.
     * 
     * @param <U>       the field's type
     * @param fieldName the name of the field, as it is declared in the class
     * @param getter    the getter method
     * @param setter    the setter method
     * @return this ObjectSerializer for chaining
     */
    @SuppressWarnings("unchecked")
    public <U> ObjectSerializer<T> access(String fieldName, Function<T, U> getter, BiConsumer<T, U> setter) {
        try {
            type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalArgumentException(fieldName + " is not a field of " + type.getName());
        }
        ObjectFieldConfiguration configuration = fieldConfigurations.computeIfAbsent(fieldName, n -> new ObjectFieldConfiguration());
        configuration.setGetter((Function<Object, Object>) getter);
        configuration.setSetter((BiConsumer<Object, Object>) setter);
        return this;
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
                    fieldRecordList.add(PrimitiveFieldAccessors.get(field));
                } else {
                    fieldRecordList.add(new ObjectFieldAccessor(field, context.getSerializerFactory().build(field)));
                }
            }
        }
    }
}
