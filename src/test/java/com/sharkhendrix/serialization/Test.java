package com.sharkhendrix.serialization;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class Test {

    private List<Integer> attr;

    private Integer i;

    public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        System.out.println(((Class<?>) ((ParameterizedType) Test.class.getDeclaredField("attr").getGenericType()).getActualTypeArguments()[0]).getSimpleName());
    }
}
