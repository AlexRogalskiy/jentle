package com.wildbeeslabs.jentle.collections.list.node;

import com.wildbeeslabs.jentle.collections.model.CPositionalKeyValueNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Custom {@link ACPositionalListNodeExtended} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CLocatorListNodeExtended<K, V> extends ACPositionalListNodeExtended<K, V, CPositionalKeyValueNode<K, V>> {

    public CLocatorListNodeExtended() {
        this(null);
    }

    public CLocatorListNodeExtended(final CPositionalKeyValueNode<K, V> data) {
        this(data, null, null);
    }

    public CLocatorListNodeExtended(final CPositionalKeyValueNode<K, V> data, final CLocatorListNodeExtended<K, V> next, final CLocatorListNodeExtended<K, V> previous) {
        super(data, next, previous);
    }
}
