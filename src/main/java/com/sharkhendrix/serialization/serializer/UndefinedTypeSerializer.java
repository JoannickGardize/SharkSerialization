package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import com.sharkhendrix.serialization.SerializationContext;

public class UndefinedTypeSerializer<T> implements Serializer<T> {

	private SerializationContext context;

	public UndefinedTypeSerializer(SerializationContext context) {
		this.context = context;
	}

	@Override
	public void write(ByteBuffer buffer, T object) {
		context.writeObject(buffer, object);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T read(ByteBuffer buffer) {
		return (T) context.readObject(buffer);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T read(ByteBuffer buffer, Consumer<T> intermediateConsumer) {
		return (T) context.readType(buffer).read(buffer, (Consumer) intermediateConsumer);
	}
}
