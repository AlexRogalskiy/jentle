package com.wildbeeslabs.jentle.collections.iface;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;

/**
 * Custom position interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPosition<T> {

    T element() throws InvalidPositionException;
}
