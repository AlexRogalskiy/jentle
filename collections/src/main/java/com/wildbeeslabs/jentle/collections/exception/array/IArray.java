package com.wildbeeslabs.jentle.collections.exception.array;

import java.util.RandomAccess;

/**
 *
 * Custom dynamic array interface declaration
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface IArray<T> extends Iterable<T>, RandomAccess {
    
}
