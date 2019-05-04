package com.wildbeeslabs.jentle.collections.iface.node;

import java.io.Serializable;

/**
 * Custom {@link Node} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface Node<T> extends Serializable, Cloneable {

    T getData();

    void setData(final T data);
}
