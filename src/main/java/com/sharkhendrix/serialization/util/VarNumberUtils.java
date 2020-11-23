package com.sharkhendrix.serialization.util;

import java.nio.ByteBuffer;

public class VarNumberUtils {

    private static final int ONE_BYTE_DATA_MASK = 0x7F;
    private static final int TWO_BYTES_DATA_MASK = 0x3FFF;
    private static final int TWO_BYTES_SHORT_FLAG = 0x8000;
    private static final byte TWO_BYTE_FLAG = (byte) 0x80;
    private static final int THREE_BYTES_DATA_MASK = 0x1FFFFF;
    private static final byte THREE_BYTES_FLAG = (byte) 0xC0;
    private static final byte FOUR_BYTES_FLAG = (byte) 0xE0;

    public static void writeVarInt(ByteBuffer buffer, int i) {
        if ((i | ONE_BYTE_DATA_MASK) == ONE_BYTE_DATA_MASK) {
            buffer.put((byte) i);
        } else if ((i | TWO_BYTES_DATA_MASK) == TWO_BYTES_DATA_MASK) {
            buffer.putShort((short) (i | TWO_BYTES_SHORT_FLAG));
        } else if ((i | THREE_BYTES_DATA_MASK) == THREE_BYTES_DATA_MASK) {
            buffer.put((byte) ((i >> 16) | THREE_BYTES_FLAG));
            buffer.putShort((short) i);
        } else {
            buffer.put(FOUR_BYTES_FLAG);
            buffer.putInt(i);
        }
    }

    public static int readVarInt(ByteBuffer buffer) {
        byte b = buffer.get();
        if ((b & TWO_BYTE_FLAG) == 0) {
            return b;
        } else if ((b & THREE_BYTES_FLAG) == TWO_BYTE_FLAG) {
            return ((b & (~TWO_BYTE_FLAG)) << 8) | (buffer.get() & 0xFF);
        } else if ((b & FOUR_BYTES_FLAG) == THREE_BYTES_FLAG) {
            return ((b & (~THREE_BYTES_FLAG)) << 16) | (buffer.getShort() & 0xFFFF);
        } else {
            return buffer.getInt();
        }
    }
}
