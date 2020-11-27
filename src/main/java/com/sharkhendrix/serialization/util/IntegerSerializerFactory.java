package com.sharkhendrix.serialization.util;

import java.nio.ByteBuffer;

/**
 * Factory to build optimized {@link IntegerSerializer} according to the
 * declared situation.
 * 
 * @author Joannick Gardize
 *
 */
public class IntegerSerializerFactory {

    /**
     * Build an IntegerSerializer that takes advantage of the parameters
     * informations.
     * 
     * @param absoluteMaxValue the potential maximum value required, in absolute
     *                         value
     * @param couldBeNegative  true if the value could be negative, false otherwise
     * @return an IntegerSerializer which supports at least integers with absolute
     *         value <= {@code absoluteMaxValue}
     */
    public static IntegerSerializer build(int absoluteMaxValue, boolean couldBeNegative) {
        if (absoluteMaxValue <= Byte.MAX_VALUE) {
            return BYTE_SERIALIZER;
        } else if (couldBeNegative) {
            return VAR_INT_SERIALIZER;
        } else {
            return POSITIVE_VAR_INT_SERIALIZER;
        }

    }

    private static final IntegerSerializer BYTE_SERIALIZER = new IntegerSerializer() {

        @Override
        public void write(ByteBuffer buffer, int value) {
            buffer.put((byte) value);
        }

        @Override
        public int read(ByteBuffer buffer) {
            return buffer.get();
        }
    };

    private static final IntegerSerializer POSITIVE_VAR_INT_SERIALIZER = new IntegerSerializer() {

        @Override
        public void write(ByteBuffer buffer, int value) {
            VarNumberIO.writePositiveVarInt(buffer, value);
        }

        @Override
        public int read(ByteBuffer buffer) {
            return VarNumberIO.readPositiveVarInt(buffer);
        }
    };

    private static final IntegerSerializer VAR_INT_SERIALIZER = new IntegerSerializer() {

        @Override
        public void write(ByteBuffer buffer, int value) {
            VarNumberIO.writeVarInt(buffer, value);
        }

        @Override
        public int read(ByteBuffer buffer) {
            return VarNumberIO.readVarInt(buffer);
        }
    };

    private IntegerSerializerFactory() {
    }
}
