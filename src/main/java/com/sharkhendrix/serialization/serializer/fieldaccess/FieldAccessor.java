package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.nio.ByteBuffer;

public interface FieldAccessor {

    void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException;

    void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException;
}
