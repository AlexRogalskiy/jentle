package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.PositionalCollection;
import lombok.NonNull;

/**
 * Custom tree {@link PositionalCollection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalTree<T, R extends TreePosition<T, R>> extends PositionalCollection<T> {

    <S extends R> S root();

    <S extends R> S getParent(final S position);

    <S extends R> @NonNull PositionIterator<R> children(final S position);

    <S extends R> boolean isInternal(final S position);

    <S extends R> boolean isExternal(final S position);

    <S extends R> boolean isRoot(final S position);
}
