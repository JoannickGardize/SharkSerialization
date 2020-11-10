package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sharkhendrix.serialization.serializer.DefaultSerializers;
import com.sharkhendrix.serialization.serializer.Serializer;

public class SharkSerialization implements SerializationContext {

	private List<SerializerRecord<?>> serializersById = new ArrayList<>();
	private Map<Class<?>, SerializerRecord<?>> serializersByClass = new HashMap<>();

	public SharkSerialization() {
		DefaultSerializers.registerAll(this);
	}

	@Override
	public <T> void register(Class<T> type, Serializer<? super T> serializer) {
		SerializerRecord<? super T> record = new SerializerRecord<>(serializersById.size(), serializer);
		serializersById.add(record);
		serializersByClass.put(type, record);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initialize() {
		serializersByClass.forEach((k, v) -> ((Serializer) v.getSerializer()).initialize(k, this));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Serializer<? super T> getSerializer(Class<T> type) {
		return (Serializer<? super T>) serializersByClass.get(type).getSerializer();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void write(ByteBuffer buffer, Object o) {
		SerializerRecord record;
		if (o == null) {
			record = serializersByClass.get(null);
		} else {
			record = serializersByClass.get(o.getClass());
		}
		if (record == null) {
			throw new IllegalStateException("Class not registered: " + o.getClass().getName());
		}
		buffer.putShort((short) record.getId());
		record.getSerializer().write(buffer, o);
	}

	@Override
	public Object read(ByteBuffer buffer) {
		int id = buffer.getShort();
		if (id < 0 || id >= serializersById.size()) {
			throw new IllegalStateException("Unknown register id: " + id);
		}
		return serializersById.get(id).getSerializer().read(buffer);
	}
}
