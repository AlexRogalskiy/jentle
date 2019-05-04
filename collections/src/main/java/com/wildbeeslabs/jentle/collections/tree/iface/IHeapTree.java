package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.position.PositionalContainer;

/**
 * Custom heap {@link PositionalContainer} declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IHeapTree<T, R extends TreePosition<T, R>> extends IPositionalBinaryTree<T, R>, PositionalContainer<T> {

    R add(final T value);

    T remove();
}
