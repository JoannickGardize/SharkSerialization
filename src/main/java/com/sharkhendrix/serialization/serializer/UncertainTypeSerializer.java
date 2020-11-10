package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.SerializationContext;

public class UncertainTypeSerializer<T> implements Serializer<T> {

	private SerializationContext context;

	public UncertainTypeSerializer(SerializationContext context) {
		this.context = context;
	}

	@Override
	public void write(ByteBuffer buffer, T object) {
		context.write(buffer, object);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T read(ByteBuffer buffer) {
		return (T) context.read(buffer);
	}

}
