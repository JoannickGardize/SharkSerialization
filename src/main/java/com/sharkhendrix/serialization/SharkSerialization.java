package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.serializer.DefaultSerializers;
import com.sharkhendrix.serialization.serializer.ObjectSerializer;
import com.sharkhendrix.serialization.serializer.factory.SerializerFactory;
import com.sharkhendrix.serialization.serializer.factory.SerializerFactoryDefaultConfigurator;
import com.sharkhendrix.serialization.util.Record;
import com.sharkhendrix.serialization.util.RecordSet;

public class SharkSerialization implements SerializationContext {

    private ReferenceContext referenceContext = new IdentityMapReferenceContext();

    private RecordSet<Class<?>, Serializer<?>> serializerRecordSet = new RecordSet<>();

    private Record<Serializer<?>> nullSerializerRecord;

    private SerializerFactory serializerFactory = new SerializerFactory();

    public SharkSerialization() {
        DefaultSerializers.registerAll(this);
        nullSerializerRecord = serializerRecordSet.get(null);
        new SerializerFactoryDefaultConfigurator(this).configure();
    }

    /**
     * Convenient method to register a class for "object" serialization.
     * 
     * @param <T>         the class type
     * @param type        the class of the object
     * @param constructor the no-args constructor method for the class
     */
    public <T> void registerObject(Class<T> type, Supplier<? extends T> constructor) {
        register(type, new ObjectSerializer<>(type, constructor));
    }

    /**
     * Convenient method to register a constructor for special use. By default,
     * according to the {@link SerializerFactoryDefaultConfigurator}, this is the
     * case for all non-primitive arrays, Lists, Sets and Maps.
     * 
     * @param <T>         the class type
     * @param type        the class binded with the constructor
     * @param constructor the constructor method for the class, the int argument may
     *                    be used for sizing arrays, collections and maps.
     */
    public <T> void registerConstructor(Class<T> type, IntFunction<? extends T> constructor) {
        serializerFactory.registerSizeableConstructor(type, constructor);
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
            throw new SharkSerializationException("Class not registered: " + o.getClass().getName());
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
    public SerializerFactory getSerializerFactory() {
        return serializerFactory;
    }
}
