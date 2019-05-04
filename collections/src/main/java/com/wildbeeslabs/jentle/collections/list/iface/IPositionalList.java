package com.wildbeeslabs.jentle.collections.list.iface;

import com.wildbeeslabs.jentle.collections.exception.BoundaryViolationException;
import com.wildbeeslabs.jentle.collections.exception.EmptyContainerException;
import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.position.PositionalCollection;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNode;

/**
 * Custom {@link IListCollection} {@link PositionalCollection} interface declaration
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalList<T, E extends ACPositionalListNode<T, E>> extends IListCollection<T, E>, PositionalCollection<T> {

    Position<T> after(final Position<T> position) throws InvalidPositionException, BoundaryViolationException;

    Position<T> before(final Position<T> position) throws InvalidPositionException, BoundaryViolationException;

    Position<T> last() throws EmptyContainerException;

    Position<T> first() throws EmptyContainerException;

    boolean isLast(final Position<T> position) throws InvalidPositionException;

    boolean isFirst(final Position<T> position) throws InvalidPositionException;
}
