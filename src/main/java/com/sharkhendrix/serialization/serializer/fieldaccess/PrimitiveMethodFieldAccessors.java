package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.nio.ByteBuffer;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.sharkhendrix.serialization.util.function.ObjBooleanConsumer;
import com.sharkhendrix.serialization.util.function.ObjByteConsumer;
import com.sharkhendrix.serialization.util.function.ObjCharConsumer;
import com.sharkhendrix.serialization.util.function.ObjFloatConsumer;
import com.sharkhendrix.serialization.util.function.ObjShortConsumer;
import com.sharkhendrix.serialization.util.function.ToByteFunction;
import com.sharkhendrix.serialization.util.function.ToCharFunction;
import com.sharkhendrix.serialization.util.function.ToFloatFunction;
import com.sharkhendrix.serialization.util.function.ToShortFunction;

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

    public static <T> FieldAccessor booleanFieldAccessor(Predicate<T> getter, ObjBooleanConsumer<T> setter) {
        return new FieldAccessor() {

            @SuppressWarnings("unchecked")
            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.put(getter.test((T) object) ? (byte) 1 : (byte) 0);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                setter.accept((T) object, buffer.get() == (byte) 1);
            }
        };
    }

    public static <T> FieldAccessor shortFieldAccessor(ToShortFunction<T> getter, ObjShortConsumer<T> setter) {
        return new FieldAccessor() {

            @SuppressWarnings("unchecked")
            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putShort(getter.apply((T) object));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                setter.accept((T) object, buffer.getShort());
            }
        };
    }

    public static <T> FieldAccessor intFieldAccessor(ToIntFunction<T> getter, ObjIntConsumer<T> setter) {
        return new FieldAccessor() {

            @SuppressWarnings("unchecked")
            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putInt(getter.applyAsInt((T) object));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                setter.accept((T) object, buffer.getInt());
            }
        };
    }

    public static <T> FieldAccessor longFieldAccessor(ToLongFunction<T> getter, ObjLongConsumer<T> setter) {
        return new FieldAccessor() {

            @SuppressWarnings("unchecked")
            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putLong(getter.applyAsLong((T) object));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                setter.accept((T) object, buffer.getLong());
            }
        };
    }

    public static <T> FieldAccessor floatFieldAccessor(ToFloatFunction<T> getter, ObjFloatConsumer<T> setter) {
        return new FieldAccessor() {

            @SuppressWarnings("unchecked")
            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putFloat(getter.apply((T) object));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                setter.accept((T) object, buffer.getFloat());
            }
        };
    }

    public static <T> FieldAccessor doubleFieldAccessor(ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter) {
        return new FieldAccessor() {

            @SuppressWarnings("unchecked")
            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putDouble(getter.applyAsDouble((T) object));
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                setter.accept((T) object, buffer.getDouble());
            }
        };
    }
}
