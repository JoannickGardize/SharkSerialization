package com.sharkhendrix.serialization.serializer.field;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.Serializer;

public class ArrayFieldAccessor extends FieldAccessor {

    @SuppressWarnings("rawtypes")
    private Serializer componentSerializer;
    private Class<?> arrayType;

    public ArrayFieldAccessor(Field field, Serializer<?> componentSerializer) {
        super(field);
        this.componentSerializer = componentSerializer;
        arrayType = field.getType().getComponentType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
        Object value = field.get(object);
        int length = Array.getLength(value);
        buffer.putInt(length);
        for (int i = 0; i < length; i++) {
            componentSerializer.write(buffer, Array.get(object, i));
        }
    }

    @Override
    public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
        int length = buffer.getInt();
        Object array = Array.newInstance(arrayType, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, componentSerializer.read(buffer));
        }
        field.set(object, array);
    }

}
