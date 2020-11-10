package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.SerializationContext;

public interface Serializer<T> {

	/**
	 * Initialize this serializer, this method is called by SharkSe
	 * 
	 * @param type
	 * @param serialization
	 */
	default void initialize(Class<T> type, SerializationContext context) {
		// Nothing by default
	}

	void write(ByteBuffer buffer, T object);

	T read(ByteBuffer buffer);
}
