package com.wildbeeslabs.jentle.collections.interfaces;

/**
 *
 * ResultVisitor interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <E>
 */
public interface ResultVisitor<T, E> extends Visitor<T> {

    /**
     * Get visitor result of a collection
     *
     */
    public E getResult();
}
