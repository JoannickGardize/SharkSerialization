package com.sharkhendrix.serialization.serializer.field;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PrimitiveFieldAccessors {

    private static Map<Class<?>, Function<Field, AttributeAccessor>> fieldRecordSuppliers = new HashMap<>();

    static {
        fieldRecordSuppliers.put(byte.class, PrimitiveFieldAccessors::byteFieldAccesor);
        fieldRecordSuppliers.put(char.class, PrimitiveFieldAccessors::charFieldAccessor);
        fieldRecordSuppliers.put(boolean.class, PrimitiveFieldAccessors::booleanFieldAccessor);
        fieldRecordSuppliers.put(short.class, PrimitiveFieldAccessors::shortFieldAccessor);
        fieldRecordSuppliers.put(int.class, PrimitiveFieldAccessors::intFieldAccessor);
        fieldRecordSuppliers.put(long.class, PrimitiveFieldAccessors::longFieldAccessor);
        fieldRecordSuppliers.put(float.class, PrimitiveFieldAccessors::floatFieldAccessor);
        fieldRecordSuppliers.put(double.class, PrimitiveFieldAccessors::doubleFieldAccessor);
    }

    public static AttributeAccessor get(Field field) {
        return fieldRecordSuppliers.get(field.getType()).apply(field);
    }

    private static AttributeAccessor byteFieldAccesor(Field field) {
        return new AttributeAccessor(field) {

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

    private static AttributeAccessor charFieldAccessor(Field field) {
        return new AttributeAccessor(field) {

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

    private static AttributeAccessor booleanFieldAccessor(Field field) {
        return new AttributeAccessor(field) {

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

    private static AttributeAccessor shortFieldAccessor(Field field) {
        return new AttributeAccessor(field) {

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

    private static AttributeAccessor intFieldAccessor(Field field) {
        return new AttributeAccessor(field) {

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

    public static AttributeAccessor longFieldAccessor(Field field) {
        return new AttributeAccessor(field) {

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

    private static AttributeAccessor floatFieldAccessor(Field field) {
        return new AttributeAccessor(field) {

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

    private static AttributeAccessor doubleFieldAccessor(Field field) {
        return new AttributeAccessor(field) {

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
