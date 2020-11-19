package com.sharkhendrix.serialization.benchmark;

public class BenchmarkUtils {

    private BenchmarkUtils() {
    }

    public static void chrono(String title, Runnable runnable) {
        long time = System.currentTimeMillis();
        runnable.run();
        System.out.println(title + ": " + (System.currentTimeMillis() - time) + " ms");
    }
}
