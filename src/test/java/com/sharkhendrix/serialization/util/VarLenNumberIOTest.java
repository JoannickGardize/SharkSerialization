package com.sharkhendrix.serialization.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.sharkhendrix.util.VarLenNumberIO;

public class VarLenNumberIOTest {

    @ParameterizedTest
    @MethodSource("intValues")
    public void positiveVarIntTest(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        VarLenNumberIO.writePositiveVarInt(buffer, i);
        buffer.flip();
        int dataBits = 32 - Integer.numberOfLeadingZeros(i);
        int totalBits = dataBits + dataBits / 8 + 1;
        int numberOfBytes = (int) Math.ceil(totalBits / 8.0);
        Assertions.assertEquals(numberOfBytes, buffer.limit());
        Assertions.assertEquals(i, VarLenNumberIO.readPositiveVarInt(buffer));
    }

    @ParameterizedTest
    @MethodSource("intValues")
    public void varIntTest(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        VarLenNumberIO.writeVarInt(buffer, i);
        buffer.flip();
        int dataBits = 32 - Integer.numberOfLeadingZeros(i < 0 ? ~i : i);
        int totalBits = dataBits + dataBits / 8 + 2;
        int numberOfBytes = (int) Math.ceil(totalBits / 8.0);
        Assertions.assertEquals(numberOfBytes, buffer.limit());
        Assertions.assertEquals(i, VarLenNumberIO.readVarInt(buffer));
    }

    @ParameterizedTest
    @MethodSource("longValues")
    public void positiveVarLongTest(long l) {
        ByteBuffer buffer = ByteBuffer.allocate(9);
        VarLenNumberIO.writePositiveVarLong(buffer, l);
        buffer.flip();
        int dataBits = 64 - Long.numberOfLeadingZeros(l);
        int totalBits = dataBits + dataBits / 8 + 1;
        int numberOfBytes = (int) Math.ceil(totalBits / 8.0);
        if (numberOfBytes > 9) {
            numberOfBytes = 9;
        }
        Assertions.assertEquals(numberOfBytes, buffer.limit());
        Assertions.assertEquals(l, VarLenNumberIO.readPositiveVarLong(buffer));
    }

    @ParameterizedTest
    @MethodSource("longValues")
    public void varLongTest(long l) {
        ByteBuffer buffer = ByteBuffer.allocate(9);
        VarLenNumberIO.writeVarLong(buffer, l);
        buffer.flip();
        int dataBits = 64 - Long.numberOfLeadingZeros(l < 0 ? ~l : l);
        int totalBits = dataBits + dataBits / 8 + 2;
        int numberOfBytes = (int) Math.ceil(totalBits / 8.0);
        if (numberOfBytes > 9) {
            numberOfBytes = 9;
        }
        Assertions.assertEquals(numberOfBytes, buffer.limit());
        Assertions.assertEquals(l, VarLenNumberIO.readVarLong(buffer));
    }

    public static Collection<Integer> intValues() {
        return longValues(4).stream().map(Long::intValue).collect(Collectors.toList());
    }

    public static Collection<Long> longValues() {
        return longValues(8);
    }

    public static Collection<Long> longValues(int typeSize) {
        int nbBit = typeSize * 8;
        Collection<Long> result = new ArrayList<>();
        for (int i = 0; i < nbBit; i++) {
            long l = 1L << i;
            result.add(l - 1);
            result.add(l);
            result.add(l + 1);
            result.add(-l - 1);
            result.add(-l);
            result.add(-l + 1);
        }
        return result;
    }

}
