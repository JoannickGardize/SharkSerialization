package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.sharkhendrix.serialization.util.VarNumberIO;

public class PrimitiveFieldAccessors {

    private static Map<Class<?>, Function<Field, FieldAccessor>> fieldRecordSuppliers = new HashMap<>();

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

    public static FieldAccessor get(Field field) {
        return fieldRecordSuppliers.get(field.getType()).apply(field);
    }

    public static FieldAccessor byteFieldAccesor(Field field) {
        return new FieldAccessor(field) {

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

    public static FieldAccessor charFieldAccessor(Field field) {
        return new FieldAccessor(field) {

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

    public static FieldAccessor booleanFieldAccessor(Field field) {
        return new FieldAccessor(field) {

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

    public static FieldAccessor shortFieldAccessor(Field field) {
        return new FieldAccessor(field) {

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

    public static FieldAccessor intFieldAccessor(Field field) {
        return new FieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                VarNumberIO.writeVarInt(buffer, field.getInt(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setInt(object, VarNumberIO.readVarInt(buffer));
            }
        };
    }

    public static FieldAccessor longFieldAccessor(Field field) {
        return new FieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                VarNumberIO.writeVarLong(buffer, field.getLong(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setLong(object, VarNumberIO.readVarLong(buffer));
            }
        };
    }

    public static FieldAccessor floatFieldAccessor(Field field) {
        return new FieldAccessor(field) {

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

    public static FieldAccessor doubleFieldAccessor(Field field) {
        return new FieldAccessor(field) {

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
