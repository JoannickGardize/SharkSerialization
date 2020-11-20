package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public abstract class FieldAccessor {

    protected Field field;

    public FieldAccessor(Field field) {
        this.field = field;
        field.setAccessible(true);
    }

    public abstract void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException;

    public abstract void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException;
}
