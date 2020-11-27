package com.sharkhendrix.serialization;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Reference context that uses an {@link IdentityHashMap} to identify same
 * instances.
 * 
 * @author Joannick Gardize
 *
 */
public class IdentityMapReferenceContext implements ReferenceContext {

    private Map<Object, Integer> idByObject = new IdentityHashMap<>();
    private Map<Integer, Object> objectById = new HashMap<>();
    private int previousId;

    @Override
    public void resetWriteContext() {
        idByObject.clear();
        previousId = 0;
    }

    @Override
    public void resetReadContext() {
        objectById.clear();
    }

    @Override
    public int retrieve(Object o) {
        return idByObject.getOrDefault(o, -1);
    }

    @Override
    public int store(Object o) {
        int id = ++previousId;
        idByObject.put(o, previousId);
        return id;
    }

    @Override
    public void store(int id, Object o) {
        objectById.put(id, o);
    }

    @Override
    public Object retrieve(int id) {
        return objectById.get(id);
    }
}
