package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.Serializer;

public class PrimitiveArraySerializers {

    private PrimitiveArraySerializers() {
    }

    public static class ByteArraySerializer implements Serializer<byte[]> {

        @Override
        public void write(ByteBuffer buffer, byte[] object) {
            int length = object.length;
            buffer.putInt(length);
            for (int i = 0; i < length; i++) {
                buffer.put(object[i]);
            }
        }

        @Override
        public byte[] read(ByteBuffer buffer) {
            int length = buffer.getInt();
            byte[] object = new byte[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.get();
            }
            return object;
        }
    }

    public static class CharArraySerializer implements Serializer<char[]> {

        @Override
        public void write(ByteBuffer buffer, char[] object) {
            int length = object.length;
            buffer.putInt(length);
            for (int i = 0; i < length; i++) {
                buffer.putChar(object[i]);
            }
        }

        @Override
        public char[] read(ByteBuffer buffer) {
            int length = buffer.getInt();
            char[] object = new char[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getChar();
            }
            return object;
        }
    }

    public static class BooleanArraySerializer implements Serializer<boolean[]> {

        @Override
        public void write(ByteBuffer buffer, boolean[] object) {
            int length = object.length;
            buffer.putInt(length);
            for (int i = 0; i < length; i++) {
                buffer.put(object[i] ? (byte) 1 : (byte) 0);
            }
        }

        @Override
        public boolean[] read(ByteBuffer buffer) {
            int length = buffer.getInt();
            boolean[] object = new boolean[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.get() == (byte) 1;
            }
            return object;
        }
    }

    public static class ShortArraySerializer implements Serializer<short[]> {

        @Override
        public void write(ByteBuffer buffer, short[] object) {
            int length = object.length;
            buffer.putInt(length);
            for (int i = 0; i < length; i++) {
                buffer.putShort(object[i]);
            }
        }

        @Override
        public short[] read(ByteBuffer buffer) {
            int length = buffer.getInt();
            short[] object = new short[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getShort();
            }
            return object;
        }
    }

    public static class IntArraySerializer implements Serializer<int[]> {

        @Override
        public void write(ByteBuffer buffer, int[] object) {
            int length = object.length;
            buffer.putInt(length);
            for (int i = 0; i < length; i++) {
                buffer.putInt(object[i]);
            }
        }

        @Override
        public int[] read(ByteBuffer buffer) {
            int length = buffer.getInt();
            int[] object = new int[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getInt();
            }
            return object;
        }
    }

    public static class LongArraySerializer implements Serializer<long[]> {

        @Override
        public void write(ByteBuffer buffer, long[] object) {
            int length = object.length;
            buffer.putInt(length);
            for (int i = 0; i < length; i++) {
                buffer.putLong(object[i]);
            }
        }

        @Override
        public long[] read(ByteBuffer buffer) {
            int length = buffer.getInt();
            long[] object = new long[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getLong();
            }
            return object;
        }
    }

    public static class FloatArraySerializer implements Serializer<float[]> {

        @Override
        public void write(ByteBuffer buffer, float[] object) {
            int length = object.length;
            buffer.putInt(length);
            for (int i = 0; i < length; i++) {
                buffer.putFloat(object[i]);
            }
        }

        @Override
        public float[] read(ByteBuffer buffer) {
            int length = buffer.getInt();
            float[] object = new float[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getFloat();
            }
            return object;
        }
    }

    public static class DoubleArraySerializer implements Serializer<double[]> {

        @Override
        public void write(ByteBuffer buffer, double[] object) {
            int length = object.length;
            buffer.putInt(length);
            for (int i = 0; i < length; i++) {
                buffer.putDouble(object[i]);
            }
        }

        @Override
        public double[] read(ByteBuffer buffer) {
            int length = buffer.getInt();
            double[] object = new double[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getDouble();
            }
            return object;
        }
    }
}
