package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.position.IPositionalContainer;
import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;

/**
 * Custom heap {@link IPositionalContainer} declaration
 *
 * @param <T>
 * @param <R>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IHeapTree<T, R extends TreePosition<T, R>> extends IPositionalBinaryTree<T, R>, IPositionalContainer<T> {

    <S extends R> S add(final T value);

    T remove();
}
