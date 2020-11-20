package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.serializer.fieldaccess.FieldAccessor;
import com.sharkhendrix.serialization.serializer.fieldaccess.PrimitiveMethodFieldAccessors;
import com.sharkhendrix.serialization.util.ReflectionUtils;
import com.sharkhendrix.serialization.util.function.ObjBooleanConsumer;
import com.sharkhendrix.serialization.util.function.ObjByteConsumer;
import com.sharkhendrix.serialization.util.function.ObjCharConsumer;
import com.sharkhendrix.serialization.util.function.ObjFloatConsumer;
import com.sharkhendrix.serialization.util.function.ObjShortConsumer;
import com.sharkhendrix.serialization.util.function.ToByteFunction;
import com.sharkhendrix.serialization.util.function.ToCharFunction;
import com.sharkhendrix.serialization.util.function.ToFloatFunction;
import com.sharkhendrix.serialization.util.function.ToShortFunction;

public class ObjectSerializerConfigurationHelper<T> {

    private ObjectSerializer<T> objectSerializer;

    public ObjectSerializerConfigurationHelper(ObjectSerializer<T> objectSerializer) {
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
    public <U> ObjectSerializerConfigurationHelper<T> access(Function<T, U> getter, BiConsumer<T, U> setter) {
        return access(nextUnconfiguredField().getName(), getter, setter);
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
    public <U> ObjectSerializerConfigurationHelper<T> access(String fieldName, Function<T, U> getter, BiConsumer<T, U> setter) {
        Field field = checkFieldExists(fieldName);
        if (field.getType().isPrimitive()) {
            throw new SharkSerializationException("The field " + field + " is a primitive type and must be configured using primitiveAccess() instead");
        }

        ObjectFieldConfiguration configuration = new ObjectFieldConfiguration(fieldName);
        configuration.setGetter((Function<Object, Object>) getter);
        configuration.setSetter((BiConsumer<Object, Object>) setter);
        objectSerializer.addFieldConfiguration(configuration);
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(ToByteFunction<T> getter, ObjByteConsumer<T> setter) {
        return primitiveAccess(nextUnconfiguredField().getName(), getter, setter);
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(String fieldName, ToByteFunction<T> getter, ObjByteConsumer<T> setter) {
        addFieldAccessor(fieldName, PrimitiveMethodFieldAccessors.byteFieldAccessor(getter, setter));
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(String fieldName, ToCharFunction<T> getter, ObjCharConsumer<T> setter) {
        addFieldAccessor(fieldName, PrimitiveMethodFieldAccessors.charFieldAccessor(getter, setter));
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(Predicate<T> getter, ObjBooleanConsumer<T> setter) {
        return primitiveAccess(nextUnconfiguredField().getName(), getter, setter);
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(String fieldName, Predicate<T> getter, ObjBooleanConsumer<T> setter) {
        addFieldAccessor(fieldName, PrimitiveMethodFieldAccessors.booleanFieldAccessor(getter, setter));
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(ToShortFunction<T> getter, ObjShortConsumer<T> setter) {
        return primitiveAccess(nextUnconfiguredField().getName(), getter, setter);
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(String fieldName, ToShortFunction<T> getter, ObjShortConsumer<T> setter) {
        addFieldAccessor(fieldName, PrimitiveMethodFieldAccessors.shortFieldAccessor(getter, setter));
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(ToIntFunction<T> getter, ObjIntConsumer<T> setter) {
        return primitiveAccess(nextUnconfiguredField().getName(), getter, setter);
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(String fieldName, ToIntFunction<T> getter, ObjIntConsumer<T> setter) {
        addFieldAccessor(fieldName, PrimitiveMethodFieldAccessors.intFieldAccessor(getter, setter));
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(ToLongFunction<T> getter, ObjLongConsumer<T> setter) {
        return primitiveAccess(nextUnconfiguredField().getName(), getter, setter);
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(String fieldName, ToLongFunction<T> getter, ObjLongConsumer<T> setter) {
        addFieldAccessor(fieldName, PrimitiveMethodFieldAccessors.longFieldAccessor(getter, setter));
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(ToFloatFunction<T> getter, ObjFloatConsumer<T> setter) {
        return primitiveAccess(nextUnconfiguredField().getName(), getter, setter);
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(String fieldName, ToFloatFunction<T> getter, ObjFloatConsumer<T> setter) {
        addFieldAccessor(fieldName, PrimitiveMethodFieldAccessors.floatFieldAccessor(getter, setter));
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter) {
        return primitiveAccess(nextUnconfiguredField().getName(), getter, setter);
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(String fieldName, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter) {
        addFieldAccessor(fieldName, PrimitiveMethodFieldAccessors.doubleFieldAccessor(getter, setter));
        return this;
    }

    public ObjectSerializerConfigurationHelper<T> primitiveAccess(ToCharFunction<T> getter, ObjCharConsumer<T> setter) {
        return primitiveAccess(nextUnconfiguredField().getName(), getter, setter);
    }

    private Field nextUnconfiguredField() {
        for (Field field : ReflectionUtils.getAllFields(objectSerializer.getType())) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (objectSerializer.getFieldConfiguration(field.getName()) == null) {
                return field;
            }
        }
        throw new IllegalStateException("No more field to configure");
    }

    private Field checkFieldExists(String fieldName) {
        try {
            return objectSerializer.getType().getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalArgumentException(fieldName + " is not a field of " + objectSerializer.getType().getName());

        }
    }

    private void addFieldAccessor(String fieldName, FieldAccessor fieldAccessor) {
        checkFieldExists(fieldName);
        ObjectFieldConfiguration configuration = new ObjectFieldConfiguration(fieldName);
        configuration.setAccessor(fieldAccessor);
        objectSerializer.addFieldConfiguration(configuration);
    }
}
