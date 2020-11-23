package com.sharkhendrix.serialization.util;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class VarNumberUtilsTest {

    @ParameterizedTest
    @ValueSource(ints = { -10000, -100, -1, 0, 1, 50, 542, 135421, 21354612, -165421231, Integer.MIN_VALUE, Integer.MAX_VALUE, 0x7F, 0x7F - 1, 0x7F + 1, 0x3FFF, 0x3FFF - 1,
            0x3FFF + 1, 0x1FFFFF, 0x1FFFFF - 1, 0x1FFFFF + 1 })
    public void varIntTest(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        VarNumberUtils.writeVarInt(buffer, i);
        buffer.flip();
        Assertions.assertEquals(i, VarNumberUtils.readVarInt(buffer));
    }

}
