package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.sharkhendrix.serialization.serializer.fieldaccess.PrimitiveMethodFieldAccessors;
import com.sharkhendrix.serialization.util.ReflectionUtils;
import com.sharkhendrix.serialization.util.function.ObjByteConsumer;
import com.sharkhendrix.serialization.util.function.ToByteFunction;

public class ObjectSerializerConfigurator<T> {

    private ObjectSerializer<T> objectSerializer;

    public ObjectSerializerConfigurator(ObjectSerializer<T> objectSerializer) {
        super();
        this.objectSerializer = objectSerializer;
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
    public <U> ObjectSerializerConfigurator<T> access(Function<T, U> getter, BiConsumer<T, U> setter) {
        Field field = nextUnconfiguredField();
        if (field == null) {
            throw new IllegalStateException("No more field to configure");
        } else {
            return access(field.getName(), getter, setter);
        }
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
    public <U> ObjectSerializerConfigurator<T> access(String fieldName, Function<T, U> getter, BiConsumer<T, U> setter) {
        checkFieldExists(fieldName);
        ObjectFieldConfiguration configuration = new ObjectFieldConfiguration(fieldName);
        configuration.setGetter((Function<Object, Object>) getter);
        configuration.setSetter((BiConsumer<Object, Object>) setter);
        objectSerializer.addFieldConfiguration(configuration);
        return this;
    }

    public <U> ObjectSerializerConfigurator<T> accessByte(ToByteFunction<T> getter, ObjByteConsumer<T> setter) {
        Field field = nextUnconfiguredField();
        if (field == null) {
            throw new IllegalStateException("No more field to configure");
        } else {
            return accessByte(field.getName(), getter, setter);
        }
    }

    public <U> ObjectSerializerConfigurator<T> accessByte(String fieldName, ToByteFunction<T> getter, ObjByteConsumer<T> setter) {
        checkFieldExists(fieldName);
        ObjectFieldConfiguration configuration = new ObjectFieldConfiguration(fieldName);
        configuration.setAccessor(PrimitiveMethodFieldAccessors.byteFieldAccessor(getter, setter));
        objectSerializer.addFieldConfiguration(configuration);
        return this;
    }

    private Field nextUnconfiguredField() {
        for (Field field : ReflectionUtils.getAllFields(objectSerializer.getType())) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (objectSerializer.getFieldConfiguration(field.getName()) != null) {
                return field;
            }
        }
        return null;
    }

    private void checkFieldExists(String fieldName) {
        try {
            objectSerializer.getType().getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalArgumentException(fieldName + " is not a field of " + objectSerializer.getType().getName());
        }
    }
}
