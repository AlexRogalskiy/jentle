package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.IPositionalContainer;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import lombok.NonNull;

/**
 * Custom tree {@link IPositionalContainer} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalTree<T> extends IPositionalContainer<T> {

    Position<T> root();

    Position<T> parent(final Position<T> position);

    @NonNull <S extends Position<T>> PositionIterator<S> children(final Position<T> position);

    boolean isInternal(final Position<T> position);

    boolean isExternal(final Position<T> position);

    boolean isRoot(final Position<T> position);
}
