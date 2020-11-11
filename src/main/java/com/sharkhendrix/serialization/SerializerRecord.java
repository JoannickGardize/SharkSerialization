package com.sharkhendrix.serialization;

import com.sharkhendrix.serialization.serializer.Serializer;

class SerializerRecord<T> {

	private int id;
	private Serializer<T> serializer;

	public SerializerRecord(int id, Serializer<T> serializer) {
		this.id = id;
		this.serializer = serializer;
	}

	public int getId() {
		return id;
	}

	public Serializer<T> getSerializer() {
		return serializer;
	}

}
