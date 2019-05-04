package com.wildbeeslabs.jentle.collections.model;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CKeyValueNode<K, V> {

    /**
     * Default key
     */
    private K key;
    /**
     * Default value
     */
    private V value;
}
