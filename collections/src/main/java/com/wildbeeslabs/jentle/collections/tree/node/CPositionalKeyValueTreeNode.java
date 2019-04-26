package com.wildbeeslabs.jentle.collections.tree.node;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.position.TreePosition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

/**
 * Positional {@link CKeyValueNode} {@link ACTreeNodeExtended} implementation
 *
 * @param <K>
 * @param <V>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CPositionalKeyValueTreeNode<K, V> extends ACTreeNodeExtended<CKeyValueNode<K, V>, CPositionalKeyValueTreeNode<K, V>> implements TreePosition<CKeyValueNode<K, V>, CPositionalKeyValueTreeNode<K, V>> {

    public CPositionalKeyValueTreeNode() {
        this(null);
    }

    public CPositionalKeyValueTreeNode(final CKeyValueNode<K, V> data) {
        this(data, null, null);
    }

    public CPositionalKeyValueTreeNode(final CKeyValueNode<K, V> data, final CPositionalKeyValueTreeNode<K, V> left, final CPositionalKeyValueTreeNode<K, V> right) {
        this(data, left, right, null);
    }

    public CPositionalKeyValueTreeNode(final CKeyValueNode<K, V> data, final CPositionalKeyValueTreeNode<K, V> left, final CPositionalKeyValueTreeNode<K, V> right, final CPositionalKeyValueTreeNode<K, V> parent) {
        super(data, left, right, parent);
    }

    public K key() {
        if (Objects.isNull(this.getData())) {
            return this.getData().getKey();
        }
        return null;
    }

    public V value() {
        if (Objects.isNull(this.getData())) {
            return this.getData().getValue();
        }
        return null;
    }

    @Override
    public CKeyValueNode<K, V> element() throws InvalidPositionException {
        return this.getData();
    }

    @Override
    public void setElement(final CKeyValueNode<K, V> value) {
        this.setData(value);
    }
}
