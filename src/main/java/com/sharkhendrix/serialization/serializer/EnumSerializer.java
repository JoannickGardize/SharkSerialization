package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.Serializer;

/**
 * Serializer of enum types. Not permissive to constants declaration order
 * changes.
 * 
 * @author Joannick Gardize
 *
 * @param <T> the enum type
 */
public class EnumSerializer<T extends Enum<T>> implements Serializer<T> {

    private Class<T> type;

    public EnumSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        buffer.put((byte) object.ordinal());
    }

    @Override
    public T read(ByteBuffer buffer) {
        return type.getEnumConstants()[buffer.get()];
    }
}
