package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;

public class FieldSerializerFactory {

    private FieldSerializerFactory() {
    }

    public static Serializer<?> get(Field field, SerializationContext context) {
        return get(field.getType(), new SerializationConfigurationNode(field), context);
    }

    private static Serializer<?> get(Class<?> type, SerializationConfigurationNode configurationNode, SerializationContext context) {
        if (type.isArray() && !type.getComponentType().isPrimitive()) {
            return new ArraySerializer<>(getContextSerializer(type, configurationNode, context), get(type.getComponentType(), configurationNode.nextOrDefault(), context));
        } else {
            return getContextSerializer(type, configurationNode, context);
        }
    }

    private static <T> Serializer<? extends T> getContextSerializer(Class<T> type, SerializationConfigurationNode configurationNode, SerializationContext context) {
        if (configurationNode.isSharedReference()) {
            if (configurationNode.isUndefinedType()) {
                return new SharedReferenceSerializer<>(context.getReferenceContext(), new UndefinedTypeSerializer<>(context));
            } else {
                return new SharedReferenceSerializer<>(context.getReferenceContext(), context.getSerializer(type));
            }
        } else {
            if (configurationNode.isUndefinedType()) {
                return new UndefinedTypeSerializer<>(context);
            } else {
                return context.getSerializer(type);
            }
        }
    }
}
