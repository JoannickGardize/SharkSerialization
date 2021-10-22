package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import com.sharkhendrix.serialization.serializer.DefaultSerializers;
import com.sharkhendrix.serialization.serializer.EnumSerializer;
import com.sharkhendrix.serialization.serializer.ObjectSerializer;
import com.sharkhendrix.serialization.serializer.ObjectSerializerConfigurationHelper;
import com.sharkhendrix.serialization.serializer.factory.FieldSerializerFactory;
import com.sharkhendrix.serialization.serializer.factory.FieldSerializerFactoryDefaultConfigurator;
import com.sharkhendrix.util.Record;
import com.sharkhendrix.util.RecordSet;

public class SharkSerialization implements SerializationContext {

	private ReferenceContext referenceContext = new IdentityMapReferenceContext();

	private RecordSet<Class<?>, Serializer<?>> serializerRecordSet = new RecordSet<>();

	private Record<Serializer<?>> nullSerializerRecord;

	private FieldSerializerFactory serializerFactory = new FieldSerializerFactory();

	public SharkSerialization() {
		DefaultSerializers.registerAll(this);
		nullSerializerRecord = serializerRecordSet.get(null);
		new FieldSerializerFactoryDefaultConfigurator(this).configure();
	}

	/**
	 * Calls {@code registerObject(type, type, constructor)}. See
	 * {@link #registerObject(Class, Class, Supplier)}
	 * 
	 * @param <T>         the object type
	 * @param type        the class of the object
	 * @param constructor the concrete no-args constructor to use
	 * @return an instance of {@link ObjectSerializerConfigurationHelper} to
	 *         optionally configure the newly created ObjectSerializer
	 * @see #registerObject(Class, Class, Supplier)
	 */
	public <T> ObjectSerializerConfigurationHelper registerObject(Class<T> type, Supplier<? extends T> constructor) {
		return registerObject(type, type, constructor);
	}

	/**
	 * Convenience method to register a class for serialization using an
	 * {@link ObjectSerializer}. When the graph encounter the type
	 * {@code declarationType} (by field declared type or by field configuration),
	 * it will be serialized using an ObjectSerializer of type {@code concreteType}.
	 * 
	 * @param <T>             the declared type
	 * @param <U>             the concrete type for the {@link ObjectSerializer}
	 * @param declarationType the declaration class
	 * @param concreteType    the concrete class for the {@link ObjectSerializer}
	 * @param constructor     the concrete no-args constructor to use
	 * @return an instance of {@link ObjectSerializerConfigurationHelper} to
	 *         optionally configure the newly created ObjectSerializer
	 */
	public <T, U extends T> ObjectSerializerConfigurationHelper registerObject(Class<T> declarationType, Class<U> concreteType, Supplier<? extends U> constructor) {
		ObjectSerializer<U> serializer = new ObjectSerializer<>(concreteType, constructor);
		register(declarationType, serializer);
		return new ObjectSerializerConfigurationHelper(serializer);
	}

	/**
	 * Convenience method to register an enum class for serialization using an
	 * {@link EnumSerializer}.
	 * 
	 * @param <T>  the enum type
	 * @param type the enum class instance
	 */
	public <T extends Enum<T>> void registerEnum(Class<T> type) {
		register(type, new EnumSerializer<>(type));
	}

	/**
	 * Convenience method to register a constructor for special use. By default,
	 * according to the {@link FieldSerializerFactoryDefaultConfigurator}, this is
	 * the case for all non-primitive arrays, Lists, Sets and Maps.
	 * 
	 * @param <T>         the class type
	 * @param type        the class binded with the constructor
	 * @param constructor the constructor method for the class, the int argument may
	 *                    be used for sizing arrays, collections and maps.
	 */
	public <T> void registerConstructor(Class<T> type, IntFunction<? extends T> constructor) {
		serializerFactory.registerSizeableConstructor(type, constructor);
	}

	@Override
	public <T> void register(Class<T> type, Serializer<? extends T> serializer) {
		serializerRecordSet.register(type, serializer);
	}

	@Override
	public void initialize() {
		SerializationContext.super.initialize();
		serializerRecordSet.initialize();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Serializer<? extends T> getSerializer(Class<T> type) {
		Record<Serializer<?>> serializerRecord = serializerRecordSet.get(type);
		if (serializerRecord == null) {
			throw new SharkSerializationException("No serializer recorded for class : " + type.getName());
		}
		return (Serializer<? extends T>) serializerRecord.getElement();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Serializer<T> writeType(ByteBuffer buffer, T o) {
		Record<Serializer<?>> serializerRecord;
		if (o == null) {
			serializerRecord = nullSerializerRecord;
		} else {
			serializerRecord = serializerRecordSet.get(o.getClass());
		}
		if (serializerRecord == null) {
			throw new SharkSerializationException("Class not registered: " + o.getClass().getName());
		}
		serializerRecordSet.writeRecord(buffer, serializerRecord);
		return (Serializer<T>) serializerRecord.getElement();
	}

	@Override
	public Serializer<?> readType(ByteBuffer buffer) {
		return serializerRecordSet.readRecord(buffer).getElement();
	}

	public void setReferenceContext(ReferenceContext referenceContext) {
		this.referenceContext = referenceContext;
	}

	@Override
	public ReferenceContext getReferenceContext() {
		return referenceContext;
	}

	@Override
	public FieldSerializerFactory getFieldSerializerFactory() {
		return serializerFactory;
	}

	@Override
	public void forEachSerializer(Consumer<Serializer<?>> action) {
		serializerRecordSet.forEachValues(action::accept);
	}

}
