package com.sharkhendrix.serialization.benchmark;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.SharkSerialization;

public class MethodVsFieldAccessBenchmark {

    private static class A {
        String s1;
        String s2;
        String s3;
        String s4;
        String s5;

        public String getS1() {
            return s1;
        }

        public void setS1(String s1) {
            this.s1 = s1;
        }

        public String getS2() {
            return s2;
        }

        public void setS2(String s2) {
            this.s2 = s2;
        }

        public String getS3() {
            return s3;
        }

        public void setS3(String s3) {
            this.s3 = s3;
        }

        public String getS4() {
            return s4;
        }

        public void setS4(String s4) {
            this.s4 = s4;
        }

        public String getS5() {
            return s5;
        }

        public void setS5(String s5) {
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
        serialization.registerObject(A.class, A::new).configure().access(A::getS1, A::setS1).access(A::getS2, A::setS2).access(A::getS3, A::setS3).access(A::getS4, A::setS4)
                .access(A::getS5, A::setS5);
        execute(serialization);
    }

    private static void execute(SharkSerialization serialization) {
        serialization.initialize();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        A a = new A();
        a.s1 = "test";
        a.s2 = "test2";
        a.s3 = "test3";
        a.s4 = "test4";
        a.s5 = "test5";
        for (int i = 0; i < 1000; i++) {
            buffer.clear();
            serialization.write(buffer, a);
            buffer.flip();
            serialization.read(buffer);
        }
    }
}
