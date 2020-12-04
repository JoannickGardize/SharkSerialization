package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.Serializer;
import com.sharkhendrix.util.VarLenNumberIO;

public class DefaultSerializers {

    private DefaultSerializers() {
    }

    public static void registerAll(SerializationContext serialization) {
        serialization.register(null, new NullSerializer());
        serialization.register(Byte.class, new PrimitiveWrapperSerializers.ByteSerializer());
        serialization.register(Character.class, new PrimitiveWrapperSerializers.CharSerializer());
        serialization.register(Boolean.class, new PrimitiveWrapperSerializers.BooleanSerializer());
        serialization.register(Short.class, new PrimitiveWrapperSerializers.ShortSerializer());
        serialization.register(Integer.class, new PrimitiveWrapperSerializers.IntNormalSerializer());
        serialization.register(Long.class, new PrimitiveWrapperSerializers.LongNormalSerializer());
        serialization.register(Float.class, new PrimitiveWrapperSerializers.FloatSerializer());
        serialization.register(Double.class, new PrimitiveWrapperSerializers.DoubleSerializer());
        serialization.register(String.class, new StringSerializer());
        serialization.register(byte[].class, new PrimitiveArraySerializers.ByteArraySerializer());
        serialization.register(char[].class, new PrimitiveArraySerializers.CharArraySerializer());
        serialization.register(short[].class, new PrimitiveArraySerializers.ShortArraySerializer());
        serialization.register(boolean[].class, new PrimitiveArraySerializers.BooleanArraySerializer());
        serialization.register(int[].class, new PrimitiveArraySerializers.IntNormalArraySerializer());
        serialization.register(int[].class, new PrimitiveArraySerializers.IntPositiveArraySerializer());
        serialization.register(int[].class, new PrimitiveArraySerializers.IntNoneArraySerializer());
        serialization.register(long[].class, new PrimitiveArraySerializers.LongNormalArraySerializer());
        serialization.register(long[].class, new PrimitiveArraySerializers.LongPositiveArraySerializer());
        serialization.register(long[].class, new PrimitiveArraySerializers.LongNoneArraySerializer());
        serialization.register(float[].class, new PrimitiveArraySerializers.FloatArraySerializer());
        serialization.register(double[].class, new PrimitiveArraySerializers.DoubleArraySerializer());
        serialization.getFieldSerializerFactory().registerSizeableConstructor(Set.class, HashSet::new);
        serialization.getFieldSerializerFactory().registerSizeableConstructor(List.class, ArrayList::new);
        serialization.getFieldSerializerFactory().registerSizeableConstructor(Map.class, HashMap::new);
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

    public static class StringSerializer implements Serializer<String> {

        @Override
        public void write(ByteBuffer buffer, String object) {
            VarLenNumberIO.writePositiveVarInt(buffer, object.length());
            buffer.put(object.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String read(ByteBuffer buffer) {
            byte[] data = new byte[VarLenNumberIO.readPositiveVarInt(buffer)];
            buffer.get(data);
            return new String(data, StandardCharsets.UTF_8);
        }

    }
}
