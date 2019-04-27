package com.wildbeeslabs.jentle.collections.iface.position;

/**
 * Custom tree {@link Position} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface TreePosition<T, R> extends Position<T> {

    R getLeft();

    void setLeft(final R position);

    R getRight();

    void setRight(final R position);

    R getParent();

    void setParent(final R position);
}