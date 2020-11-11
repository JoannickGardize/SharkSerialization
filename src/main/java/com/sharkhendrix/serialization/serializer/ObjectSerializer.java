package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.SerializationContext;
import com.sharkhendrix.serialization.SharedReference;
import com.sharkhendrix.serialization.UndefinedType;
import com.sharkhendrix.serialization.util.ReflectionUtils;

public class ObjectSerializer<T> implements Serializer<T> {

	private Supplier<? extends T> newInstanceSupplier;
	private FieldRecord[] fieldRecords;

	public ObjectSerializer(Supplier<? extends T> newInstanceSupplier) {
		this.newInstanceSupplier = newInstanceSupplier;
	}

	@Override
	public void initialize(Class<T> type, SerializationContext context) {
		if (!ReflectionUtils.isInstanciable(type)) {
			throw new IllegalArgumentException("Cannot initialize ObjectSerializer for the class " + type.getName() + ", it must be a regular class");
		}
		Class<?> currentClass = type;
		List<FieldRecord> fieldRecordList = new ArrayList<>();
		while (currentClass != Object.class) {
			registerFields(context, currentClass, fieldRecordList);
			currentClass = currentClass.getSuperclass();
		}
		fieldRecords = fieldRecordList.toArray(FieldRecord[]::new);
	}

	@Override
	public void write(ByteBuffer buffer, T object) {
		for (FieldRecord record : fieldRecords) {
			record.writeField(buffer, object);
		}
	}

	@Override
	public T read(ByteBuffer buffer, Consumer<T> intermediateConsumer) {
		T object = newInstanceSupplier.get();
		intermediateConsumer.accept(object);
		for (FieldRecord record : fieldRecords) {
			record.readField(buffer, object);
		}
		return object;
	}

	@Override
	public T read(ByteBuffer buffer) {
		return read(buffer, o -> {
		});
	}

	private void registerFields(SerializationContext context, Class<?> currentClass, List<FieldRecord> fieldRecordList) {
		for (Field field : currentClass.getDeclaredFields()) {
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}
			if (field.isAnnotationPresent(SharedReference.class)) {
				fieldRecordList.add(new FieldRecord(field, new SharedReferenceSerializer<>(context.getReferenceContext(), createSerializer(context, field))));
			} else {
				fieldRecordList.add(new FieldRecord(field, createSerializer(context, field)));
			}
		}
	}

	private Serializer<?> createSerializer(SerializationContext context, Field field) {
		if (field.isAnnotationPresent(UndefinedType.class)) {
			return new UndefinedTypeSerializer<>(context);
		} else {
			return context.getSerializer(field.getType());
		}
	}

}
