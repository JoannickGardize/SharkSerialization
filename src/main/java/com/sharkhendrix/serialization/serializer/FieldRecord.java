package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.SharkSerializationException;

class FieldRecord {

	private Field field;
	@SuppressWarnings("rawtypes")
	private Serializer serializer;

	public FieldRecord(Field field, Serializer<?> serializer) {
		this.field = field;
		this.serializer = serializer;
		field.setAccessible(true);
	}

	@SuppressWarnings("unchecked")
	public void writeField(ByteBuffer buffer, Object object) {
		try {
			serializer.write(buffer, field.get(object));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new SharkSerializationException(e);
		}
	}

	public void readField(ByteBuffer buffer, Object object) {
		try {
			field.set(object, serializer.read(buffer));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new SharkSerializationException(e);
		}
	}
}
