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
package com.wildbeeslabs.jentle.collections.tree.iface.tree;

import com.wildbeeslabs.jentle.collections.iface.node.TreeNode;
import com.wildbeeslabs.jentle.collections.iface.visitor.Visitor;

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
public interface ITreeCollection<T, U extends TreeNode<T, U>> extends ITreeLike<T, U> {

    /**
     * Checks if current node has left child
     *
     * @param node - current node
     * @return true - if current node has left child node, false - otherwise
     */
    default <S extends U> boolean hasLeftChild(final S node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getLeft()));
    }

    /**
     * Checks if current node has right child
     *
     * @param node - current node
     * @return true - if current node has right child node, false - otherwise
     */
    default <S extends U> boolean hasRightChild(final S node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getRight()));
    }

    /**
     * Checks if current node is external node
     *
     * @param node - current node
     * @return true - if current node is external node, false - otherwise
     */
    default <S extends U> boolean isExternal(final S node) {
        Objects.requireNonNull(node);
        return (Objects.isNull(node.getLeft()) && Objects.isNull(node.getRight()));
    }

    /**
     * Checks if current node is internal node
     *
     * @param node - current node
     * @return true - if current node is internal node, false - otherwise
     */
    default <S extends U> boolean isInternal(final S node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getLeft()) || Objects.nonNull(node.getRight()));
    }

    /**
     * Returns left child node of the current node
     *
     * @param node - current node
     * @return left child node
     */
    default <S extends U> U getLeftChild(final S node) {
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
    default <S extends U> U getRightChild(final S node) {
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
    default <S extends U> int nodeSize(final S node) {
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
    default <S extends U> int height(final S node) {
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
    default <S extends U> int nodesOnLevel(final S node, int level) {
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
    default <S extends U> void inOrderIterator(final S node, final Visitor<T> visitor) {
        Objects.requireNonNull(node);
        this.inOrderIterator(node.getLeft(), visitor);
        visitor.visit(node.getData());
        this.inOrderIterator(node.getRight(), visitor);
    }

    /**
     * Traverses children of the current node in pre order sequence
     *
     * @param node    - current node
     * @param visitor - traversable service instance
     */
    default <S extends U> void preOrderIterator(final S node, final Visitor<T> visitor) {
        Objects.requireNonNull(node);
        visitor.visit(node.getData());
        this.preOrderIterator(node.getLeft(), visitor);
        this.preOrderIterator(node.getRight(), visitor);
    }

    /**
     * Traverses children of the current node in post order sequence
     *
     * @param node    - current node
     * @param visitor - traversable service instance
     */
    default <S extends U> void postOrderIterator(final S node, final Visitor<T> visitor) {
        Objects.requireNonNull(node);
        this.postOrderIterator(node.getLeft(), visitor);
        this.postOrderIterator(node.getRight(), visitor);
        visitor.visit(node.getData());
    }
}
