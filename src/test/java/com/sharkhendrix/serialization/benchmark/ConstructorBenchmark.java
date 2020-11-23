package com.sharkhendrix.serialization.benchmark;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class ConstructorBenchmark {

    static class A {

        static int i;

        A() {
            i++;
        }
    }

    public static void main(String[] args) {
        BenchmarkUtils.chrono("reflection", ConstructorBenchmark::reflectionConstructor);
        BenchmarkUtils.chrono("method", ConstructorBenchmark::methodConstructor);
    }

    public static void reflectionConstructor() {
        try {
            Constructor<A> constructor = A.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            for (int i = 0; i < 10_000_000; i++) {
                constructor.newInstance();
            }
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void methodConstructor() {

        Supplier<A> constructor = A::new;
        for (int i = 0; i < 10_000_000; i++) {
            constructor.get();
        }
    }
}
