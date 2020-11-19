package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.lang.reflect.Field;

public abstract class AbstractFieldAccessor implements FieldAccessor {

    protected Field field;

    public AbstractFieldAccessor(Field field) {
        this.field = field;
        field.setAccessible(true);
    }

}
