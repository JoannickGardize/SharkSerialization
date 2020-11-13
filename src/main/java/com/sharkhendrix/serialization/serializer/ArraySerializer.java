package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.Serializer;

public class ArraySerializer<T> implements Serializer<T> {

    private Serializer<T> arrayInstanceSerializer;
    private Serializer<Object> elementSerializer;

    @SuppressWarnings("unchecked")
    public ArraySerializer(Serializer<T> arrayInstanceSerializer, Serializer<?> elementSerializer) {
        this.arrayInstanceSerializer = arrayInstanceSerializer;
        this.elementSerializer = (Serializer<Object>) elementSerializer;
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        arrayInstanceSerializer.write(buffer, object);
        int length = Array.getLength(object);
        for (int i = 0; i < length; i++) {
            elementSerializer.write(buffer, Array.get(object, i));
        }
    }

    @Override
    public T read(ByteBuffer buffer) {
        T object = arrayInstanceSerializer.read(buffer);
        int length = Array.getLength(object);
        for (int i = 0; i < length; i++) {
            Array.set(object, i, elementSerializer.read(buffer));
        }
        return object;
    }

}
