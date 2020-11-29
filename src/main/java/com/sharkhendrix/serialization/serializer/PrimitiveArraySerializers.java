package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.serialization.annotation.VarLenStrategy;
import com.sharkhendrix.util.VarLenNumberIO;

public class PrimitiveArraySerializers {

    private static Map<VarLenTypeKey, Serializer<?>> serializers = new HashMap<>();

    static {
        serializers.put(new VarLenTypeKey(byte[].class, VarLenStrategy.NONE), new ByteArraySerializer());
        serializers.put(new VarLenTypeKey(char[].class, VarLenStrategy.NONE), new CharArraySerializer());
        serializers.put(new VarLenTypeKey(short[].class, VarLenStrategy.NONE), new ShortArraySerializer());
        serializers.put(new VarLenTypeKey(boolean[].class, VarLenStrategy.NONE), new BooleanArraySerializer());
        serializers.put(new VarLenTypeKey(int[].class, VarLenStrategy.NORMAL), new IntNormalArraySerializer());
        serializers.put(new VarLenTypeKey(int[].class, VarLenStrategy.POSITIVE), new IntPositiveArraySerializer());
        serializers.put(new VarLenTypeKey(int[].class, VarLenStrategy.NONE), new IntNoneArraySerializer());
        serializers.put(new VarLenTypeKey(long[].class, VarLenStrategy.NORMAL), new LongNormalArraySerializer());
        serializers.put(new VarLenTypeKey(long[].class, VarLenStrategy.POSITIVE), new LongPositiveArraySerializer());
        serializers.put(new VarLenTypeKey(long[].class, VarLenStrategy.NONE), new LongNoneArraySerializer());
        serializers.put(new VarLenTypeKey(float[].class, VarLenStrategy.NONE), new FloatArraySerializer());
        serializers.put(new VarLenTypeKey(double[].class, VarLenStrategy.NONE), new DoubleArraySerializer());
    }

    private PrimitiveArraySerializers() {
    }

    public static Serializer<?> get(Class<?> arrayType, VarLenStrategy varLenStrategy) {
        Serializer<?> result = serializers.get(new VarLenTypeKey(arrayType, varLenStrategy));
        if (result == null) {
            result = serializers.get(new VarLenTypeKey(arrayType, VarLenStrategy.NONE));
        }
        return result;
    }

    public static class ByteArraySerializer implements Serializer<byte[]> {

        @Override
        public void write(ByteBuffer buffer, byte[] object) {
            int length = object.length;
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                buffer.put(object[i]);
            }
        }

        @Override
        public byte[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
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
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                buffer.putChar(object[i]);
            }
        }

        @Override
        public char[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
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
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                buffer.put(object[i] ? (byte) 1 : (byte) 0);
            }
        }

        @Override
        public boolean[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
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
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                buffer.putShort(object[i]);
            }
        }

        @Override
        public short[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
            short[] object = new short[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getShort();
            }
            return object;
        }
    }

    public static class IntNormalArraySerializer implements Serializer<int[]> {

        @Override
        public void write(ByteBuffer buffer, int[] object) {
            int length = object.length;
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                VarLenNumberIO.writeVarInt(buffer, object[i]);
            }
        }

        @Override
        public int[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
            int[] object = new int[length];
            for (int i = 0; i < length; i++) {
                object[i] = VarLenNumberIO.readVarInt(buffer);
            }
            return object;
        }
    }

    public static class IntPositiveArraySerializer implements Serializer<int[]> {

        @Override
        public void write(ByteBuffer buffer, int[] object) {
            int length = object.length;
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                VarLenNumberIO.writePositiveVarInt(buffer, object[i]);
            }
        }

        @Override
        public int[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
            int[] object = new int[length];
            for (int i = 0; i < length; i++) {
                object[i] = VarLenNumberIO.readPositiveVarInt(buffer);
            }
            return object;
        }
    }

    public static class IntNoneArraySerializer implements Serializer<int[]> {

        @Override
        public void write(ByteBuffer buffer, int[] object) {
            int length = object.length;
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                buffer.putInt(object[i]);
            }
        }

        @Override
        public int[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
            int[] object = new int[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getInt();
            }
            return object;
        }
    }

    public static class LongNormalArraySerializer implements Serializer<long[]> {

        @Override
        public void write(ByteBuffer buffer, long[] object) {
            int length = object.length;
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                VarLenNumberIO.writeVarLong(buffer, object[i]);
            }
        }

        @Override
        public long[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
            long[] object = new long[length];
            for (int i = 0; i < length; i++) {
                object[i] = VarLenNumberIO.readVarLong(buffer);
            }
            return object;
        }
    }

    public static class LongPositiveArraySerializer implements Serializer<long[]> {

        @Override
        public void write(ByteBuffer buffer, long[] object) {
            int length = object.length;
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                VarLenNumberIO.writePositiveVarLong(buffer, object[i]);
            }
        }

        @Override
        public long[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
            long[] object = new long[length];
            for (int i = 0; i < length; i++) {
                object[i] = VarLenNumberIO.readPositiveVarLong(buffer);
            }
            return object;
        }
    }

    public static class LongNoneArraySerializer implements Serializer<long[]> {

        @Override
        public void write(ByteBuffer buffer, long[] object) {
            int length = object.length;
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                buffer.putLong(object[i]);
            }
        }

        @Override
        public long[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
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
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                buffer.putFloat(object[i]);
            }
        }

        @Override
        public float[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
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
            VarLenNumberIO.writePositiveVarInt(buffer, length);
            for (int i = 0; i < length; i++) {
                buffer.putDouble(object[i]);
            }
        }

        @Override
        public double[] read(ByteBuffer buffer) {
            int length = VarLenNumberIO.readPositiveVarInt(buffer);
            double[] object = new double[length];
            for (int i = 0; i < length; i++) {
                object[i] = buffer.getDouble();
            }
            return object;
        }
    }
}
