package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.factory.FieldSerializerFactory;
import com.sharkhendrix.serialization.factory.FieldSerializerFactoryDefaultConfigurator;
import com.sharkhendrix.serialization.serializer.DefaultSerializers;
import com.sharkhendrix.serialization.serializer.ObjectSerializer;
import com.sharkhendrix.serialization.util.Record;
import com.sharkhendrix.serialization.util.RecordSet;

public class SharkSerialization implements SerializationContext {

    private ReferenceContext referenceContext = new IdentityMapReferenceContext();

    private RecordSet<Class<?>, Serializer<?>> serializerRecordSet = new RecordSet<>();

    private Record<Serializer<?>> nullSerializerRecord;

    private FieldSerializerFactory fieldSerializerFactory = new FieldSerializerFactory();

    public SharkSerialization() {
        DefaultSerializers.registerAll(this);
        nullSerializerRecord = serializerRecordSet.get(null);
        new FieldSerializerFactoryDefaultConfigurator(this).configure();
    }

    public <T> void register(Class<T> type, Supplier<? extends T> constructor) {
        register(type, new ObjectSerializer<>(type, constructor));
    }

    public <T> void registerConstructor(Class<T> type, IntFunction<? extends T> constructor) {
        fieldSerializerFactory.registerSizeableConstructor(type, constructor);
    }

    @Override
    public <T> void register(Class<T> type, Serializer<? extends T> serializer) {
        serializerRecordSet.register(type, serializer);
    }

    public void initialize() {
        serializerRecordSet.forEachValues(s -> s.initialize(this));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Serializer<? extends T> getSerializer(Class<T> type) {
        Record<Serializer<?>> serializerRecord = serializerRecordSet.get(type);
        if (serializerRecord == null) {
            throw new SharkSerializationException("No serializer recorded for class : " + type.getName());
        }
        return (Serializer<? extends T>) serializerRecord.getElement();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Serializer<T> writeType(ByteBuffer buffer, T o) {
        Record<Serializer<?>> serializerRecord;
        if (o == null) {
            serializerRecord = nullSerializerRecord;
        } else {
            serializerRecord = serializerRecordSet.get(o.getClass());
        }
        if (serializerRecord == null) {
            throw new IllegalStateException("Class not registered: " + o.getClass().getName());
        }
        buffer.putShort((short) serializerRecord.getId());
        return (Serializer<T>) serializerRecord.getElement();
    }

    @Override
    public Serializer<?> readType(ByteBuffer buffer) {
        int id = buffer.getShort();
        if (id < 0 || id >= serializerRecordSet.size()) {
            throw new SharkSerializationException("Unknown register id: " + id);
        }
        return serializerRecordSet.get(id).getElement();
    }

    public void setReferenceContext(ReferenceContext referenceContext) {
        this.referenceContext = referenceContext;
    }

    @Override
    public ReferenceContext getReferenceContext() {
        return referenceContext;
    }

    @Override
    public FieldSerializerFactory getFieldSerializerFactory() {
        return fieldSerializerFactory;
    }
}
