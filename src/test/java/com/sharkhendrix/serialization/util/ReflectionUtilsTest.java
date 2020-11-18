package com.sharkhendrix.serialization.util;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ReflectionUtilsTest {

    private static @interface AnAnnototation {

    }

    private static interface AnInterface {

    }

    private static abstract class AnAbstractClass {

    }

    private static enum AnEnum {

    }

    private static class ANormalClass {

    }

    private List<List<List<String>[]>[][]> arrayOfListStorm;

    @ParameterizedTest
    @ValueSource(classes = { int.class, Integer.class, AnAnnototation.class, AnInterface.class, AnAbstractClass.class, AnEnum.class })
    public void isInstanciableFalseTest() {
        Assertions.assertFalse(ReflectionUtils.isInstanciable(int.class));
    }

    @Test
    public void isInstanciableTrueTest() {
        Assertions.assertTrue(ReflectionUtils.isInstanciable(ANormalClass.class));
    }

    @Test
    public void getComponentTypeHierarchyTest() throws NoSuchFieldException, SecurityException {
        Assertions.assertArrayEquals(new Class[] { List[][].class, List[].class, List.class, List[].class, List.class, String.class },
                ReflectionUtils.getComponentTypeHierarchy(getClass().getDeclaredField("arrayOfListStorm")));
    }
}
