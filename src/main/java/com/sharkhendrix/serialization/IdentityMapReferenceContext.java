package com.sharkhendrix.serialization;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class IdentityMapReferenceContext implements ReferenceContext {

	private Map<Object, Short> idByObject = new IdentityHashMap<>();
	private Map<Short, Object> objectById = new HashMap<>();
	private short previousId;

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
	public short retrieve(Object o) {
		return idByObject.getOrDefault(o, (short) -1);
	}

	@Override
	public short store(Object o) {
		short id = ++previousId;
		idByObject.put(o, previousId);
		return id;
	}

	@Override
	public void store(short id, Object o) {
		objectById.put(id, o);
	}

	@Override
	public Object retrieve(short id) {
		return objectById.get(id);
	}
}
