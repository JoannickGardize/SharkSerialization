package com.sharkhendrix.serialization;

import com.sharkhendrix.serialization.annotation.SharedReference;
import com.sharkhendrix.serialization.serializer.SharedReferenceSerializer;

/**
 * <p>
 * Object reference context interface, used to store and retrieve
 * multi-referenced instances. IDs are short values in order to minimize the
 * serialization size.
 * <p>
 * IDs must always be greater than zero to satisfy the internal process of
 * {@link SharedReferenceSerializer}.
 * <p>
 * Manage two independents stores :
 * <ul>
 * <li>{@link #store(Object)} and {@link #retrieve(Object)} for serialization
 * <li>{@link #store(short, Object)} and {@link #retrieve(short)} for
 * derialization
 * </ul>
 * 
 * @author Joannick Gardize
 *
 */
public interface ReferenceContext {

    /**
     * Prepare this ReferenceContext to start a new object serialization.
     */
    void resetWriteContext();

    /**
     * Prepare this ReferenceContext to start a new object deserialization.
     */
    void resetReadContext();

    /**
     * Used when serializing an object's field annotated with
     * {@link SharedReference}
     * 
     * @param o
     * @return the associated id of o, or -1 if there is no associated id.
     */
    short retrieve(Object o);

    /**
     * Used when serializing the first time an object's field annotated with
     * {@link SharedReference}. It must generate a different id for each different
     * object. The id must always be greater than 0.
     * 
     * @param o
     * @return the newly associated id. The first generated id should starts from 1.
     */
    short store(Object o);

    /**
     * Used when deserializing an object's field annotated with
     * {@link SharedReference}
     * 
     * @param id associated id, must be > 0
     * @param o
     */
    void store(short id, Object o);

    /**
     * Used when deserializing an object's field annotated with
     * {@link SharedReference}
     * 
     * @param id
     * @return
     */
    Object retrieve(short id);
}
