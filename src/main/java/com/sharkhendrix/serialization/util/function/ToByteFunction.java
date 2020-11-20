package com.sharkhendrix.serialization.util.function;

public interface ToByteFunction<T> {

    byte apply(T value);
}
