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

    @Override
    default T get(int index) throws IndexOutOfBoundsException {
        return this.elemAtRank(index);
    }
}
