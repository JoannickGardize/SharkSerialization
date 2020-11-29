package com.sharkhendrix.serialization.annotation;

import com.sharkhendrix.util.VarLenNumberIO;

/**
 * Represents the possible varying length quantity strategies implemented by
 * {@link VarLenNumberIO} for eligible primitive number types (at least
 * {@code int} and {@code long}).
 * 
 * @author Joannick Gardize
 *
 */
public enum VarLenStrategy {
    /**
     * Default varying length compression, symmetric between negative and positive
     * values. Or no compression if the primitive type is not eligible for it.
     */
    NORMAL,
    /**
     * Small positive values (and zero) will take less bytes, but negative values
     * will always take {@code size of the primitive + 1} bytes.
     */
    POSITIVE,
    /**
     * Disable varying length compression, faster to serializer / deserialize, but
     * will always take {@code size of the primitive} bytes.
     */
    NONE;
}
