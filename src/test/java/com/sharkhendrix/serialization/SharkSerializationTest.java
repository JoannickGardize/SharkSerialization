package com.sharkhendrix.serialization;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import com.sharkhendrix.serialization.SharkSerializationTestModel.A;
import com.sharkhendrix.serialization.SharkSerializationTestModel.AbstractType;
import com.sharkhendrix.serialization.SharkSerializationTestModel.B;
import com.sharkhendrix.serialization.SharkSerializationTestModel.C;
import com.sharkhendrix.serialization.SharkSerializationTestModel.ConfiguredArraysClass;
import com.sharkhendrix.serialization.SharkSerializationTestModel.CyclicSharedReferenceClassA;
import com.sharkhendrix.serialization.SharkSerializationTestModel.CyclicSharedReferenceClassB;
import com.sharkhendrix.serialization.SharkSerializationTestModel.CyclicSharedReferenceWrapper;
import com.sharkhendrix.serialization.SharkSerializationTestModel.ImplementationClass;
import com.sharkhendrix.serialization.SharkSerializationTestModel.PrimitiveArrayClass;
import com.sharkhendrix.serialization.SharkSerializationTestModel.PrimitiveClass;
import com.sharkhendrix.serialization.SharkSerializationTestModel.SharedUndefinedFieldClass;
import com.sharkhendrix.serialization.SharkSerializationTestModel.SimpleSharedReferenceClass;
import com.sharkhendrix.serialization.SharkSerializationTestModel.TransientFieldClass;
import com.sharkhendrix.serialization.SharkSerializationTestModel.UndefinedFieldsClass;
import com.sharkhendrix.serialization.SharkSerializationTestModel.WrapperClass;

public class SharkSerializationTest {

    @BeforeEach
    public void log(TestInfo info) {
        System.out.println(info.getDisplayName());
    }

    @Test
    public void primitivesSerializationTest() {
        SharkSerialization serialization = new SharkSerialization();
        serialization.register(PrimitiveClass.class, PrimitiveClass::new);
        serialization.initialize();
        PrimitiveClass a = new PrimitiveClass();
        a.c = 'a';
        a.bo = true;
        a.st = "test";
        a.b = 12;
        a.sh = 15;
        a.i = 42;
        a.l = 123;
        a.d = 1.1;
        a.f = 23.2f;
        PrimitiveClass a2 = writeAndRead(serialization, a);
        Assertions.assertNotSame(a2, a);
        Assertions.assertEquals(a.c, a2.c);
        Assertions.assertEquals(a.bo, a2.bo);
        Assertions.assertEquals(a.st, a2.st);
        Assertions.assertEquals(a.b, a2.b);
        Assertions.assertEquals(a.sh, a2.sh);
        Assertions.assertEquals(a.i, a2.i);
        Assertions.assertEquals(a.l, a2.l);
        Assertions.assertEquals(a.d, a2.d);
        Assertions.assertEquals(a.f, a2.f);
    }

    @Test
    public void primitiveArraysSerializationTest() {
        SharkSerialization serialization = new SharkSerialization();
        serialization.register(PrimitiveArrayClass.class, PrimitiveArrayClass::new);
        serialization.initialize();
        PrimitiveArrayClass a = new PrimitiveArrayClass();
        a.c = new char[] { 'a' };
        a.bo = new boolean[] { true, false };
        a.b = new byte[] { 12 };
        a.sh = new short[] { 15, 12, 13 };
        a.i = new int[] { 42 };
        a.l = new long[] { 123 };
        a.d = new double[] { 1.1 };
        a.f = new float[] { 23.2f };
        PrimitiveArrayClass a2 = writeAndRead(serialization, a);
        Assertions.assertArrayEquals(a.c, a2.c);
        Assertions.assertArrayEquals(a.bo, a2.bo);
        Assertions.assertArrayEquals(a.b, a2.b);
        Assertions.assertArrayEquals(a.sh, a2.sh);
        Assertions.assertArrayEquals(a.i, a2.i);
        Assertions.assertArrayEquals(a.l, a2.l);
        Assertions.assertArrayEquals(a.d, a2.d);
        Assertions.assertArrayEquals(a.f, a2.f);
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
        serialization.register(List.class, () -> new ArrayList<>());
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
        a.c5 = c2;
        SimpleSharedReferenceClass a2 = writeAndRead(serialization, a);
        Assertions.assertNotSame(a2.c, a2.c2);
        Assertions.assertSame(a2.c, a2.c3);
        Assertions.assertSame(a2.c2, a2.c4);
        Assertions.assertNotSame(a2.c4, a2.c5);
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

    @Test
    public void configuredArraysClassTest() {
        SharkSerialization serialization = new SharkSerialization();
        serialization.register(ConfiguredArraysClass.class, ConfiguredArraysClass::new);
        serialization.registerConstructor(AbstractType[].class, AbstractType[]::new);
        serialization.registerConstructor(AbstractType[][].class, AbstractType[][]::new);
        serialization.register(ImplementationClass.class, ImplementationClass::new);
        serialization.initialize();
        ImplementationClass c = new ImplementationClass();
        ImplementationClass c2 = new ImplementationClass();
        AbstractType[] array1 = new AbstractType[] { c, c, null, c2 };
        AbstractType[] array2 = new AbstractType[] { null, c };
        AbstractType[][] array2d = new AbstractType[][] { array1, array2 };
        ConfiguredArraysClass object = new ConfiguredArraysClass();
        object.array2d = array2d;
        object.array2dCopy = array2d;
        object.arrayCopy = array2;
        object.arrayNotCopy = array1;

        ConfiguredArraysClass object2 = writeAndRead(serialization, object);
        Assertions.assertEquals(2, object2.array2d.length);
        Assertions.assertEquals(4, object2.array2d[0].length);
        Assertions.assertEquals(2, object2.array2d[1].length);
        Assertions.assertEquals(ImplementationClass.class, object2.array2d[0][0].getClass());
        Assertions.assertSame(object2.array2d[0][0], object2.array2d[0][1]);
        Assertions.assertNull(object2.array2d[0][2]);
        Assertions.assertEquals(ImplementationClass.class, object2.array2d[0][3].getClass());
        Assertions.assertNotSame(object2.array2d[0][0], object2.array2d[0][3]);
        Assertions.assertSame(object2.array2d, object2.array2dCopy);
        Assertions.assertSame(object2.array2d[1], object2.arrayCopy);
        Assertions.assertNotSame(object2.arrayCopy, object2.arrayNotCopy);
        Assertions.assertNotSame(object2.arrayCopy[1], object2.arrayNotCopy[1]);
    }

    @SuppressWarnings("unchecked")
    private <T> T writeAndRead(SharkSerialization serialization, T initial) {
        ByteBuffer bb = ByteBuffer.allocate(65536);
        serialization.write(bb, initial);
        bb.flip();
        System.out.println(bb.limit());
        T object = (T) serialization.read(bb);
        return object;
    }
}
