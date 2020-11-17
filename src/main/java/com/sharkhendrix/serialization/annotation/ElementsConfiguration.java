package com.sharkhendrix.serialization.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.lang.model.type.NullType;

@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ElementsConfigurationGroup.class)
public @interface ElementsConfiguration {

    boolean sharedReference() default false;

    boolean undefinedType() default false;

    Class<?> type() default NullType.class;
}