package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.function.IntFunction;

import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.util.VarLenNumberIO;

public class CollectionSerializer<T extends Collection<Object>> implements Serializer<T> {

    private IntFunction<T> constructor;
    private Serializer<Object> elementSerializer;

    @SuppressWarnings("unchecked")
    public CollectionSerializer(IntFunction<T> constructor, Serializer<?> elementSerializer) {
        this.constructor = constructor;
        this.elementSerializer = (Serializer<Object>) elementSerializer;
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        int length = object.size();
        VarLenNumberIO.writePositiveVarInt(buffer, length);
        for (Object element : object) {
            elementSerializer.write(buffer, element);
        }
    }

    @Override
    public T read(ByteBuffer buffer) {
        int length = VarLenNumberIO.readPositiveVarInt(buffer);
        T object = constructor.apply(length);
        for (int i = 0; i < length; i++) {
            object.add(elementSerializer.read(buffer));
        }
        return object;
    }
}
