package com.wildbeeslabs.jentle.collections.model;

import com.wildbeeslabs.jentle.collections.iface.node.KeyValueNode;
import lombok.*;

/**
 * Default {@link KeyValueNode} implementation
 *
 * @param <K> type of node key
 * @param <V> type of node value
 */
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CKeyValueNode<K, V> implements KeyValueNode<K, V> {

    /**
     * Default node key
     */
    private K key;
    /**
     * Default node value
     */
    private V value;

    /**
     * Returns {@link CKeyValueNode} by input parameters
     *
     * @param <K>    type of node key
     * @param <V>    type of node value
     * @param first  - initial input node key {@code K}
     * @param second - initial input node value {@code V}
     * @return {@link CKeyValueNode}
     */
    public static <K, V> CKeyValueNode<K, V> of(final K first, final V second) {
        return new CKeyValueNode<>(first, second);
    }
}
