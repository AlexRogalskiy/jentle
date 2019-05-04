package com.wildbeeslabs.jentle.collections.iface.node;

/**
 * Custom key-value {@link Node} interface declaration
 *
 * @param <T>
 * @param <R>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface KeyValueNode<K, V> {

    /**
     * Returns key {@code K}
     *
     * @return key {@code K}
     */
    K getKey();

    /**
     * Returns value {@code V}
     *
     * @return value {@code V}
     */
    V getValue();
}
