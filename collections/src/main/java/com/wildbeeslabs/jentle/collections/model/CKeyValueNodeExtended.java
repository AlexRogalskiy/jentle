package com.wildbeeslabs.jentle.collections.model;

import lombok.*;

/**
 * Default {@link CKeyValueNode} extended implementation
 *
 * @param <K>
 * @param <V>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CKeyValueNodeExtended<K, V> extends CKeyValueNode<K, V> {

    /**
     * Default node height
     */
    private int height;

    public CKeyValueNodeExtended(final K key, final V value) {
        this(key, value, 0);
    }

    public CKeyValueNodeExtended(final K key, final V value, final int height) {
        super(key, value);
        this.height = height;
    }
}
