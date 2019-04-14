package com.wildbeeslabs.jentle.collections.interfaces.list;

import com.wildbeeslabs.jentle.collections.exception.BoundaryViolationException;
import com.wildbeeslabs.jentle.collections.exception.EmptyContainerException;
import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.interfaces.service.Position;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNode;

/**
 * Custom list extended interface declaration {@link IBaseList}
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalList<T, E extends ACPositionalListNode<T, E>> extends IBaseList<T, E> {

    T replace(final Position<T> position, final T value);

    void swap(final Position<T> positionFirst, final Position<T> positionLast);

    T remove(final Position<T> position);

    Position<T> insertLast(final T value);

    Position<T> insertFirst(final T value);

    Position<T> insertAfter(final Position<T> position, final T value) throws InvalidPositionException;

    Position<T> insertBefore(final Position<T> position, final T value) throws InvalidPositionException;

    Position<T> after(final Position<T> position) throws InvalidPositionException, BoundaryViolationException;

    Position<T> before(final Position<T> position) throws InvalidPositionException, BoundaryViolationException;

    Position<T> last() throws EmptyContainerException;

    Position<T> first() throws EmptyContainerException;

    boolean isLast(final Position<T> position) throws InvalidPositionException;

    boolean isFirst(final Position<T> position) throws InvalidPositionException;
}
