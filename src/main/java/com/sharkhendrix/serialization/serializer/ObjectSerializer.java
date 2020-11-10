package com.sharkhendrix.serialization.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.SerializationContext;
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
			throw new IllegalArgumentException("Cannot initialize ObjectSerializer for the class " + type.getName()
					+ ", it should be a normal and instanciable class");
		}
		Class<?> currentClass = type;
		List<FieldRecord> fieldRecordList = new ArrayList<>();
		while (currentClass != Object.class) {
			registerFields(context, currentClass, fieldRecordList);
			currentClass = currentClass.getSuperclass();
		}
		fieldRecords = fieldRecordList.toArray(FieldRecord[]::new);
	}

	private void registerFields(SerializationContext context, Class<?> currentClass,
			List<FieldRecord> fieldRecordList) {
		for (Field field : currentClass.getDeclaredFields()) {
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}
			if (field.isAnnotationPresent(UncertainType.class)) {
				fieldRecordList.add(new FieldRecord(field, new UncertainTypeSerializer<>(context)));
			} else {
				fieldRecordList.add(new FieldRecord(field, context.getSerializer(field.getType())));
			}
		}
	}

	@Override
	public void write(ByteBuffer buffer, T object) {
		for (FieldRecord record : fieldRecords) {
			record.writeField(buffer, object);
		}
	}

	@Override
	public T read(ByteBuffer buffer) {
		T object = newInstanceSupplier.get();
		for (FieldRecord record : fieldRecords) {
			record.readField(buffer, object);
		}
		return object;
	}

}
