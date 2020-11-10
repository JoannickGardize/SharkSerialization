package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;

public class NullSerializer<T> implements Serializer<T> {

	@Override
	public void write(ByteBuffer buffer, T object) {
		// Nothing to write
	}

	@Override
	public T read(ByteBuffer buffer) {
		return null;
	}

}
