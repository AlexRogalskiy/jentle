package com.wildbeeslabs.jentle.collections.tree.node;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CKeyValueNode<K, V> {

    private K key;
    private V value;
}
