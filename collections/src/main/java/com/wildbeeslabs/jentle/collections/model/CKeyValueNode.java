package com.wildbeeslabs.jentle.collections.model;

import com.wildbeeslabs.jentle.collections.iface.node.KeyValueNode;
import lombok.*;

import java.util.Optional;

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
     * @param <K>   type of node key
     * @param <V>   type of node value
     * @param key   - initial input node key {@code K}
     * @param value - initial input node value {@code V}
     * @return {@link CKeyValueNode}
     */
    public static <K, V> CKeyValueNode<K, V> of(final K key, final V value) {
        return new CKeyValueNode<>(key, value);
    }

    /**
     * Returns {@link CKeyValueNode} if both {@link Optional} instances have values or {@link Optional#empty()} if one or both
     * are missing.
     *
     * @param <K>   type of node key
     * @param <V>   type of node value
     * @param key   - initial input key {@link Optional}
     * @param value - initial input value {@link Optional}
     * @return {@link Optional} of {@link CKeyValueNode}
     */
    public static <K, V> Optional<CKeyValueNode<K, V>> with(final Optional<K> key, final Optional<V> value) {
        return key.flatMap(k -> value.map(v -> CKeyValueNode.of(k, v)));
    }
}
