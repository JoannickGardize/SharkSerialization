package com.sharkhendrix.serialization.serializer.fieldaccess;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.sharkhendrix.serialization.annotation.VarLenStrategy;
import com.sharkhendrix.serialization.serializer.VarLenTypeKey;
import com.sharkhendrix.util.VarLenNumberIO;

public class PrimitiveFieldAccessors {

    private static Map<VarLenTypeKey, Function<Field, FieldAccessor>> fieldRecordSuppliers = new HashMap<>();

    static {
        fieldRecordSuppliers.put(new VarLenTypeKey(byte.class, VarLenStrategy.NONE), PrimitiveFieldAccessors::byteFieldAccesor);
        fieldRecordSuppliers.put(new VarLenTypeKey(char.class, VarLenStrategy.NONE), PrimitiveFieldAccessors::charFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(boolean.class, VarLenStrategy.NONE), PrimitiveFieldAccessors::booleanFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(short.class, VarLenStrategy.NONE), PrimitiveFieldAccessors::shortFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(int.class, VarLenStrategy.NORMAL), PrimitiveFieldAccessors::intNormalFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(int.class, VarLenStrategy.POSITIVE), PrimitiveFieldAccessors::intPositiveFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(int.class, VarLenStrategy.NONE), PrimitiveFieldAccessors::intNoneFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(long.class, VarLenStrategy.NORMAL), PrimitiveFieldAccessors::longNormalFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(long.class, VarLenStrategy.POSITIVE), PrimitiveFieldAccessors::longPositiveFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(long.class, VarLenStrategy.NONE), PrimitiveFieldAccessors::longNoneFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(float.class, VarLenStrategy.NONE), PrimitiveFieldAccessors::floatFieldAccessor);
        fieldRecordSuppliers.put(new VarLenTypeKey(double.class, VarLenStrategy.NONE), PrimitiveFieldAccessors::doubleFieldAccessor);
    }

    public static FieldAccessor get(Field field, VarLenStrategy varLenStrategy) {
        Function<Field, FieldAccessor> producer = fieldRecordSuppliers.get(new VarLenTypeKey(field.getType(), varLenStrategy));
        if (producer == null) {
            producer = fieldRecordSuppliers.get(new VarLenTypeKey(field.getType(), VarLenStrategy.NONE));
        }
        return producer.apply(field);
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

    public static FieldAccessor intNormalFieldAccessor(Field field) {
        return new FieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                VarLenNumberIO.writeVarInt(buffer, field.getInt(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setInt(object, VarLenNumberIO.readVarInt(buffer));
            }
        };
    }

    public static FieldAccessor intPositiveFieldAccessor(Field field) {
        return new FieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                VarLenNumberIO.writePositiveVarInt(buffer, field.getInt(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setInt(object, VarLenNumberIO.readPositiveVarInt(buffer));
            }
        };
    }

    public static FieldAccessor intNoneFieldAccessor(Field field) {
        return new FieldAccessor(field) {

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

    public static FieldAccessor longNormalFieldAccessor(Field field) {
        return new FieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                VarLenNumberIO.writeVarLong(buffer, field.getLong(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setLong(object, VarLenNumberIO.readVarLong(buffer));
            }
        };
    }

    public static FieldAccessor longPositiveFieldAccessor(Field field) {
        return new FieldAccessor(field) {

            @Override
            public void writeField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                VarLenNumberIO.writePositiveVarLong(buffer, field.getLong(object));
            }

            @Override
            public void readField(ByteBuffer buffer, Object object) throws IllegalArgumentException, IllegalAccessException {
                field.setLong(object, VarLenNumberIO.readPositiveVarLong(buffer));
            }
        };
    }

    public static FieldAccessor longNoneFieldAccessor(Field field) {
        return new FieldAccessor(field) {

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
