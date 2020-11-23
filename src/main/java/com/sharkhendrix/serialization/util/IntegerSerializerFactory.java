package com.sharkhendrix.serialization.util;

import java.nio.ByteBuffer;

/**
 * Factory of {@link IntegerSerializer}.
 * 
 * @author Joannick
 *
 */
public class IntegerSerializerFactory {

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

    private static final IntegerSerializer SHORT_SERIALIZER = new IntegerSerializer() {

        @Override
        public void write(ByteBuffer buffer, int value) {
            buffer.putShort((short) value);
        }

        @Override
        public int read(ByteBuffer buffer) {
            return buffer.getShort();
        }
    };

    private static final IntegerSerializer INT_SERIALIZER = new IntegerSerializer() {

        @Override
        public void write(ByteBuffer buffer, int value) {
            buffer.putInt(value);
        }

        @Override
        public int read(ByteBuffer buffer) {
            return buffer.getInt();
        }
    };

    private IntegerSerializerFactory() {
    }

    /**
     * Returns an IntegerSerializer that uses the smallest number of bytes required
     * according to the known maximum value required.
     * 
     * @param potentialAbsoluteMaxValue the potential maximum value, in absolute
     *                                  value
     * @return an IntegerSerializer
     */
    public static IntegerSerializer build(int potentialAbsoluteMaxValue) {
        if (potentialAbsoluteMaxValue <= Byte.MAX_VALUE) {
            return BYTE_SERIALIZER;
        } else if (potentialAbsoluteMaxValue <= Short.MAX_VALUE) {
            return SHORT_SERIALIZER;
        } else {
            return INT_SERIALIZER;
        }

    }
}
