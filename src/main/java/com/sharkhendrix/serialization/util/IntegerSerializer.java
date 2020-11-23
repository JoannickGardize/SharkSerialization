package com.sharkhendrix.serialization.util;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.Serializer;

/**
 * Primitive integer version of the {@link Serializer} interface
 * 
 * @author Joannick
 *
 */
public interface IntegerSerializer {

    void write(ByteBuffer buffer, int value);

    int read(ByteBuffer buffer);
}
