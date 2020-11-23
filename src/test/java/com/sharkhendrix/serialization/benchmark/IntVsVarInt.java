package com.sharkhendrix.serialization.benchmark;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.util.VarNumberUtils;

public class IntVsVarInt {

    public static void main(String[] args) {
        BenchmarkUtils.chrono("normal int", IntVsVarInt::normalInt);
        BenchmarkUtils.chrono("var int", IntVsVarInt::varInt);
    }

    private static void normalInt() {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        for (int i = 0; i < 10_000_000; i++) {
            buffer.clear();
            buffer.putInt(i % 64);
            buffer.flip();
            buffer.getInt();
        }
    }

    private static void varInt() {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        for (int i = 0; i < 10_000_000; i++) {
            buffer.clear();
            VarNumberUtils.writeVarInt(buffer, i % 64);
            buffer.flip();
            VarNumberUtils.readVarInt(buffer);
        }
    }
}
