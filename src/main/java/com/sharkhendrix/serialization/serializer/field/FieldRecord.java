package com.sharkhendrix.serialization.serializer.field;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public abstract class FieldRecord {

    protected Field field;

    public FieldRecord(Field field) {
        this.field = field;
        field.setAccessible(true);
    }

    public abstract void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException;

    public abstract void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException;
}
