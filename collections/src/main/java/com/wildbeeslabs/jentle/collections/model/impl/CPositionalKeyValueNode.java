package com.wildbeeslabs.jentle.collections.model.impl;

import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.model.interfaces.Locator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class CPositionalKeyValueNode<K, V> extends CKeyValueNode<K, V> implements Locator<CPositionalKeyValueNode<K, V>> {

    private Position<CPositionalKeyValueNode<K, V>> position;

    public CPositionalKeyValueNode() {
        this(null, null);
    }

    public CPositionalKeyValueNode(final CPositionalKeyValueNode<K, V> data) {
        this(data.getKey(), data.getValue());
    }

    public CPositionalKeyValueNode(final K key, final V value) {
        this(key, value, null);
    }

    public CPositionalKeyValueNode(final K key, final V value, final Position<CPositionalKeyValueNode<K, V>> position) {
        super(key, value);
        this.position = position;
    }
}
