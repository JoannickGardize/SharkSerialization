package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import com.sharkhendrix.serialization.serializer.factory.FieldSerializerFactory;

/**
 * <p>
 * A serialization context able to associate {@link Serializer}s to class types.
 * <p>
 * Instances of registered classes are able to be serialized / deserialized,
 * using the serializer to use as overhead information.
 * <p>
 * Provides a {@link ReferenceContext} and a {@link FieldSerializerFactory} to
 * be optionally used by serializers.
 * 
 * @author Joannick Gardize
 *
 */

public interface SerializationContext {

	/**
	 * Register the given serializer to be used for objects the given type.
	 * 
	 * @param <T>        the type to register
	 * @param type       the type to be serialized / deserialized with the given
	 *                   serializer
	 * @param serializer the serializer to use for the given type
	 */
	<T> void register(Class<T> type, Serializer<? extends T> serializer);

	/**
	 * Get a previously registered serializer.
	 * 
	 * @param <T>  the type
	 * @param type the type to get its serializer
	 * @return the serializer of the given type
	 */
	<T> Serializer<? extends T> getSerializer(Class<T> type);

	/**
	 * Write the type information of the given object to the buffer and returns its
	 * serializer.
	 * 
	 * @param <T>    the object's type
	 * @param buffer the buffer to write in
	 * @param o      the object to write its type information
	 * @return the serializer for the object o, previously registered
	 */
	<T> Serializer<T> writeType(ByteBuffer buffer, T o);

	/**
	 * Read the type information in the given buffer and returns the appropriate
	 * serializer.
	 * 
	 * @param buffer the buffer to read
	 * @return the serializer associated to the read information type
	 */
	Serializer<?> readType(ByteBuffer buffer);

	/**
	 * @return the ReferenceContext associated to this serialization context
	 */
	ReferenceContext getReferenceContext();

	/**
	 * @return the FieldSerializerFactory associated to this serialization context
	 */
	FieldSerializerFactory getFieldSerializerFactory();

	/**
	 * Calls the action for each registered serializer
	 * 
	 * @param action the action to do
	 */
	void forEachSerializer(Consumer<Serializer<?>> action);

	/**
	 * Initialize all the registered serializers by calling
	 * {@link Serializer#initialize(SerializationContext)}
	 */
	default void initialize() {
		forEachSerializer(s -> s.initialize(this));
	}

	/**
	 * Write the full graph of the object o. Keeps the actual reference context.
	 * 
	 * @param buffer the buffer to write in
	 * @param o      the object to write
	 */
	default void writeObject(ByteBuffer buffer, Object o) {
		writeType(buffer, o).write(buffer, o);
	}

	/**
	 * <p>
	 * Read the full graph of the object in this buffer. Keeps the actual reference
	 * context.
	 * 
	 * @param buffer the buffer to read
	 * @return the read object
	 */
	default Object readObject(ByteBuffer buffer) {
		return readType(buffer).read(buffer);
	}

	/**
	 * <p>
	 * Write the full graph of the object o with a new reference context.
	 * <p>
	 * 
	 * @param buffer the buffer to write in
	 * @param o      the object to serialize
	 */
	default void write(ByteBuffer buffer, Object o) {
		getReferenceContext().resetWriteContext();
		writeObject(buffer, o);
	}

	/**
	 * <p>
	 * Read the full graph of the object in this buffer with a new reference
	 * context.
	 * <p>
	 * 
	 * @param buffer the buffer to read
	 * @return the deserialized object
	 */
	default Object read(ByteBuffer buffer) {
		getReferenceContext().resetReadContext();
		return readObject(buffer);
	}
}
