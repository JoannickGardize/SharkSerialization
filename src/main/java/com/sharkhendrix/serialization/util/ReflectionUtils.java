package com.sharkhendrix.serialization.util;

import java.lang.reflect.Modifier;

public class ReflectionUtils {

	private ReflectionUtils() {
	}

	/**
	 * @param type
	 * @return True if the given class is a "normal" and instanciable class.
	 */
	public static boolean isInstanciable(Class<?> type) {
		return !type.isAnnotation() && !type.isArray() && !type.isEnum() && !type.isInterface() && !type.isPrimitive()
				&& !Modifier.isAbstract(type.getModifiers());
	}
}
