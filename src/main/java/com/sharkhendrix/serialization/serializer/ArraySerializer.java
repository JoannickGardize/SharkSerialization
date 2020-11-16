package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.function.IntFunction;

import com.sharkhendrix.serialization.Serializer;

public class ArraySerializer<T> implements Serializer<T> {

    private IntFunction<T> constructor;
    private Serializer<Object> elementSerializer;

    @SuppressWarnings("unchecked")
    public ArraySerializer(IntFunction<T> constructor, Serializer<?> elementSerializer) {
        this.constructor = constructor;
        this.elementSerializer = (Serializer<Object>) elementSerializer;
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        int length = Array.getLength(object);
        buffer.putInt(length);
        for (int i = 0; i < length; i++) {
            elementSerializer.write(buffer, Array.get(object, i));
        }
    }

    @Override
    public T read(ByteBuffer buffer) {
        int length = buffer.getInt();
        T object = constructor.apply(length);
        for (int i = 0; i < length; i++) {
            Array.set(object, i, elementSerializer.read(buffer));
        }
        return object;
    }

}
