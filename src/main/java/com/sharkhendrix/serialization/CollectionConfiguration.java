package com.sharkhendrix.serialization;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(CollectionConfigurationGroup.class)
public @interface CollectionConfiguration {

    boolean sharedReference() default false;

    boolean undefinedType() default false;
}