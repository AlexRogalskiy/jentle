package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.position.Position;

/**
 * Custom binary {@link IPositionalTree} declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalBinaryTree<T> extends IPositionalTree<T> {

    Position<T> getLeft(final Position<T> value);

    Position<T> getRight(final Position<T> value);

    Position<T> getSibling(final Position<T> value);
}
