package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.serializer.DefaultsSerializers;
import com.sharkhendrix.serialization.serializer.ObjectSerializer;
import com.sharkhendrix.serialization.serializer.Serializer;

public class SharkSerialization implements SerializationContext {

	private List<SerializerRecord<?>> serializersById = new ArrayList<>();
	private Map<Class<?>, SerializerRecord<?>> serializersByClass = new HashMap<>();

	private ReferenceContext referenceContext = new MapReferenceContext();

	public SharkSerialization() {
		DefaultsSerializers.registerAll(this);
	}

	public <T> void register(Class<T> type, Supplier<? extends T> newInstanceSupplier) {
		register(type, new ObjectSerializer<>(newInstanceSupplier));
	}

	@Override
	public <T> void register(Class<T> type, Serializer<? extends T> serializer) {
		SerializerRecord<? extends T> record = new SerializerRecord<>(serializersById.size(), serializer);
		serializersById.add(record);
		serializersByClass.put(type, record);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initialize() {
		serializersByClass.forEach((k, v) -> ((Serializer) v.getSerializer()).initialize(k, this));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Serializer<? extends T> getSerializer(Class<T> type) {
		return (Serializer<? extends T>) serializersByClass.get(type).getSerializer();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Serializer<T> writeType(ByteBuffer buffer, T o) {
		SerializerRecord<T> serializerRecord;
		if (o == null) {
			serializerRecord = (SerializerRecord<T>) serializersByClass.get(null);
		} else {
			serializerRecord = (SerializerRecord<T>) serializersByClass.get(o.getClass());
		}
		if (serializerRecord == null) {
			throw new IllegalStateException("Class not registered: " + o.getClass().getName());
		}
		buffer.putShort((short) serializerRecord.getId());
		return serializerRecord.getSerializer();
	}

	@Override
	public Serializer<?> readType(ByteBuffer buffer) {
		int id = buffer.getShort();
		if (id < 0 || id >= serializersById.size()) {
			throw new IllegalStateException("Unknown register id: " + id);
		}
		return serializersById.get(id).getSerializer();
	}

	public void setReferenceContext(ReferenceContext referenceContext) {
		this.referenceContext = referenceContext;
	}

	@Override
	public ReferenceContext getReferenceContext() {
		return referenceContext;
	}

}
