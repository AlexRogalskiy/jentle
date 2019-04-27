package com.wildbeeslabs.jentle.collections.model.impl;

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
