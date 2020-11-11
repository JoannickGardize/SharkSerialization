package com.sharkhendrix.serialization.serializer;

import java.util.function.IntFunction;

public class ObjectArraySerializer<T> {

    private IntFunction<T> newInstanceFunction;

    public ObjectArraySerializer(IntFunction<T> newInstanceFunction) {
        this.newInstanceFunction = newInstanceFunction;
        // TODO in progress
    }

}
