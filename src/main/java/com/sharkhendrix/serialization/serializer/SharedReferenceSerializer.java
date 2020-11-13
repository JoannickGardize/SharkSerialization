package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.ReferenceContext;
import com.sharkhendrix.serialization.Serializer;

public class SharedReferenceSerializer<T> implements Serializer<T> {

	private ReferenceContext referenceContext;
	private Serializer<T> serializer;

	public SharedReferenceSerializer(ReferenceContext referenceContext, Serializer<T> serializer) {
		this.referenceContext = referenceContext;
		this.serializer = serializer;
	}

	@Override
	public void write(ByteBuffer buffer, T object) {
		short id = referenceContext.retrieve(object);
		if (id == -1) {
			id = referenceContext.store(object);
			buffer.putShort((short) -id);
			serializer.write(buffer, object);
		} else {
			buffer.putShort(id);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public T read(ByteBuffer buffer) {
		short id = buffer.getShort();
		if (id < 0) {
			T object = serializer.read(buffer, o -> referenceContext.store((short) -id, o));
			return object;
		} else {
			return (T) referenceContext.retrieve(id);
		}
	}
}
