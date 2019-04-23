package com.wildbeeslabs.jentle.collections.iface.position;

/**
 * Custom mutable {@link IPositionalCollection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalContainer<T> extends IPositionalCollection<T> {

    void swap(final Position<T> positionFirst, final Position<T> positionLast);

    T replace(final Position<T> position, final T value);
}
