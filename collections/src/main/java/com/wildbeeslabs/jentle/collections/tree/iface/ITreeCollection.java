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
package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.visitor.IVisitor;
import com.wildbeeslabs.jentle.collections.tree.node.ACBaseTreeNode;

import java.util.Objects;

/**
 * Custom {@link ITreeLike} interface declaration
 *
 * @param <T>
 * @param <U>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface ITreeCollection<T, U extends ACBaseTreeNode<T, U>> extends ITreeLike<T, U> {

    /**
     * Checks if current node has left child
     *
     * @param node - current node
     * @return true - if current node has left child node, false - otherwise
     */
    default boolean hasLeftChild(final U node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getLeft()));
    }

    /**
     * Checks if current node has right child
     *
     * @param node - current node
     * @return true - if current node has right child node, false - otherwise
     */
    default boolean hasRightChild(final U node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getRight()));
    }

    /**
     * Checks if current node is external node
     *
     * @param node - current node
     * @return true - if current node is external node, false - otherwise
     */
    default boolean isExternal(final U node) {
        Objects.requireNonNull(node);
        return (Objects.isNull(node.getLeft()) && Objects.isNull(node.getRight()));
    }

    /**
     * Checks if current node is internal node
     *
     * @param node - current node
     * @return true - if current node is internal node, false - otherwise
     */
    default boolean isInternal(final U node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getLeft()) || Objects.nonNull(node.getRight()));
    }

    /**
     * Returns left child node of the current node
     *
     * @param node - current node
     * @return left child node
     */
    default U getLeftChild(final U node) {
        if (this.hasLeftChild(node)) {
            return node.getLeft();
        }
        return null;
    }

    /**
     * Returns right child node of the current node
     *
     * @param node - current node
     * @return right child node
     */
    default U getRightChild(final U node) {
        if (this.hasRightChild(node)) {
            return node.getRight();
        }
        return null;
    }

    /**
     * Returns the size of the current node
     *
     * @param node - current node
     * @return total number of nodes in the child hierarchy
     */
    default int nodeSize(final U node) {
        if (Objects.isNull(node)) {
            return 0;
        }
        if (this.hasLeftChild(node) && this.hasRightChild(node)) {
            return 1 + this.nodeSize(node.getLeft()) + this.nodeSize(node.getRight());
        } else if (this.hasLeftChild(node) && !this.hasRightChild(node)) {
            return 1 + this.nodeSize(node.getLeft());
        } else if (!this.hasLeftChild(node) && this.hasRightChild(node)) {
            return 1 + this.nodeSize(node.getRight());
        }
        return 1;
    }

    /**
     * Returns max number of left/right child nodes of the current node
     *
     * @param node - current node
     * @return max number of nodes in the left / right child hierarchy
     */
    default int height(final U node) {
        if (Objects.isNull(node)) {
            return -1;
        }
        return Math.max(this.height(node.getLeft()), this.height(node.getRight())) + 1;
    }

    /**
     * Returns number of child nodes of the current node on a particular level
     * hierarchy
     *
     * @param node  - current node
     * @param level - level of hierarchy
     * @return number of nodes on level hierarchy
     */
    default int nodesOnLevel(final U node, int level) {
        if (Objects.isNull(node)) {
            return 0;
        }
        if (1 == level) {
            return 1;
        }
        return this.nodesOnLevel(node.getLeft(), level - 1) + this.nodesOnLevel(node.getRight(), level - 1);
    }

    /**
     * Traverses children of the current node in order sequence
     *
     * @param node    - current node
     * @param visitor - traversable service instance
     */
    default void inOrderIterator(final U node, final IVisitor<T> visitor) {
        Objects.requireNonNull(node);
        inOrderIterator(node.getLeft(), visitor);
        visitor.visit(node.getData());
        inOrderIterator(node.getRight(), visitor);
    }

    /**
     * Traverses children of the current node in pre order sequence
     *
     * @param node    - current node
     * @param visitor - traversable service instance
     */
    default void preOrderIterator(final U node, final IVisitor<T> visitor) {
        Objects.requireNonNull(node);
        visitor.visit(node.getData());
        preOrderIterator(node.getLeft(), visitor);
        preOrderIterator(node.getRight(), visitor);
    }

    /**
     * Traverses children of the current node in post order sequence
     *
     * @param node    - current node
     * @param visitor - traversable service instance
     */
    default void postOrderIterator(final U node, final IVisitor<T> visitor) {
        Objects.requireNonNull(node);
        postOrderIterator(node.getLeft(), visitor);
        postOrderIterator(node.getRight(), visitor);
        visitor.visit(node.getData());
    }
}
