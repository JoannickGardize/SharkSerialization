package com.sharkhendrix.serialization.util;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class IntegerSerializerFactoryTest {

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 4 })
    public void test(int nBytes) {
        int maxValue = ((int) Math.pow(2, 8 * nBytes)) / 2 - 1;
        ByteBuffer buffer = ByteBuffer.allocate(10);
        IntegerSerializer serializer = IntegerSerializerFactory.build(maxValue);

        serializer.write(buffer, maxValue);
        buffer.flip();
        Assertions.assertEquals(nBytes, buffer.limit());
        Assertions.assertEquals(maxValue, serializer.read(buffer));
    }
}
