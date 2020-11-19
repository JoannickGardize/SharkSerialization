package com.sharkhendrix.serialization.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to configure serialization of object's fields of array or Collection
 * types.
 * 
 * @author Joannick Gardize
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ElementsConfigurationGroup.class)
public @interface ElementsConfiguration {

    ElementsConfigurationType type() default ElementsConfigurationType.ELEMENTS;

    boolean sharedReference() default false;

    boolean undefinedType() default false;

    Class<?> concreteType() default void.class;
}