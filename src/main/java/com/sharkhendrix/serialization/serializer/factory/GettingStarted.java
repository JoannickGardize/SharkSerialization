package com.sharkhendrix.serialization.serializer.factory;

import java.nio.ByteBuffer;

import com.sharkhendrix.serialization.SharkSerialization;

public class GettingStarted {

    static class A {
        B b;
    }

    static class B {
        int data;
    }

    public static void main(String[] args) {

        // Initialize the serializer
        SharkSerialization serialization = new SharkSerialization();
        serialization.registerObject(A.class, A::new);
        serialization.registerObject(B.class, B::new);
        serialization.initialize();

        // Build data
        A a = new A();
        a.b = new B();
        a.b.data = 42;

        // Serialize data and deserialize data
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        serialization.write(buffer, a);

        buffer.flip();
        System.out.println("data size : " + buffer.limit() + " bytes");

        A a2 = (A) serialization.read(buffer);
        System.out.println("data : " + a2.b.data);
    }
}
