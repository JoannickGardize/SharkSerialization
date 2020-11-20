package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.serializer.factory.SerializerFactory;

public interface SerializationContext {

    <T> void register(Class<T> type, Serializer<? extends T> serializer);

    <T> Serializer<? extends T> getSerializer(Class<T> type);

    <T> Serializer<T> writeType(ByteBuffer buffer, T o);

    Serializer<?> readType(ByteBuffer buffer);

    ReferenceContext getReferenceContext();

    SerializerFactory getSerializerFactory();

    /**
     * Write the full graph of the object o. Keeps the actual reference context.
     * 
     * @param buffer
     * @param o
     */
    default void writeObject(ByteBuffer buffer, Object o) {
        writeType(buffer, o).write(buffer, o);
    }

    /**
     * <p>
     * Read the full graph of the object in this buffer. Keeps the actual reference
     * context.
     * 
     * @param buffer
     * @return
     */
    default Object readObject(ByteBuffer buffer) {
        return readType(buffer).read(buffer);
    }

    /**
     * <p>
     * Write the full graph of the object o with a new reference context.
     * <p>
     * 
     * @param buffer
     * @param o
     */
    default void write(ByteBuffer buffer, Object o) {
        getReferenceContext().resetWriteContext();
        writeObject(buffer, o);
    }

    /**
     * <p>
     * Read the full graph of the object in this buffer with a new reference
     * context.
     * <p>
     * 
     * @param buffer
     * @return
     */
    default Object read(ByteBuffer buffer) {
        getReferenceContext().resetReadContext();
        return readObject(buffer);
    }
}
