package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.sharkhendrix.serialization.SerializationContext;

public class DefaultsSerializers {

	public static void registerAll(SerializationContext serialization) {
		serialization.register(null, nullSerializer());
		serialization.register(Byte.class, byteSerializer());
		serialization.register(byte.class, byteSerializer());
		serialization.register(Short.class, shortSerializer());
		serialization.register(short.class, shortSerializer());
		serialization.register(Integer.class, intSerializer());
		serialization.register(int.class, intSerializer());
		serialization.register(Long.class, longSerializer());
		serialization.register(long.class, longSerializer());
		serialization.register(Float.class, floatSerializer());
		serialization.register(float.class, floatSerializer());
		serialization.register(Double.class, doubleSerializer());
		serialization.register(double.class, doubleSerializer());
		serialization.register(String.class, stringSerializer());
	}

	public static Serializer<Object> nullSerializer() {
		return new Serializer<Object>() {

			@Override
			public void write(ByteBuffer buffer, Object object) {
				// Nothing to write
			}

			@Override
			public Object read(ByteBuffer buffer) {
				return null;
			}
		};
	}

	public static Serializer<Byte> byteSerializer() {
		return new Serializer<Byte>() {

			@Override
			public void write(ByteBuffer buffer, Byte object) {
				buffer.put(object);
			}

			@Override
			public Byte read(ByteBuffer buffer) {
				return buffer.get();
			}
		};
	}

	public static Serializer<Short> shortSerializer() {
		return new Serializer<Short>() {

			@Override
			public void write(ByteBuffer buffer, Short object) {
				buffer.putShort(object);
			}

			@Override
			public Short read(ByteBuffer buffer) {
				return buffer.getShort();
			}
		};
	}

	public static Serializer<Integer> intSerializer() {
		return new Serializer<Integer>() {

			@Override
			public void write(ByteBuffer buffer, Integer object) {
				buffer.putInt(object);
			}

			@Override
			public Integer read(ByteBuffer buffer) {
				return buffer.getInt();
			}
		};
	}

	public static Serializer<Long> longSerializer() {
		return new Serializer<Long>() {

			@Override
			public void write(ByteBuffer buffer, Long object) {
				buffer.putLong(object);
			}

			@Override
			public Long read(ByteBuffer buffer) {
				return buffer.getLong();
			}
		};
	}

	public static Serializer<Float> floatSerializer() {
		return new Serializer<Float>() {

			@Override
			public void write(ByteBuffer buffer, Float object) {
				buffer.putFloat(object);
			}

			@Override
			public Float read(ByteBuffer buffer) {
				return buffer.getFloat();
			}
		};
	}

	public static Serializer<Double> doubleSerializer() {
		return new Serializer<Double>() {

			@Override
			public void write(ByteBuffer buffer, Double object) {
				buffer.putDouble(object);
			}

			@Override
			public Double read(ByteBuffer buffer) {
				return buffer.getDouble();
			}
		};
	}

	public static Serializer<String> stringSerializer() {
		return new Serializer<String>() {

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
		};

	}
}
