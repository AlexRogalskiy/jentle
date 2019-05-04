package com.wildbeeslabs.jentle.collections.tree.iface.heap;

import com.wildbeeslabs.jentle.collections.iface.collection.PositionalContainer;
import com.wildbeeslabs.jentle.collections.iface.node.PositionTreeNode;
import com.wildbeeslabs.jentle.collections.tree.iface.tree.binary.position.IPositionalBinaryTree;

/**
 * Custom heap {@link PositionalContainer} declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IHeapTree<T, R extends PositionTreeNode<T, R>> extends IPositionalBinaryTree<T, R>, PositionalContainer<T> {

    R add(final T value);

    T remove();
}
