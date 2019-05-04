package com.wildbeeslabs.jentle.collections.iface.collection;

import com.wildbeeslabs.jentle.collections.iface.position.Position;

/**
 * Custom mutable {@link PositionalCollection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface PositionalContainer<T> extends PositionalCollection<T> {

    /**
     * Swaps positions {@code S} by input values
     *
     * @param <S>
     * @param positionFirst - initial input first position {@code S} to swap by
     * @param positionLast  - initial input last position {@code S} to be swapped
     */
    <S extends Position<T>> void swap(final S positionFirst, final S positionLast);

    /**
     * Replaces value {@code T} by input position {@code S} and value {@code T}
     *
     * @param <S>
     * @param position - initial input position {@code S} to replace value
     * @param value    - initial input new value {@code T}
     * @return replaced value {@code T} of input position {@code S}
     */
    <S extends Position<T>> T replace(final S position, final T value);
}
