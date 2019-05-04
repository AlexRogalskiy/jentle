package com.wildbeeslabs.jentle.collections.model;

import com.wildbeeslabs.jentle.collections.iface.node.KeyValueNode;
import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CKeyValueNode<K, V> implements KeyValueNode<K, V> {

    /**
     * Default key
     */
    private K key;
    /**
     * Default value
     */
    private V value;
}
