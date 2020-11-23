package com.sharkhendrix.serialization.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Stores a set a key/value pairs, and is able to write and retrieve them by an
 * internal ID. Uses the {@link IntegerSerializerFactory} to use the smallest
 * number of byte required for writing / reading IDs.
 * 
 * @author Joannick Gardize
 *
 * @param <K> the key type of the records
 * @param <V> the value type of the records
 */
public class RecordSet<K, V> {

    private List<Record<V>> recordsById = new ArrayList<>();
    private Map<K, Record<V>> recordsByKey = new HashMap<>();
    private IntegerSerializer idSerialier;

    public void register(K key, V value) {
        Record<V> record = new Record<>(recordsById.size(), value);
        recordsById.add(record);
        recordsByKey.put(key, record);
    }

    public void initialize() {
        idSerialier = IntegerSerializerFactory.build(recordsById.size() - 1);
    }

    public Record<V> get(K key) {
        return recordsByKey.get(key);
    }

    public Record<V> get(int id) {
        return recordsById.get(id);
    }

    public void forEachValues(Consumer<V> action) {
        recordsById.forEach(r -> action.accept(r.getElement()));
    }

    public int size() {
        return recordsById.size();
    }

    public void writeRecord(ByteBuffer buffer, Record<V> record) {
        idSerialier.write(buffer, record.getId());
    }

    public Record<V> readRecord(ByteBuffer buffer) {
        return recordsById.get(idSerialier.read(buffer));
    }
}
