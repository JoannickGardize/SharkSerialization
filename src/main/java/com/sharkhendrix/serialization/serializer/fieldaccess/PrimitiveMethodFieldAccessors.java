package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.util.function.ObjByteConsumer;
import com.sharkhendrix.serialization.util.function.ObjCharConsumer;
import com.sharkhendrix.serialization.util.function.ToByteFunction;
import com.sharkhendrix.serialization.util.function.ToCharFunction;

public class PrimitiveMethodFieldAccessors {

    private PrimitiveMethodFieldAccessors() {
    }

    public static <T> FieldAccessor byteFieldAccessor(ToByteFunction<T> getter, ObjByteConsumer<T> setter) {
        return new FieldAccessor() {

            @SuppressWarnings("unchecked")
            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.put(getter.apply((T) object));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                setter.accept((T) object, buffer.get());
            }
        };
    }

    public static <T> FieldAccessor charFieldAccessor(ToCharFunction<T> getter, ObjCharConsumer<T> setter) {
        return new FieldAccessor() {

            @SuppressWarnings("unchecked")
            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putChar(getter.apply((T) object));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                setter.accept((T) object, buffer.getChar());
            }
        };
    }
}
