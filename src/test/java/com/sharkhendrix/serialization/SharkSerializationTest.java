package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SharkSerializationTest {

	private static class PrimitiveClass {
		String st;
		byte b;
		short sh;
		int i;
		long l;
		double d;
		float f;
	}

	private static class WrapperClass {
		Byte b;
		Short s;
		Integer i;
		Long l;
		Double d;
		Float f;
	}

	private static class A {
		B b;
		C c;
	}

	private static class B {
		C c;
	}

	private static class C {
		int i;
	}

	private static class UndefinedFieldsClass {

		@UndefinedType
		AbstractType a;

		@UndefinedType
		AbstractType b;
	}

	private static interface AbstractType {

	}

	private static class ImplementationClass implements AbstractType {

	}

	private static class TransientFieldClass {
		transient int i;
	}

	private static class SimpleSharedReferenceClass {

		@SharedReference
		C c;

		@SharedReference
		C c2;

		@SharedReference
		C c3;

		@SharedReference
		C c4;
	}

	private static class CyclicSharedReferenceWrapper {
		@SharedReference
		CyclicSharedReferenceClassA a;
	}

	private static class CyclicSharedReferenceClassA {

		@SharedReference
		CyclicSharedReferenceClassB b;
	}

	private static class CyclicSharedReferenceClassB {
		@SharedReference
		CyclicSharedReferenceClassA a;
	}

	private static class SharedUndefinedFieldClass {

		@UndefinedType
		@SharedReference
		AbstractType a;

		@UndefinedType
		@SharedReference
		AbstractType b;
	}

	@Test
	public void primitivesSerializationTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(PrimitiveClass.class, PrimitiveClass::new);
		serialization.initialize();
		PrimitiveClass a = new PrimitiveClass();
		a.st = "test";
		a.b = 12;
		a.sh = 15;
		a.i = 42;
		a.l = 123;
		a.d = 1.1;
		a.f = 23.2f;
		PrimitiveClass a2 = writeAndRead(serialization, a);
		Assertions.assertNotSame(a2, a);
		Assertions.assertEquals(a.st, a2.st);
		Assertions.assertEquals(a.b, a2.b);
		Assertions.assertEquals(a.sh, a2.sh);
		Assertions.assertEquals(a.i, a2.i);
		Assertions.assertEquals(a.l, a2.l);
		Assertions.assertEquals(a.d, a2.d);
		Assertions.assertEquals(a.f, a2.f);
	}

	@Test
	public void wrappersSerializationTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(WrapperClass.class, WrapperClass::new);
		serialization.initialize();
		WrapperClass a = new WrapperClass();
		a.b = 12;
		a.s = 15;
		a.i = 42;
		a.l = 123L;
		a.d = 1.1;
		a.f = 23.2f;
		WrapperClass a2 = writeAndRead(serialization, a);
		Assertions.assertNotSame(a2, a);
		Assertions.assertEquals(a.b, a2.b);
		Assertions.assertEquals(a.s, a2.s);
		Assertions.assertEquals(a.i, a2.i);
		Assertions.assertEquals(a.l, a2.l);
		Assertions.assertEquals(a.d, a2.d);
		Assertions.assertEquals(a.f, a2.f);
	}

	@Test
	public void objectHierarchySerializationTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(A.class, A::new);
		serialization.register(B.class, B::new);
		serialization.register(C.class, C::new);
		serialization.initialize();
		A a = new A();
		a.c = new C();
		a.c.i = 12;
		a.b = new B();
		a.b.c = new C();
		a.b.c.i = 42;
		A a2 = writeAndRead(serialization, a);
		Assertions.assertEquals(a.c.i, a2.c.i);
		Assertions.assertEquals(a.b.c.i, a2.b.c.i);
	}

	@Test
	public void undefinedFieldTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(UndefinedFieldsClass.class, UndefinedFieldsClass::new);
		serialization.register(ImplementationClass.class, ImplementationClass::new);
		serialization.initialize();
		UndefinedFieldsClass a = new UndefinedFieldsClass();
		a.b = new ImplementationClass();
		UndefinedFieldsClass a2 = writeAndRead(serialization, a);
		Assertions.assertNull(a.a);
		Assertions.assertSame(ImplementationClass.class, a2.b.getClass());
	}

	@Test
	public void registerInterfaceFailTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(List.class, ArrayList::new);
		Assertions.assertThrows(IllegalArgumentException.class, serialization::initialize);
	}

	@Test
	public void transientFieldTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(TransientFieldClass.class, TransientFieldClass::new);
		TransientFieldClass transientFieldClass = new TransientFieldClass();
		serialization.initialize();
		transientFieldClass.i = 12;
		TransientFieldClass transientFieldClass2 = writeAndRead(serialization, transientFieldClass);
		Assertions.assertEquals(transientFieldClass2.i, 0);
	}

	@Test
	public void simpleSharedReferenceTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(SimpleSharedReferenceClass.class, SimpleSharedReferenceClass::new);
		serialization.register(C.class, C::new);
		serialization.initialize();
		SimpleSharedReferenceClass a = new SimpleSharedReferenceClass();
		C c = new C();
		c.i = 12;
		C c2 = new C();
		c2.i = 13;
		a.c = c;
		a.c2 = c2;
		a.c3 = c;
		a.c4 = c2;
		SimpleSharedReferenceClass a2 = writeAndRead(serialization, a);
		Assertions.assertNotSame(a2.c, a2.c2);
		Assertions.assertSame(a2.c, a2.c3);
		Assertions.assertSame(a2.c2, a2.c4);
		Assertions.assertEquals(a2.c.i, 12);
		Assertions.assertEquals(a2.c2.i, 13);
	}

	@Test
	public void cyclicSharedReferenceTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(CyclicSharedReferenceClassA.class, CyclicSharedReferenceClassA::new);
		serialization.register(CyclicSharedReferenceClassB.class, CyclicSharedReferenceClassB::new);
		serialization.register(CyclicSharedReferenceWrapper.class, CyclicSharedReferenceWrapper::new);
		serialization.initialize();

		CyclicSharedReferenceClassA a = new CyclicSharedReferenceClassA();
		CyclicSharedReferenceClassB b = new CyclicSharedReferenceClassB();
		a.b = b;
		b.a = a;
		CyclicSharedReferenceWrapper wrapper = new CyclicSharedReferenceWrapper();
		wrapper.a = a;
		CyclicSharedReferenceWrapper wrapper2 = writeAndRead(serialization, wrapper);
		Assertions.assertSame(wrapper2.a, wrapper2.a.b.a);
		Assertions.assertSame(wrapper2.a.b, wrapper2.a.b.a.b);
	}

	@Test
	public void sharedUndefinedFieldTest() {
		SharkSerialization serialization = new SharkSerialization();
		serialization.register(SharedUndefinedFieldClass.class, SharedUndefinedFieldClass::new);
		serialization.register(ImplementationClass.class, ImplementationClass::new);
		serialization.initialize();
		SharedUndefinedFieldClass a = new SharedUndefinedFieldClass();
		AbstractType attr = new ImplementationClass();
		a.a = attr;
		a.b = attr;

		SharedUndefinedFieldClass a2 = writeAndRead(serialization, a);
		Assertions.assertSame(ImplementationClass.class, a2.a.getClass());
		Assertions.assertSame(a2.a, a2.b);
	}

	@SuppressWarnings("unchecked")
	private <T> T writeAndRead(SharkSerialization serialization, T initial) {
		ByteBuffer bb = ByteBuffer.allocate(65536);
		serialization.write(bb, initial);
		bb.flip();
		T object = (T) serialization.read(bb);
		return object;
	}
}
