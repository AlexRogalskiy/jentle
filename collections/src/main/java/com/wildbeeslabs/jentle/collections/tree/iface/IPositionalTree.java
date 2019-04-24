package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.IPositionalCollection;
import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;
import lombok.NonNull;

/**
 * Custom tree {@link IPositionalCollection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalTree<T, R extends TreePosition<T, R>> extends IPositionalCollection<T> {

    <S extends TreePosition<T, R>> S root();

    <S extends TreePosition<T, R>> S getParent(final S position);

    @NonNull PositionIterator<R> children(final R position);

    <S extends TreePosition<T, R>> boolean isInternal(final S position);

    <S extends TreePosition<T, R>> boolean isExternal(final S position);

    <S extends TreePosition<T, R>> boolean isRoot(final S position);
}
