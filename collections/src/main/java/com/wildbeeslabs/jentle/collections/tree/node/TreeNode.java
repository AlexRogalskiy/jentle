package com.wildbeeslabs.jentle.collections.tree.node;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * TreeNode Class for Binary Search Tree Implementation
 */
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode<T> {
    //Attributes
    private T data;
    private TreeNode<T> left;
    private TreeNode<T> right;
    private TreeNode<T> parent;

    public TreeNode(final T data) {
        this(data, null, null, null);
    }

    /**
     * Return true if data is null or false if not
     *
     * @return boolean
     */
    public boolean isNIL() {
        return this.data == null;
    }

    /**
     * Return true if this node is a bst leaf or false if not
     *
     * @return boolean
     */
    public boolean isLeaf() {
        return (this.left.data == null && this.right.data == null);
    }

    /**
     * Return data
     *
     * @return data
     */
    public T getData() {
        return this.data;
    }

    /**
     * Return left TreeNode
     *
     * @return TreeNode
     */
    public TreeNode<T> getLeft() {
        return this.left;
    }

    /**
     * Return right TreeNode
     *
     * @return TreeNode
     */
    public TreeNode<T> getRight() {
        return this.right;
    }

    /**
     * Return parent TreeNode
     *
     * @return TreeNode
     */
    public TreeNode<T> getParent() {
        return this.parent;
    }

    /**
     * Set data value
     *
     * @param value
     */
    public void setData(final T value) {
        this.data = value;
    }

    /**
     * Set left node
     *
     * @param value
     */
    public void setLeft(final TreeNode<T> value) {
        this.left = value;
    }

    /**
     * Set right node
     *
     * @param value
     */
    public void setRight(final TreeNode<T> value) {
        this.right = value;
    }

    /**
     * Set parent node
     *
     * @param value
     */
    public void setParent(final TreeNode<T> value) {
        this.parent = value;
    }

    public boolean equals(final Object obj) {
        boolean resp = false;
        if (obj instanceof TreeNode) {
            if (!this.isNIL() && !((TreeNode<T>) obj).isNIL()) {
                resp = this.data.equals(((TreeNode<T>) obj).data);
            } else {
                resp = this.isNIL() && ((TreeNode<T>) obj).isNIL();
            }

        }
        return resp;
    }
}
