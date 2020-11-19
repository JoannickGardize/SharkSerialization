package com.sharkhendrix.serialization.serializer;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ObjectFieldConfiguration {

    private Function<Object, Object> getter;
    private BiConsumer<Object, Object> setter;

    public Function<Object, Object> getGetter() {
        return getter;
    }

    public void setGetter(Function<Object, Object> getter) {
        this.getter = getter;
    }

    public BiConsumer<Object, Object> getSetter() {
        return setter;
    }

    public void setSetter(BiConsumer<Object, Object> setter) {
        this.setter = setter;
    }
}