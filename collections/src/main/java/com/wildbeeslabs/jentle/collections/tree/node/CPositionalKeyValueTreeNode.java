package com.wildbeeslabs.jentle.collections.tree.node;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.node.PositionTreeNode;
import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
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
public class CPositionalKeyValueTreeNode<K, V, T extends CKeyValueNode<K, V>> extends ACTreeNodeExtended<T, CPositionalKeyValueTreeNode<K, V, T>> implements PositionTreeNode<T, CPositionalKeyValueTreeNode<K, V, T>> {

    public CPositionalKeyValueTreeNode() {
        this(null);
    }

    public CPositionalKeyValueTreeNode(final T data) {
        this(data, null, null);
    }

    public CPositionalKeyValueTreeNode(final T data, final CPositionalKeyValueTreeNode<K, V, T> left, final CPositionalKeyValueTreeNode<K, V, T> right) {
        this(data, left, right, null);
    }

    public CPositionalKeyValueTreeNode(final T data, final CPositionalKeyValueTreeNode<K, V, T> left, final CPositionalKeyValueTreeNode<K, V, T> right, final CPositionalKeyValueTreeNode<K, V, T> parent) {
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
    public T element() throws InvalidPositionException {
        return this.getData();
    }

    @Override
    public void setElement(final T value) {
        this.setData(value);
    }
}
