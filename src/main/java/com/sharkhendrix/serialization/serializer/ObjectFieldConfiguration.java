package com.sharkhendrix.serialization.serializer;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.sharkhendrix.serialization.serializer.fieldaccess.FieldAccessor;

public class ObjectFieldConfiguration {

    private String fieldName;
    private Function<Object, Object> getter;
    private BiConsumer<Object, Object> setter;
    private FieldAccessor accessor;

    public ObjectFieldConfiguration(String fieldName) {
        super();
        this.fieldName = fieldName;
    }

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

    public FieldAccessor getAccessor() {
        return accessor;
    }

    public void setAccessor(FieldAccessor accessor) {
        this.accessor = accessor;
    }

    public String getFieldName() {
        return fieldName;
    }
}
