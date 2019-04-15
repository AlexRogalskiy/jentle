package com.wildbeeslabs.jentle.collections.array.iface;

/**
 * Custom vector {@link IArray} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IVector<T> extends IArray<T> {

    T elemAtRank(int rank);

    T replaceAtRank(int rank, final T value);

    T removeAtRank(int rank);

    void insertAtRank(int rank, final T value);
}
