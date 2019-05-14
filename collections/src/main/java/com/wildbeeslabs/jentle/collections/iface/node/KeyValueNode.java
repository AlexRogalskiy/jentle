package com.wildbeeslabs.jentle.collections.iface.node;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom key-value {@link Node} interface declaration
 *
 * @param <K> type of node key
 * @param <V> type of node value
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

    /**
     * A collector to create a {@link Map} from a {@link Stream} of {@link KeyValueNode}s.
     *
     * @return
     */
    public static <S, T> Collector<KeyValueNode<S, T>, ?, Map<S, T>> toMap() {
        return Collectors.toMap(KeyValueNode::getKey, KeyValueNode::getValue);
    }
}
