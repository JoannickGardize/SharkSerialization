package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import com.sharkhendrix.serialization.annotation.SharedReference;
import com.sharkhendrix.serialization.serializer.ObjectSerializer;

public interface Serializer<T> {

    void write(ByteBuffer buffer, T object);

    T read(ByteBuffer buffer);

    /**
     * Initialize this serializer, this method must be called once, before any
     * serialization.
     * 
     * @param serialization
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
     * The default implementation consider that the object has no recursive content.
     * <p>
     * Typically, the content is recursive if reading it requires a call to other
     * unknown Serializers, this is the case for the {@link ObjectSerializer}.
     * <p>
     * {@link SharedReference} uses this signature to store the reference of the
     * newly created instance.
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
