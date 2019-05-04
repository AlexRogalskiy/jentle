package com.wildbeeslabs.jentle.collections.tree.node;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.node.PositionTreeNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Positional {@link ACTreeNodeExtended} implementation
 *
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CPositionalTreeNode<T> extends ACTreeNodeExtended<T, CPositionalTreeNode<T>> implements PositionTreeNode<T, CPositionalTreeNode<T>> {

    public CPositionalTreeNode() {
        this(null);
    }

    public CPositionalTreeNode(final T data) {
        this(data, null, null);
    }

    public CPositionalTreeNode(final T data, final CPositionalTreeNode<T> left, final CPositionalTreeNode<T> right) {
        this(data, left, right, null);
    }

    public CPositionalTreeNode(final T data, final CPositionalTreeNode<T> left, final CPositionalTreeNode<T> right, final CPositionalTreeNode<T> parent) {
        super(data, left, right, parent);
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
