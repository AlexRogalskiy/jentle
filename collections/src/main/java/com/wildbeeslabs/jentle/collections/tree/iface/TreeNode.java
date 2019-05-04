package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.collection.Collection;

/**
 * Custom tree {@link Node} interface declaration
 *
 * @param <T>
 * @param <R>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface TreeNode<T, R> extends Node<T> {

    R getLeft();

    void setLeft(final R position);

    R getRight();

    void setRight(final R position);

    R getParent();

    void setParent(final R position);
}
