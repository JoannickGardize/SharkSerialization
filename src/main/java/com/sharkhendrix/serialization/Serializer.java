package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import com.sharkhendrix.serialization.serializer.ObjectSerializer;
import com.sharkhendrix.serialization.serializer.SharedReferenceSerializer;

/**
 * Base interface for object's serializers.
 * 
 * @author Joannick Gardize
 *
 * @param <T> the type this serializer is able to read and write
 */
public interface Serializer<T> {

    /**
     * Write the given object to the given buffer.
     * 
     * @param buffer the buffer to write in
     * @param object the object to serialize
     */
    void write(ByteBuffer buffer, T object);

    /**
     * Read the given object from the given buffer.
     * 
     * @param buffer the buffer to read
     * @return the new instance deserialized from the buffer
     */
    T read(ByteBuffer buffer);

    /**
     * Initialize this serializer, this method must be called one time, before any
     * serialization. This allows serializers to configure itself with a full view
     * of all the serializers of the context.
     * 
     * @param serialization the serialization context this serializer is initalized
     *                      for
     */
    default void initialize(SerializationContext context) {
        // Nothing by default
    }

    /**
     * <p>
     * This method is called when the newly created instance have to be provided
     * before reading any recursive content. This is the case in the context of a
     * reference resolver.
     * <p>
     * The default implementation simply calls {@link #read(ByteBuffer)} and
     * consider that the object has no recursive content.
     * <p>
     * Typically, the content is recursive if reading it requires a call to other
     * unknown Serializers, this is the case for the {@link ObjectSerializer}.
     * <p>
     * {@link SharedReferenceSerializer} calls this signature to store the reference
     * of the newly created instance, before going deeper in its content.
     * <p>
     * If this method must be overridden for the above reason (recursive content),
     * then this is highly recommended that the implementation of
     * {@link Serializer#read(ByteBuffer)} calls this method, with a "do nothing"
     * {@code intermediateConsumer}, since they must read the data the exact same
     * way.
     * 
     * @param buffer
     * @param intermediateConsumer
     * @return
     */
    default T read(ByteBuffer buffer, Consumer<T> intermediateConsumer) {
        T object = read(buffer);
        intermediateConsumer.accept(object);
        return object;
    }
}
