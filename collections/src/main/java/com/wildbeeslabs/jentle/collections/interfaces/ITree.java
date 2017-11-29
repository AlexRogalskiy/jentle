package com.wildbeeslabs.jentle.collections.interfaces;

/**
 *
 * Custom tree interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface ITree<T> {

    /**
     * Get the size of the tree.
     *
     * @return size of the tree.
     */
    public int size();

    /**
     * Check if the list contains values.
     *
     * @return boolean (true - if the list is empty, false - otherwise)
     */
    public boolean isEmpty();
}
