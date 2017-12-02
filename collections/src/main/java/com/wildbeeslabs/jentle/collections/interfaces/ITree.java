/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.collections.interfaces;

import com.wildbeeslabs.jentle.collections.tree.node.ANode;

/**
 *
 * Custom tree interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface ITree<T> {

    /**
     * Get the size of the tree.
     *
     * @return size of the tree.
     */
    int size();

    /**
     * Check if the list contains values.
     *
     * @return boolean (true - if the list is empty, false - otherwise)
     */
    boolean isEmpty();

    /**
     * Add new root node
     *
     * @param value - new root node
     */
    void setRoot(final T value);

    /**
     * Returns current root node
     *
     * @return - current root node
     */
    ANode<T> getRoot();

    /**
     * Checks if current node is root
     *
     * @param node - current node
     * @return true - if current node is root, false - otherwise
     */
    boolean isRoot(final ANode<T> node);

    /**
     * Checks if current node has left child
     *
     * @param node - current node
     * @return true - if current node has left child node, false - otherwise
     */
    boolean hasLeftChild(final ANode<T> node);

    /**
     * Checks if current node has right child
     *
     * @param node - current node
     * @return true - if current node has right child node, false - otherwise
     */
    boolean hasRightChild(final ANode<T> node);

    /**
     * Checks if current node is left child
     *
     * @param node - current node
     * @return true - if current node is left child node, false - otherwise
     */
    boolean isLeftChild(final ANode<T> node);

    /**
     * Checks if current node is right child
     *
     * @param node - current node
     * @return true - if current node is right child node, false - otherwise
     */
    boolean isRightChild(final ANode<T> node);

    /**
     * Creates new node as a left child of the current node
     *
     * @param node - current node
     * @param value - node value
     */
    void insertLeft(final ANode<T> node, final T value);

    /**
     * Creates new node as a right child of the current node
     *
     * @param node - current node
     * @param value - node value
     */
    void insertRight(final ANode<T> node, final T value);

    /**
     * Returns left child node of the current node
     *
     * @param node - current node
     * @return left child node
     */
    ANode<T> getLeftChild(final ANode<T> node);

    /**
     * Returns right child node of the current node
     *
     * @param node - current node
     * @return right child node
     */
    ANode<T> getRightChild(final ANode<T> node);

    /**
     * Replaces current node with a new value
     *
     * @param node - current node
     * @param newValue - new node value
     * @return previous value of current node
     */
    T replaceElement(final ANode<T> node, final T newValue);

    /**
     * Swaps elements of first and last nodes
     *
     * @param first - first node to replace
     * @param last - last node to be replaced with
     */
    void swapElements(final ANode<T> first, final ANode<T> last);

    /**
     * Returns the depth of the current node
     *
     * @param node - current node
     * @return number of nodes in parent hierarchy
     */
    int depth(final ANode<T> node);

    /**
     * Returns the size of the current node
     *
     * @param node - current node
     * @return number of nodes in the child hierarchy
     */
    int nodeSize(final ANode<T> node);
}
