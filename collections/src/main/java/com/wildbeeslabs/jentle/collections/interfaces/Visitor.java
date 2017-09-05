package com.wildbeeslabs.jentle.collections.interfaces;

/**
 *
 * Visitor interface declaration
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface Visitor<T> {
    /**
     * Process item in collection
     * @param item 
     */
    public void visit(final T item);
}
