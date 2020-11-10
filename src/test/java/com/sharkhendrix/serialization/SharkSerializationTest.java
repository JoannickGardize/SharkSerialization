package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.sharkhendrix.serialization.serializer.ObjectSerializer;

public class SharkSerializationTest {

	private static class A {
		String s;
		int i;
	}

	@Test
	public void test() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(A.class, new ObjectSerializer<>(A::new));
		serialization.initialize();
		A a = new A();
		a.s = "test";
		a.i = 42;
		ByteBuffer bb = ByteBuffer.allocate(1024);
		serialization.write(bb, a);
		bb.position(0);
		A a2 = (A) serialization.read(bb);
		Assertions.assertNotSame(a2, a);
		Assertions.assertEquals(a.s, a2.s);
		Assertions.assertEquals(a.i, a2.i);
	}
}
