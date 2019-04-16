package com.wildbeeslabs.jentle.collections.array.iface;

/**
 * Custom mutable {@link IVector} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IMutableVector<T> extends IVector<T> {

    T replaceAtRank(int rank, final T value);

    T removeAtRank(int rank);

    void insertAtRank(int rank, final T value);

    @Override
    default T set(final T item, int index) throws IndexOutOfBoundsException {
        return this.replaceAtRank(index, item);
    }
}
