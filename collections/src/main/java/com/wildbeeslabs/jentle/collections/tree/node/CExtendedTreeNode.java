package com.wildbeeslabs.jentle.collections.tree.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * {@link ACTreeNodeExtended} implementation
 *
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CExtendedTreeNode<T> extends ACTreeNodeExtended<T, CExtendedTreeNode<T>> {

    public CExtendedTreeNode() {
        this(null);
    }

    public CExtendedTreeNode(final T data) {
        this(data, null, null);
    }

    public CExtendedTreeNode(final T data, final CExtendedTreeNode<T> left, final CExtendedTreeNode<T> right) {
        this(data, left, right, null);
    }

    public CExtendedTreeNode(final T data, final CExtendedTreeNode<T> left, final CExtendedTreeNode<T> right, final CExtendedTreeNode<T> parent) {
        super(data, left, right, parent);
    }
}
