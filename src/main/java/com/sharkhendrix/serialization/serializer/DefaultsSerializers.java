package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;

public class DefaultsSerializers {

    private DefaultsSerializers() {
    }

    public static void registerAll(SerializationContext serialization) {
        serialization.register(null, new NullSerializer());
        serialization.register(Byte.class, new ByteSerializer());
        serialization.register(Character.class, new CharSerializer());
        serialization.register(Boolean.class, new BooleanSerializer());
        serialization.register(Short.class, new ShortSerializer());
        serialization.register(Integer.class, new IntSerializer());
        serialization.register(Long.class, new LongSerializer());
        serialization.register(Float.class, new FloatSerializer());
        serialization.register(Double.class, new DoubleSerializer());
        serialization.register(String.class, new StringSerializer());
        serialization.register(byte[].class, new PrimitiveArraySerializers.ByteArraySerializer());
        serialization.register(char[].class, new PrimitiveArraySerializers.CharArraySerializer());
        serialization.register(boolean[].class, new PrimitiveArraySerializers.BooleanArraySerializer());
        serialization.register(short[].class, new PrimitiveArraySerializers.ShortArraySerializer());
        serialization.register(int[].class, new PrimitiveArraySerializers.IntArraySerializer());
        serialization.register(long[].class, new PrimitiveArraySerializers.LongArraySerializer());
        serialization.register(float[].class, new PrimitiveArraySerializers.FloatArraySerializer());
        serialization.register(double[].class, new PrimitiveArraySerializers.DoubleArraySerializer());
    }

    public static class NullSerializer implements Serializer<Object> {

        @Override
        public void write(ByteBuffer buffer, Object object) {
            // Nothing to write
        }

        @Override
        public Object read(ByteBuffer buffer) {
            return null;
        }
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

    public static class IntSerializer implements Serializer<Integer> {

        @Override
        public void write(ByteBuffer buffer, Integer object) {
            buffer.putInt(object);
        }

        @Override
        public Integer read(ByteBuffer buffer) {
            return buffer.getInt();
        }
    }

    public static class LongSerializer implements Serializer<Long> {

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

    public static class StringSerializer implements Serializer<String> {

        @Override
        public void write(ByteBuffer buffer, String object) {
            buffer.putShort((short) object.length());
            buffer.put(object.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String read(ByteBuffer buffer) {
            byte[] data = new byte[buffer.getShort()];
            buffer.get(data);
            return new String(data, StandardCharsets.UTF_8);
        }

    }
}
