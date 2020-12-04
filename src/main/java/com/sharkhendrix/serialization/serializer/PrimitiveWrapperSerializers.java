package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.annotation.VarLenStrategy;
import com.sharkhendrix.util.VarLenNumberIO;
import com.sharkhendrix.util.VarLenTypeKey;

public class PrimitiveWrapperSerializers {

    private static Map<VarLenTypeKey, Serializer<?>> serializers = new HashMap<>();

    static {
        serializers.put(new VarLenTypeKey(Byte.class, VarLenStrategy.NONE), new ByteSerializer());
        serializers.put(new VarLenTypeKey(Character.class, VarLenStrategy.NONE), new CharSerializer());
        serializers.put(new VarLenTypeKey(Short.class, VarLenStrategy.NONE), new ShortSerializer());
        serializers.put(new VarLenTypeKey(Boolean.class, VarLenStrategy.NONE), new BooleanSerializer());
        serializers.put(new VarLenTypeKey(Integer.class, VarLenStrategy.NORMAL), new IntNormalSerializer());
        serializers.put(new VarLenTypeKey(Integer.class, VarLenStrategy.POSITIVE), new IntPositiveSerializer());
        serializers.put(new VarLenTypeKey(Integer.class, VarLenStrategy.NONE), new IntNoneSerializer());
        serializers.put(new VarLenTypeKey(Long.class, VarLenStrategy.NORMAL), new LongNormalSerializer());
        serializers.put(new VarLenTypeKey(Long.class, VarLenStrategy.POSITIVE), new LongPositiveSerializer());
        serializers.put(new VarLenTypeKey(Long.class, VarLenStrategy.NONE), new LongNoneSerializer());
        serializers.put(new VarLenTypeKey(Float.class, VarLenStrategy.NONE), new FloatSerializer());
        serializers.put(new VarLenTypeKey(Double.class, VarLenStrategy.NONE), new DoubleSerializer());
    }

    private PrimitiveWrapperSerializers() {
    }

    public static Serializer<?> get(Class<?> wrapperType, VarLenStrategy varLenStrategy) {
        Serializer<?> result = serializers.get(new VarLenTypeKey(wrapperType, varLenStrategy));
        if (result == null) {
            result = serializers.get(new VarLenTypeKey(wrapperType, VarLenStrategy.NONE));
        }
        return result;
    }

    public static class ByteSerializer implements Serializer<Byte> {

        @Override
        public void write(ByteBuffer buffer, Byte object) {
            buffer.put(object);
        }

        @Override
        public Byte read(ByteBuffer buffer) {
            return buffer.get();
        }
    }

    public static class CharSerializer implements Serializer<Character> {

        @Override
        public void write(ByteBuffer buffer, Character object) {
            buffer.putChar(object);
        }

        @Override
        public Character read(ByteBuffer buffer) {
            return buffer.getChar();
        }
    }

    public static class BooleanSerializer implements Serializer<Boolean> {

        @Override
        public void write(ByteBuffer buffer, Boolean object) {
            buffer.put(object == true ? (byte) 1 : (byte) 0);
        }

        @Override
        public Boolean read(ByteBuffer buffer) {
            return buffer.get() == (byte) 1;
        }

    }

    public static class ShortSerializer implements Serializer<Short> {

        @Override
        public void write(ByteBuffer buffer, Short object) {
            buffer.putShort(object);
        }

        @Override
        public Short read(ByteBuffer buffer) {
            return buffer.getShort();
        }
    }

    public static class IntNormalSerializer implements Serializer<Integer> {

        @Override
        public void write(ByteBuffer buffer, Integer object) {
            VarLenNumberIO.writeVarInt(buffer, object);
        }

        @Override
        public Integer read(ByteBuffer buffer) {
            return VarLenNumberIO.readVarInt(buffer);
        }
    }

    public static class IntPositiveSerializer implements Serializer<Integer> {

        @Override
        public void write(ByteBuffer buffer, Integer object) {
            VarLenNumberIO.writePositiveVarInt(buffer, object);
        }

        @Override
        public Integer read(ByteBuffer buffer) {
            return VarLenNumberIO.readPositiveVarInt(buffer);
        }
    }

    public static class IntNoneSerializer implements Serializer<Integer> {

        @Override
        public void write(ByteBuffer buffer, Integer object) {
            buffer.putInt(object);
        }

        @Override
        public Integer read(ByteBuffer buffer) {
            return buffer.getInt();
        }
    }

    public static class LongNormalSerializer implements Serializer<Long> {

        @Override
        public void write(ByteBuffer buffer, Long object) {
            VarLenNumberIO.writeVarLong(buffer, object);
        }

        @Override
        public Long read(ByteBuffer buffer) {
            return VarLenNumberIO.readVarLong(buffer);
        }
    }

    public static class LongPositiveSerializer implements Serializer<Long> {

        @Override
        public void write(ByteBuffer buffer, Long object) {
            VarLenNumberIO.writePositiveVarLong(buffer, object);
        }

        @Override
        public Long read(ByteBuffer buffer) {
            return VarLenNumberIO.readPositiveVarLong(buffer);
        }
    }

    public static class LongNoneSerializer implements Serializer<Long> {

        @Override
        public void write(ByteBuffer buffer, Long object) {
            buffer.putLong(object);
        }

        @Override
        public Long read(ByteBuffer buffer) {
            return buffer.getLong();
        }
    }

    public static class FloatSerializer implements Serializer<Float> {

        @Override
        public void write(ByteBuffer buffer, Float object) {
            buffer.putFloat(object);
        }

        @Override
        public Float read(ByteBuffer buffer) {
            return buffer.getFloat();
        }
    }

    public static class DoubleSerializer implements Serializer<Double> {

        @Override
        public void write(ByteBuffer buffer, Double object) {
            buffer.putDouble(object);
        }

        @Override
        public Double read(ByteBuffer buffer) {
            return buffer.getDouble();
        }
    }
}
