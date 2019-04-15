package com.wildbeeslabs.jentle.collections.list.iface;

import com.wildbeeslabs.jentle.collections.exception.BoundaryViolationException;
import com.wildbeeslabs.jentle.collections.exception.EmptyContainerException;
import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.IPosition;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNode;

/**
 * Custom {@link IListCollection} interface declaration
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionList<T, E extends ACPositionalListNode<T, E>> extends IListCollection<T, E> {

    T replace(final IPosition<T> position, final T value);

    void swap(final IPosition<T> positionFirst, final IPosition<T> positionLast);

    T remove(final IPosition<T> position);

    IPosition<T> insertLast(final T value);

    IPosition<T> insertFirst(final T value);

    IPosition<T> insertAfter(final IPosition<T> position, final T value) throws InvalidPositionException;

    IPosition<T> insertBefore(final IPosition<T> position, final T value) throws InvalidPositionException;

    IPosition<T> after(final IPosition<T> position) throws InvalidPositionException, BoundaryViolationException;

    IPosition<T> before(final IPosition<T> position) throws InvalidPositionException, BoundaryViolationException;

    IPosition<T> last() throws EmptyContainerException;

    IPosition<T> first() throws EmptyContainerException;

    boolean isLast(final IPosition<T> position) throws InvalidPositionException;

    boolean isFirst(final IPosition<T> position) throws InvalidPositionException;
}
