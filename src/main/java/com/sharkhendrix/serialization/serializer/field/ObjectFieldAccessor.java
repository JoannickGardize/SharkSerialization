package com.sharkhendrix.serialization.serializer.field;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.Serializer;

public class ObjectFieldAccessor extends FieldAccessor {
    @SuppressWarnings("rawtypes")
    protected Serializer serializer;

    public ObjectFieldAccessor(Field field, Serializer<?> serializer) {
        super(field);
        this.serializer = serializer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
        serializer.write(buffer, field.get(object));
    }

    @Override
    public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
        field.set(object, serializer.read(buffer));
    }
}
