package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.serializer.Serializer;

public interface SerializationContext {

	<T> void register(Class<T> type, Serializer<? super T> serializer);

	<T> Serializer<? super T> getSerializer(Class<T> type);

	void write(ByteBuffer buffer, Object o);

	Object read(ByteBuffer buffer);
}
