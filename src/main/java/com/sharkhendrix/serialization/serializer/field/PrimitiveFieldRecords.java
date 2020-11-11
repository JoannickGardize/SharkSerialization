package com.sharkhendrix.serialization.serializer.field;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PrimitiveFieldRecords {

    private static Map<Class<?>, Function<Field, FieldRecord>> fieldRecordSuppliers = new HashMap<>();

    static {
        fieldRecordSuppliers.put(byte.class, PrimitiveFieldRecords::byteFieldRecord);
        fieldRecordSuppliers.put(char.class, PrimitiveFieldRecords::charFieldRecord);
        fieldRecordSuppliers.put(boolean.class, PrimitiveFieldRecords::booleanFieldRecord);
        fieldRecordSuppliers.put(short.class, PrimitiveFieldRecords::shortFieldRecord);
        fieldRecordSuppliers.put(int.class, PrimitiveFieldRecords::intFieldRecord);
        fieldRecordSuppliers.put(long.class, PrimitiveFieldRecords::longFieldRecord);
        fieldRecordSuppliers.put(float.class, PrimitiveFieldRecords::floatFieldRecord);
        fieldRecordSuppliers.put(double.class, PrimitiveFieldRecords::doubleFieldRecord);
    }

    public static FieldRecord create(Field field) {
        return fieldRecordSuppliers.get(field.getType()).apply(field);
    }

    private static FieldRecord byteFieldRecord(Field field) {
        return new FieldRecord(field) {

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

    private static FieldRecord charFieldRecord(Field field) {
        return new FieldRecord(field) {

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

    private static FieldRecord booleanFieldRecord(Field field) {
        return new FieldRecord(field) {

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

    private static FieldRecord shortFieldRecord(Field field) {
        return new FieldRecord(field) {

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

    private static FieldRecord intFieldRecord(Field field) {
        return new FieldRecord(field) {

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

    public static FieldRecord longFieldRecord(Field field) {
        return new FieldRecord(field) {

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

    private static FieldRecord floatFieldRecord(Field field) {
        return new FieldRecord(field) {

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

    private static FieldRecord doubleFieldRecord(Field field) {
        return new FieldRecord(field) {

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
