package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import com.sharkhendrix.serialization.SerializationContext;

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
     * Typically, the content is recursive if reading it requires a call to
     * {@link SerializationContext#readObject(ByteBuffer)}.
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
