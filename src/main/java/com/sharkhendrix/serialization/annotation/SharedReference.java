package com.sharkhendrix.serialization.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that the reference of the object's field could be shared elsewhere
 * in the context of the same object's graph.
 * 
 * @author Joannick Gardize
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface SharedReference {

}
