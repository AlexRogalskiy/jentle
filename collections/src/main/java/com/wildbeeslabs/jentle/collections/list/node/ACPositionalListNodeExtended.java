package com.wildbeeslabs.jentle.collections.list.node;

import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Custom {@link ACPositionalListNode} extended implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ACPositionalListNodeExtended<K, V, T extends CKeyValueNode<K, V>> extends ACPositionalListNode<T, ACPositionalListNodeExtended<K, V, T>> {

    public ACPositionalListNodeExtended() {
        this(null);
    }

    public ACPositionalListNodeExtended(final T data) {
        this(data, null, null);
    }

    public ACPositionalListNodeExtended(final T data, final ACPositionalListNodeExtended<K, V, T> next, final ACPositionalListNodeExtended<K, V, T> previous) {
        super(data, next, previous);
    }
}
