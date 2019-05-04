package com.wildbeeslabs.jentle.collections.model;

import com.wildbeeslabs.jentle.collections.iface.node.KeyValueNode;
import lombok.*;

/**
 * Default {@link KeyValueNode} implementation
 *
 * @param <K>
 * @param <V>
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
}
