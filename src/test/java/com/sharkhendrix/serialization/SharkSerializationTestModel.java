package com.sharkhendrix.serialization;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.sharkhendrix.serialization.annotation.ConcreteType;
import com.sharkhendrix.serialization.annotation.ElementsConfiguration;
import com.sharkhendrix.serialization.annotation.ElementsConfigurationType;
import com.sharkhendrix.serialization.annotation.SharedReference;
import com.sharkhendrix.serialization.annotation.UndefinedType;

public class SharkSerializationTestModel {
    public static class PrimitiveClass {
        char c;
        boolean bo;
        String st;
        byte b;
        short sh;
        int i;
        long l;
        double d;
        float f;
    }

    public static class PrimitiveArrayClass {
        char[] c;
        boolean[] bo;
        byte[] b;
        short[] sh;
        int[] i;
        long[] l;
        double[] d;
        float[] f;
    }

    public static class WrapperClass {
        Byte b;
        Short s;
        Integer i;
        Long l;
        Double d;
        Float f;
    }

    public static class A {
        B b;
        C c;
    }

    public static class B {
        C c;
    }

    public static class C {
        int i;
    }

    public static class UndefinedFieldsClass {

        @UndefinedType
        AbstractType a;

        @UndefinedType
        AbstractType b;
    }

    public static interface AbstractType {

    }

    public static class ImplementationClass implements AbstractType {

    }

    public static class TransientFieldClass {
        transient int i;
    }

    public static class SimpleSharedReferenceClass {

        @SharedReference
        C c;

        @SharedReference
        C c2;

        @SharedReference
        C c3;

        @SharedReference
        C c4;

        C c5;
    }

    public static class CyclicSharedReferenceWrapper {
        @SharedReference
        CyclicSharedReferenceClassA a;
    }

    public static class CyclicSharedReferenceClassA {

        @SharedReference
        CyclicSharedReferenceClassB b;
    }

    public static class CyclicSharedReferenceClassB {
        @SharedReference
        CyclicSharedReferenceClassA a;
    }

    public static class SharedUndefinedFieldClass {

        @UndefinedType
        @SharedReference
        AbstractType a;

        @UndefinedType
        @SharedReference
        AbstractType b;
    }

    public static class ConfiguredArraysClass {

        @SharedReference
        @ElementsConfiguration(sharedReference = true)
        @ElementsConfiguration(sharedReference = true, undefinedType = true)
        AbstractType[][] array2d;

        @SharedReference
        @ElementsConfiguration(sharedReference = true)
        @ElementsConfiguration(sharedReference = true, undefinedType = true)
        AbstractType[][] array2dCopy;

        @SharedReference
        @ElementsConfiguration(sharedReference = true, undefinedType = true)
        AbstractType[] arrayCopy;

        @ElementsConfiguration(sharedReference = false, undefinedType = true)
        AbstractType[] arrayNotCopy;

    }

    public static class CollectionsClass {
        Collection<Integer> aCollection;
        List<String> aList;
    }

    public static class ParameterizedListClass {
        @SharedReference
        @ElementsConfiguration
        @ElementsConfiguration(undefinedType = true)
        List<List<Object>> the3DCollection;

        @SharedReference
        Object the3DCollectionCopy;
    }

    public static class ConcreteTypeClass {

        @ConcreteType(Object[].class)
        @ElementsConfiguration(concreteType = String.class)
        Object o;

        @ConcreteType(String.class)
        Object aString;
    }

    public static class ArrayCollectionHybridClass {
        List<List<String>[]> list;
    }

    public static class MapClass {

        @ElementsConfiguration(type = ElementsConfigurationType.KEYS, concreteType = List.class)
        @ElementsConfiguration(concreteType = String.class)
        @ElementsConfiguration(type = ElementsConfigurationType.VALUES)
        Map<Object, Map<String, String>> map;
    }

}
