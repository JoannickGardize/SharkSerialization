package com.sharkhendrix.serialization.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RecordSet<K, V> {

    private List<Record<V>> recordsById = new ArrayList<>();
    private Map<K, Record<V>> recordsByKey = new HashMap<>();

    public void register(K key, V value) {
        Record<V> record = new Record<>(recordsById.size(), value);
        recordsById.add(record);
        recordsByKey.put(key, record);
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
}
