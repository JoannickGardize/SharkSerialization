package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.function.IntFunction;

import com.sharkhendrix.serialization.Serializer;

public class ArrayInstanceSerializer<T> implements Serializer<T> {

    private IntFunction<T> newInstanceFunction;

    public ArrayInstanceSerializer(IntFunction<T> newInstanceFunction) {
        this.newInstanceFunction = newInstanceFunction;
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        buffer.putInt(Array.getLength(object));
    }

    @Override
    public T read(ByteBuffer buffer) {
        return newInstanceFunction.apply(buffer.getInt());
    }

}
