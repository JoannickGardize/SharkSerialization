package com.sharkhendrix.serialization.factory;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.SharkSerializationException;
import com.sharkhendrix.serialization.serializer.ArraySerializer;
import com.sharkhendrix.serialization.serializer.CollectionSerializer;
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
            if (field.getType().isArray() && !field.getType().getComponentType().isPrimitive()) {
                return FieldConfiguration.forArray(field);
            } else if (Collection.class.isAssignableFrom(List.class)) {
                return FieldConfiguration.forCollection(field);
            } else {
                return new FieldConfiguration(field);
            }
        });
        factory.setDefault(this::defaultBuilder);
        factory.addCase("array", f -> f.getType() != null && f.getType().isArray() && !f.getType().getComponentType().isPrimitive(), this::nonPrimitiveArrayBuilder);
        factory.addCase("collection", f -> f.getType() != null && Collection.class.isAssignableFrom(f.getType()), this::collectionBuilder);
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
        return collectionBuilder(fieldConfiguration, (t, u) -> new ArraySerializer<>(t, u));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Serializer<?> collectionBuilder(FieldConfiguration fieldConfiguration) {
        return collectionBuilder(fieldConfiguration, (t, u) -> new CollectionSerializer(t, u));
    }

    private Serializer<?> collectionBuilder(FieldConfiguration fieldConfiguration, BiFunction<IntFunction<?>, Serializer<?>, Serializer<?>> serializerConstructor) {
        FieldSerializerFactory factory = context.getFieldSerializerFactory();
        if (fieldConfiguration.isUndefinedType()) {
            throw new SharkSerializationException("UndefinedType is unsupported by the field factory for arrays and collections.");
        }
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Record<IntFunction<?>> constructor = (Record) factory.getSizeableConstructorRecord(fieldConfiguration.getType());
        if (constructor == null) {
            throw new SharkSerializationException("Missing registered constructor for array/collection " + fieldConfiguration.getType().getName());
        }
        if (fieldConfiguration.isSharedReference()) {
            return new SharedReferenceSerializer<>(context.getReferenceContext(), serializerConstructor.apply(constructor.getElement(), factory.build(fieldConfiguration.next())));
        } else {
            return serializerConstructor.apply(constructor.getElement(), factory.build(fieldConfiguration.next()));
        }
    }
}
