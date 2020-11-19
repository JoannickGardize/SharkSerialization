package com.sharkhendrix.serialization.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the object's field could reference an instance of a different
 * type than the declared one, or at least in unexpected for a given context.
 * 
 * @author Joannick Gardize
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UndefinedType {

}
