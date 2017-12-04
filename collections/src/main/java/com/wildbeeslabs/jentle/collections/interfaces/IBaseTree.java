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

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;
import java.util.Optional;

/**
 *
 * Custom base tree interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
public interface IBaseTree<T, U extends ACTreeNode<T, U>> extends IBase<T, U> {

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
    void setRoot(final Optional<? extends T> value);

    /**
     * Returns current root node
     *
     * @return - current root node
     */
    U getRoot();

    /**
     * Checks if current node is root
     *
     * @param node - current node
     * @return true - if current node is root, false - otherwise
     */
    boolean isRoot(final U node);

    /**
     * Checks if current node has left child
     *
     * @param node - current node
     * @return true - if current node has left child node, false - otherwise
     */
    boolean hasLeftChild(final U node);

    /**
     * Checks if current node has right child
     *
     * @param node - current node
     * @return true - if current node has right child node, false - otherwise
     */
    boolean hasRightChild(final U node);

    /**
     * Checks if current node is external node
     *
     * @param node - current node
     * @return true - if current node is external node, false - otherwise
     */
    boolean isExternal(final U node);

    /**
     * Checks if current node is internal node
     *
     * @param node - current node
     * @return true - if current node is internal node, false - otherwise
     */
    boolean isInternal(final U node);

    /**
     * Returns left child node of the current node
     *
     * @param node - current node
     * @return left child node
     */
    U getLeftChild(final U node);

    /**
     * Returns right child node of the current node
     *
     * @param node - current node
     * @return right child node
     */
    U getRightChild(final U node);

    /**
     * Returns the size of the current node
     *
     * @param node - current node
     * @return total number of nodes in the child hierarchy
     */
    int nodeSize(final U node);

    /**
     * Returns max number of left/right child nodes of the current node
     *
     * @param node - current node
     * @return max number of nodes in the left / right child hierarchy
     */
    int height(final U node);

    /**
     * Returns number of child nodes of the current node on a particular level
     * hierarchy
     *
     * @param node - current node
     * @param level - level of hierarchy
     * @return number of nodes on level hierarchy
     */
    int nodesOnLevel(final U node, int level);

    /**
     * Traverses children of the current node in order sequence
     *
     * @param node - current node
     * @param visitor - traversable visitor instance
     */
    void inOrderTraversal(final U node, final IVisitor<T> visitor);

    /**
     * Traverses children of the current node in pre order sequence
     *
     * @param node - current node
     * @param visitor - traversable visitor instance
     */
    void preOrderTraversal(final U node, final IVisitor<T> visitor);

    /**
     * Traverses children of the current node in post order sequence
     *
     * @param node - current node
     * @param visitor - traversable visitor instance
     */
    void postOrderTraversal(final U node, final IVisitor<T> visitor);
}
