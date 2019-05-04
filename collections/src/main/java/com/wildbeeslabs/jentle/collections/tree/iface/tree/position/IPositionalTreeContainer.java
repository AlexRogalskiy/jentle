package com.wildbeeslabs.jentle.collections.tree.iface.tree.position;

import com.wildbeeslabs.jentle.collections.iface.node.PositionTreeNode;
import com.wildbeeslabs.jentle.collections.iface.collection.PositionalContainer;

/**
 * Custom {@link IPositionalTree} container declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalTreeContainer<T, R extends PositionTreeNode<T, R>> extends IPositionalTree<T, R>, PositionalContainer<T> {
}
