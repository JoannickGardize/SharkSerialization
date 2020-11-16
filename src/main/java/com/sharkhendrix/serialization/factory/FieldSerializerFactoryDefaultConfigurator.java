package com.sharkhendrix.serialization.factory;

import java.util.function.IntFunction;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.serializer.ArraySerializer;
import com.sharkhendrix.serialization.serializer.SharedReferenceSerializer;
import com.sharkhendrix.serialization.serializer.UndefinedTypeSerializer;
import com.sharkhendrix.serialization.util.Record;

public class FieldSerializerFactoryDefaultConfigurator {

    private SerializationContext context;

    public FieldSerializerFactoryDefaultConfigurator(SerializationContext context) {
        this.context = context;
    }

    public void configure() {
        FieldSerializerFactory factory = context.getFieldSerializerFactory();
        factory.setFieldConfigurator(field -> {
            if (field.getType().isArray()) {
                return new FieldConfiguration(field, t -> t.getComponentType());
            } else {
                return new FieldConfiguration(field);
            }
        });
        factory.setDefault(this::defaultBuilder);
        factory.addCase("array", f -> f.getType().isArray() && !f.getType().getComponentType().isPrimitive(), this::nonPrimitiveArrayBuilder);
    }

    private Serializer<?> defaultBuilder(FieldConfiguration fieldConfiguration) {
        return defaultBuilder(fieldConfiguration.getType(), fieldConfiguration);
    }

    private <T> Serializer<? extends T> defaultBuilder(Class<T> type, FieldConfiguration configuration) {
        Serializer<? extends T> serializer;
        if (configuration.isUndefinedType()) {
            serializer = new UndefinedTypeSerializer<>(context);
        } else {
            serializer = context.getSerializer(type);
        }
        if (configuration.isSharedReference()) {
            return new SharedReferenceSerializer<>(context.getReferenceContext(), serializer);
        } else {
            return serializer;
        }
    }

    private Serializer<?> nonPrimitiveArrayBuilder(FieldConfiguration fieldConfiguration) {
        FieldSerializerFactory factory = context.getFieldSerializerFactory();
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Record<IntFunction<?>> constructor = (Record) factory.getSizeableConstructorRecord(fieldConfiguration.getType());
        if (constructor == null) {
            throw new SharkSerializationException("Missing registered constructor for array " + fieldConfiguration.getType().getName());
        }
        if (fieldConfiguration.isSharedReference()) {
            return new SharedReferenceSerializer<>(context.getReferenceContext(), new ArraySerializer<>(constructor.getElement(), factory.build(fieldConfiguration.next())));
        } else {
            return new ArraySerializer<>(constructor.getElement(), factory.build(fieldConfiguration.next()));
        }
    }
}
