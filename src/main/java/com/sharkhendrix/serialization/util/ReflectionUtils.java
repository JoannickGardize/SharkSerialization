package com.sharkhendrix.serialization.util;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

    private ReflectionUtils() {
    }

    /**
     * @param type
     * @return True if the given class is a "normal" and instanciable class.
     */
    public static boolean isInstanciable(Class<?> type) {
        return !type.isAnnotation() && !type.isArray() && !type.isEnum() && !type.isInterface() && !type.isPrimitive() && !Modifier.isAbstract(type.getModifiers());
    }

    public static Class<?>[] getComponentTypeHierarchy(Field field) {
        try {
            Type[] types = getSubTypes(field.getGenericType());
            List<Class<?>> result = new ArrayList<>();
            while (types.length > 0) {
                // TODO handle multiple subtypes
                result.add(getRawClass(types[0]));
                types = getSubTypes(types[0]);
            }
            return result.toArray(Class[]::new);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Type[] getSubTypes(Type type) {
        if (type instanceof Class) {
            if (((Class<?>) type).isArray()) {
                return new Type[] { ((Class<?>) type).getComponentType() };
            } else {
                return new Type[0];
            }
        } else if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        } else if (type instanceof GenericArrayType) {
            return new Type[] { ((GenericArrayType) type).getGenericComponentType() };
        } else {
            throw new UnsupportedOperationException("Unsupported type : " + type);
        }
    }

    public static Class<?> getRawClass(Type type) throws ClassNotFoundException {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof GenericArrayType) {
            return getArrayClass((GenericArrayType) type);
        } else {
            throw new UnsupportedOperationException("Unsupported type : " + type);
        }
    }

    public static Class<?> getArrayClass(GenericArrayType type) throws ClassNotFoundException {
        return getArrayClass("", type);
    }

    public static Class<?> getArrayClass(String preffix, GenericArrayType type) throws ClassNotFoundException {
        Type subType = type.getGenericComponentType();
        if (subType instanceof GenericArrayType) {
            return getArrayClass("[", (GenericArrayType) subType);
        } else if (subType instanceof ParameterizedType) {
            return Class.forName(preffix + "[L" + ((Class<?>) ((ParameterizedType) subType).getRawType()).getName() + ";");
        } else {
            return Class.forName(preffix + "[L" + ((Class<?>) subType).getName() + ";");
        }
    }
}
