package com.sharkhendrix.serialization.benchmark;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.SharkSerialization;

public class MethodVsFieldAccessBenchmark {

    private static class A {
        int s1;
        int s2;
        int s3;
        int s4;
        int s5;

        public int getS1() {
            return s1;
        }

        public void setS1(int s1) {
            this.s1 = s1;
        }

        public int getS2() {
            return s2;
        }

        public void setS2(int s2) {
            this.s2 = s2;
        }

        public int getS3() {
            return s3;
        }

        public void setS3(int s3) {
            this.s3 = s3;
        }

        public int getS4() {
            return s4;
        }

        public void setS4(int s4) {
            this.s4 = s4;
        }

        public int getS5() {
            return s5;
        }

        public void setS5(int s5) {
            this.s5 = s5;
        }

    }

    public static void main(String[] args) {
        BenchmarkUtils.chrono("Field access", MethodVsFieldAccessBenchmark::executeFieldAccess);
        BenchmarkUtils.chrono("Method access", MethodVsFieldAccessBenchmark::executeMethodAccess);
    }

    private static void executeFieldAccess() {
        SharkSerialization serialization = new SharkSerialization();
        serialization.registerObject(A.class, A::new);
        execute(serialization);
    }

    private static void executeMethodAccess() {
        SharkSerialization serialization = new SharkSerialization();
        serialization.registerObject(A.class, A::new).primitiveAccess(A::getS1, A::setS1).primitiveAccess(A::getS2, A::setS2).primitiveAccess(A::getS3, A::setS3)
                .primitiveAccess(A::getS4, A::setS4).primitiveAccess(A::getS5, A::setS5);
        execute(serialization);
    }

    private static void execute(SharkSerialization serialization) {
        serialization.initialize();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        A a = new A();
        a.s1 = 1;
        a.s2 = 2;
        a.s3 = 3;
        a.s4 = 4;
        a.s5 = 5;
        for (int i = 0; i < 1000; i++) {
            buffer.clear();
            serialization.write(buffer, a);
            buffer.flip();
            serialization.read(buffer);
        }
    }
}
