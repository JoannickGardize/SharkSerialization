package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PrimitiveReflectionFieldAccessors {

    private static Map<Class<?>, Function<Field, FieldAccessor>> fieldRecordSuppliers = new HashMap<>();

    static {
        fieldRecordSuppliers.put(byte.class, PrimitiveReflectionFieldAccessors::byteFieldAccesor);
        fieldRecordSuppliers.put(char.class, PrimitiveReflectionFieldAccessors::charFieldAccessor);
        fieldRecordSuppliers.put(boolean.class, PrimitiveReflectionFieldAccessors::booleanFieldAccessor);
        fieldRecordSuppliers.put(short.class, PrimitiveReflectionFieldAccessors::shortFieldAccessor);
        fieldRecordSuppliers.put(int.class, PrimitiveReflectionFieldAccessors::intFieldAccessor);
        fieldRecordSuppliers.put(long.class, PrimitiveReflectionFieldAccessors::longFieldAccessor);
        fieldRecordSuppliers.put(float.class, PrimitiveReflectionFieldAccessors::floatFieldAccessor);
        fieldRecordSuppliers.put(double.class, PrimitiveReflectionFieldAccessors::doubleFieldAccessor);
    }

    public static FieldAccessor get(Field field) {
        return fieldRecordSuppliers.get(field.getType()).apply(field);
    }

    private static FieldAccessor byteFieldAccesor(Field field) {
        return new AbstractFieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.put(field.getByte(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setByte(object, buffer.get());
            }
        };
    }

    private static FieldAccessor charFieldAccessor(Field field) {
        return new AbstractFieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putChar(field.getChar(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setChar(object, buffer.getChar());
            }
        };
    }

    private static FieldAccessor booleanFieldAccessor(Field field) {
        return new AbstractFieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.put(field.getBoolean(object) ? (byte) 1 : (byte) 0);
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setBoolean(object, buffer.get() == (byte) 1);
            }
        };
    }

    private static FieldAccessor shortFieldAccessor(Field field) {
        return new AbstractFieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putShort(field.getShort(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setShort(object, buffer.getShort());
            }
        };
    }

    private static FieldAccessor intFieldAccessor(Field field) {
        return new AbstractFieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putInt(field.getInt(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setInt(object, buffer.getInt());
            }
        };
    }

    public static FieldAccessor longFieldAccessor(Field field) {
        return new AbstractFieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putLong(field.getLong(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setLong(object, buffer.getLong());
            }
        };
    }

    private static FieldAccessor floatFieldAccessor(Field field) {
        return new AbstractFieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putFloat(field.getFloat(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setFloat(object, buffer.getFloat());
            }
        };
    }

    private static FieldAccessor doubleFieldAccessor(Field field) {
        return new AbstractFieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                buffer.putDouble(field.getDouble(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setDouble(object, buffer.getDouble());
            }
        };
    }
}
