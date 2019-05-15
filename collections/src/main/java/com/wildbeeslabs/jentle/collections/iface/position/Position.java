package com.wildbeeslabs.jentle.collections.iface.position;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;

/**
 * Position interface declaration
 *
 * @param <T> type of element
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface Position<T> {

    /**
     * Returns {@link Position} element {@code T}
     *
     * @return {@link Position} element {@code T}
     * @throws InvalidPositionException
     */
    T element() throws InvalidPositionException;

    /**
     * Updates {@link Position} element {@code T}
     *
     * @param value - initial input element {@code T} to update
     */
    void setElement(final T value);
}
