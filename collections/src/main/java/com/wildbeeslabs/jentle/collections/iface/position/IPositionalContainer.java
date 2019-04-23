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

    <S extends Position<T>> void swap(final S positionFirst, final S positionLast);

    <S extends Position<T>> T replace(final S position, final T value);
}
