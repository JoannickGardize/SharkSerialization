package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.sharkhendrix.serialization.Serializer;

// TODO Primitive method field
public class MethodFieldAccessor implements FieldAccessor {

    private Function<Object, Object> getter;
    private BiConsumer<Object, Object> setter;
    protected Serializer<Object> serializer;

    @SuppressWarnings("unchecked")
    public <T, U> MethodFieldAccessor(Function<T, U> getter, BiConsumer<T, U> setter, Serializer<U> serializer) {
        this.getter = (Function<Object, Object>) getter;
        this.setter = (BiConsumer<Object, Object>) setter;
        this.serializer = (Serializer<Object>) serializer;
    }

    @Override
    public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
        serializer.write(buffer, getter.apply(object));
    }

    @Override
    public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
        setter.accept(object, serializer.read(buffer));
    }
}