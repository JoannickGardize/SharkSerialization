package com.sharkhendrix.serialization;

public @interface MapComplexity {

    boolean keySharedReference() default false;

    boolean keyUndefinedType() default false;

    boolean valueUndefinedType() default false;

    boolean valueSharedReference() default false;

}
