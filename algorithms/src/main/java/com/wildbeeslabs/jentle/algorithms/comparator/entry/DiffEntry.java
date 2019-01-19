package com.wildbeeslabs.jentle.algorithms.comparator.entry;

import java.io.Serializable;

/**
 * Difference entry declaration
 */
public interface DiffEntry<T> extends Serializable {

    /**
     * Returns property name {@link String} to compare by
     *
     * @return property name
     */
    String getPropertyName();

    /**
     * Returns property value {@link T} of first argument
     *
     * @return property value {@link T}
     */
    T getFirst();

    /**
     * Returns property value {@link T} of last argument
     *
     * @return property value {@link T}
     */
    T getLast();
}
