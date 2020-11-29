package com.sharkhendrix.util;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.Serializer;

/**
 * Primitive integer version of the {@link Serializer} interface, to avoid the
 * use of the Integer wrapper.
 * 
 * @author Joannick Gardize
 *
 */
public interface IntegerSerializer {

    void write(ByteBuffer buffer, int value);

    int read(ByteBuffer buffer);
}
