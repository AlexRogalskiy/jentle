package com.wildbeeslabs.jentle.collections.tree.node;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Node Class for Binary Search Tree Implementation
 */
@NoArgsConstructor
@AllArgsConstructor
public class Node<T> {
    //Attributes
    private T data;
    private Node<T> left;
    private Node<T> right;
    private Node<T> parent;

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
     * Return left Node
     *
     * @return Node
     */
    public Node<T> getLeft() {
        return this.left;
    }

    /**
     * Return right Node
     *
     * @return Node
     */
    public Node<T> getRight() {
        return this.right;
    }

    /**
     * Return parent Node
     *
     * @return Node
     */
    public Node<T> getParent() {
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
    public void setLeft(final Node<T> value) {
        this.left = value;
    }

    /**
     * Set right node
     *
     * @param value
     */
    public void setRight(final Node<T> value) {
        this.right = value;
    }

    /**
     * Set parent node
     *
     * @param value
     */
    public void setParent(final Node<T> value) {
        this.parent = value;
    }

    public boolean equals(final Object obj) {
        boolean resp = false;
        if (obj instanceof Node) {
            if (!this.isNIL() && !((Node<T>) obj).isNIL()) {
                resp = this.data.equals(((Node<T>) obj).data);
            } else {
                resp = this.isNIL() && ((Node<T>) obj).isNIL();
            }

        }
        return resp;
    }
}
