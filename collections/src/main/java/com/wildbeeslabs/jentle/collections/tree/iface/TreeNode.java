package com.wildbeeslabs.jentle.collections.tree.iface;

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

    default R getParent() {
        return null;
    }

    default void setParent(final R position) {
    }
}
