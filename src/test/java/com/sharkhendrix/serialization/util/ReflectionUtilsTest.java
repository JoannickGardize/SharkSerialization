package com.sharkhendrix.serialization.util;

import java.util.List;
import java.util.Map;

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

    @SuppressWarnings("unused")
    private List<List<Map<Class<?>, String[]>>[][]> arrayListMapStorm;

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
        ComponentTypeHierarchy cth = ReflectionUtils.getComponentTypeHierarchy(getClass().getDeclaredField("arrayListMapStorm"));
        Assertions.assertEquals(List.class, cth.getType());
        Assertions.assertNull(cth.getKeys());
        Assertions.assertNull(cth.getValues());
        Assertions.assertEquals(List[][].class, cth.getElements().getType());
        Assertions.assertNull(cth.getElements().getKeys());
        Assertions.assertNull(cth.getElements().getValues());
        Assertions.assertEquals(List[].class, cth.getElements().getElements().getType());
        Assertions.assertEquals(List.class, cth.getElements().getElements().getElements().getType());
        Assertions.assertEquals(Map.class, cth.getElements().getElements().getElements().getElements().getType());
        Assertions.assertNull(cth.getElements().getElements().getElements().getElements().getElements());
        Assertions.assertEquals(Class.class, cth.getElements().getElements().getElements().getElements().getKeys().getType());
        Assertions.assertEquals(String[].class, cth.getElements().getElements().getElements().getElements().getValues().getType());
        Assertions.assertEquals(String.class, cth.getElements().getElements().getElements().getElements().getValues().getElements().getType());
    }
}
