package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;

/**
 * Custom binary {@link IPositionalTree} declaration
 *
 * @param <T>
 * @param <R>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalBinaryTree<T, R extends TreePosition<T, R>> extends IPositionalTree<T, R> {

    <S extends R> S getLeft(final S value);

    <S extends R> S getRight(final S value);

    <S extends R> S getSibling(final S value);
}
