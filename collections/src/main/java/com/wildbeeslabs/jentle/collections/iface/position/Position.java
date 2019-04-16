package com.wildbeeslabs.jentle.collections.iface.position;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;

/**
 * Custom position interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface Position<T> {

    T element() throws InvalidPositionException;
}
