package com.wildbeeslabs.jentle.collections.list.iface;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.collection.PositionalContainer;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNode;

/**
 * Custom {@link IPositionalList} {@link PositionalContainer} interface declaration
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalListCollection<T, E extends ACPositionalListNode<T, E>> extends IPositionalList<T, E>, PositionalContainer<T> {

    T remove(final Position<T> position);

    Position<T> insertLast(final T value);

    Position<T> insertFirst(final T value);

    Position<T> insertAfter(final Position<T> position, final T value) throws InvalidPositionException;

    Position<T> insertBefore(final Position<T> position, final T value) throws InvalidPositionException;
}
