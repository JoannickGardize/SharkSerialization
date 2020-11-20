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

            A a = null;
            for (int i = 0; i < 10_000_000; i++) {
                a = constructor.newInstance();
            }
            System.out.println(A.i);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void methodConstructor() {

        A a = null;
        Supplier<A> constructor = A::new;
        for (int i = 0; i < 10_000_000; i++) {
            a = constructor.get();
        }
        System.out.println(A.i);
    }
}
