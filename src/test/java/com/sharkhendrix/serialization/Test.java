package com.sharkhendrix.serialization;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

public class Test {

    private Map<Integer, String> attr;

    private Integer i;

    public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        System.out.println(Arrays.toString(((ParameterizedType) Test.class.getDeclaredField("attr").getGenericType()).getActualTypeArguments()));
    }
}
