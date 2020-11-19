package com.sharkhendrix.serialization.serializer;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntFunction;

import com.sharkhendrix.serialization.Serializer;

public class MapSerializer<T extends Map<Object, Object>> implements Serializer<T> {

    private IntFunction<T> constructor;
    private Serializer<Object> keySerializer;
    private Serializer<Object> valueSerializer;

    @SuppressWarnings("unchecked")
    public MapSerializer(IntFunction<T> constructor, Serializer<?> keySerializer, Serializer<?> valueSerializer) {
        this.constructor = constructor;
        this.keySerializer = (Serializer<Object>) keySerializer;
        this.valueSerializer = (Serializer<Object>) valueSerializer;
    }

    @Override
    public void write(ByteBuffer buffer, T object) {
        int length = object.size();
        buffer.putInt(length);
        for (Entry<Object, Object> entry : object.entrySet()) {
            keySerializer.write(buffer, entry.getKey());
            valueSerializer.write(buffer, entry.getValue());
        }
    }

    @Override
    public T read(ByteBuffer buffer) {
        int length = buffer.getInt();
        T object = constructor.apply(length);
        for (int i = 0; i < length; i++) {
            object.put(keySerializer.read(buffer), valueSerializer.read(buffer));
        }
        return object;
    }
}
