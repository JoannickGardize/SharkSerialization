package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.ReferenceContext;
import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.util.VarLenNumberIO;

/**
 * Decorates a {@link Serializer} to keep in memory multiple references of the
 * same instance using a {@link ReferenceContext} and thus, avoid serializing
 * several times the same instance and restore the initial "reference graph".
 * 
 * This implementation uses the sign of the reference ID as a flag to announce
 * if this is the first time the reference is seen.
 * 
 * @author Joannick Gardize
 *
 * @param <T> the object type that this serializer is able to serialize /
 *            deserialize
 */
public class SharedReferenceSerializer<T> implements Serializer<T> {

    private ReferenceContext referenceContext;
    private Serializer<T> serializer;

    public SharedReferenceSerializer(ReferenceContext referenceContext, Serializer<T> serializer) {
        this.referenceContext = referenceContext;
        this.serializer = serializer;
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        int id = referenceContext.retrieve(object);
        if (id == -1) {
            id = referenceContext.store(object);
            VarLenNumberIO.writeVarInt(buffer, -id);
            serializer.write(buffer, object);
        } else {
            VarLenNumberIO.writeVarInt(buffer, id);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T read(ByteBuffer buffer) {
        int id = VarLenNumberIO.readVarInt(buffer);
        if (id < 0) {
            T object = serializer.read(buffer, o -> referenceContext.store(-id, o));
            return object;
        } else {
            return (T) referenceContext.retrieve(id);
        }
    }
}
