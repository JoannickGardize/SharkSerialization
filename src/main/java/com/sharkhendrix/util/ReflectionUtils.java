package com.sharkhendrix.util;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReflectionUtils {

    private static Set<Class<?>> primitiveWrappers = new HashSet<>();

    static {
        primitiveWrappers.add(Byte.class);
        primitiveWrappers.add(Character.class);
        primitiveWrappers.add(Boolean.class);
        primitiveWrappers.add(Short.class);
        primitiveWrappers.add(Integer.class);
        primitiveWrappers.add(Long.class);
        primitiveWrappers.add(Float.class);
        primitiveWrappers.add(Double.class);
    }

    private ReflectionUtils() {
    }

    public static boolean isPrimitiveWrapper(Class<?> type) {
        return primitiveWrappers.contains(type);
    }

    /**
     * @param type
     * @return true if the given class is a "normal" and instanciable class.
     */
    public static boolean isInstanciable(Class<?> type) {
        return !type.isAnnotation() && !type.isArray() && !type.isEnum() && !type.isInterface() && !type.isPrimitive() && !Modifier.isAbstract(type.getModifiers());
    }

    /**
     * Get all declared fields of the type and its ancestors
     * 
     * @param type the type to get all its fields
     * @return all the declared fields in the class and its parents
     */
    public static Field[] getAllFields(Class<?> type) {
        Class<?> currentClass = type;
        List<Field> fields = new ArrayList<>();
        while (currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return fields.toArray(Field[]::new);
    }

    /**
     * Calls {@link #getComponentTypeHierarchy(Field, Class...)} as follow:
     * {@code getComponentTypeHierarchy(field, Collection.class, Map.class)}.
     * <p>
     * If the field has unsupported generic types, the return value will only
     * contain one entry with {@link Field#getType()} as type.
     * 
     * @param field the field to get its component type hierarchy
     * @return the root of the component type hierarchy
     * @throws UnsupportedComponentTypeHierarchyException
     */
    public static ComponentTypeHierarchy getComponentTypeHierarchy(Field field) {
        try {
            return getComponentTypeHierarchy(field, Collection.class, Map.class);
        } catch (UnsupportedComponentTypeHierarchyException e) {
            return new ComponentTypeHierarchy(field.getType());
        }
    }

    /**
     * <p>
     * Returns the component hierarchy of the given field according to it's declared
     * type. Supports arrays, collections and maps.
     * <p>
     * types with one generic argument are treated as collections, types with two
     * generic arguments are treated as maps.
     * <p>
     * Does not supports wildcard generics.
     * 
     * @param field                        the field to get its component type
     *                                     hierarchy
     * @param genericComponentSuperclasses only generic type that inherit of one of
     *                                     this argument's class are considered as
     *                                     collection or map.
     * @return the root of the component type hierarchy
     * @throws UnsupportedComponentTypeHierarchyException
     */
    public static ComponentTypeHierarchy getComponentTypeHierarchy(Field field, Class<?>... genericComponentSuperclasses) throws UnsupportedComponentTypeHierarchyException {
        try {
            return analyzeType(field.getGenericType(), Arrays.asList(genericComponentSuperclasses));
        } catch (ClassNotFoundException e) {
            throw new UnsupportedComponentTypeHierarchyException(e);
        }
    }

    private static ComponentTypeHierarchy analyzeType(Type type, Collection<Class<?>> genericComponentSuperclasses)
            throws ClassNotFoundException, UnsupportedComponentTypeHierarchyException {
        if (type instanceof Class) {
            ComponentTypeHierarchy componentTypeHierarchy = new ComponentTypeHierarchy((Class<?>) type);
            if (componentTypeHierarchy.getType().isArray()) {
                componentTypeHierarchy.setElements(analyzeType(componentTypeHierarchy.getType().getComponentType(), genericComponentSuperclasses));
            }
            return componentTypeHierarchy;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            ComponentTypeHierarchy componentTypeHierarchy = new ComponentTypeHierarchy((Class<?>) parameterizedType.getRawType());
            if (!genericComponentSuperclasses.stream().anyMatch(c -> c.isAssignableFrom(componentTypeHierarchy.getType()))) {
                return componentTypeHierarchy;
            }
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length == 1) {
                componentTypeHierarchy.setElements(analyzeType(typeArguments[0], genericComponentSuperclasses));
            } else if (typeArguments.length == 2) {
                componentTypeHierarchy.setKeys(analyzeType(typeArguments[0], genericComponentSuperclasses));
                componentTypeHierarchy.setValues(analyzeType(typeArguments[1], genericComponentSuperclasses));
            } else {
                throw new UnsupportedComponentTypeHierarchyException(
                        "Wrong number of generic argument for type \"" + type + "\", It must have one argument for collection types or 2 arguments for map types.");
            }
            return componentTypeHierarchy;
        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            ComponentTypeHierarchy componentTypeHierarchy = new ComponentTypeHierarchy(getArrayClass(genericArrayType));
            componentTypeHierarchy.setElements(analyzeType(genericArrayType.getGenericComponentType(), genericComponentSuperclasses));
            return componentTypeHierarchy;
        } else {
            throw new UnsupportedComponentTypeHierarchyException("Unsupported generic type : " + type);
        }
    }

    private static Class<?> getArrayClass(GenericArrayType type) throws ClassNotFoundException {
        return getArrayClass("", type);
    }

    private static Class<?> getArrayClass(String preffix, GenericArrayType type) throws ClassNotFoundException {
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
