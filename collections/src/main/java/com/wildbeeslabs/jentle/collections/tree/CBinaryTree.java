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
package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.interfaces.ITreeExtended;
import com.wildbeeslabs.jentle.collections.tree.node.ACExtendedTreeNode;

import java.util.Comparator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom binary tree implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CBinaryTree<T> implements ITreeExtended<T, CBinaryTree.CBinaryTreeNode<T>> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class CBinaryTreeNode<T> extends ACExtendedTreeNode<T, CBinaryTreeNode<T>> {

        public CBinaryTreeNode() {
            this(null);
        }

        public CBinaryTreeNode(final T data) {
            this(data, null, null);
        }

        public CBinaryTreeNode(final T data, final CBinaryTreeNode<T> left, final CBinaryTreeNode<T> right) {
            this(data, left, right, null);
        }

        public CBinaryTreeNode(final T data, final CBinaryTreeNode<T> left, final CBinaryTreeNode<T> right, final CBinaryTreeNode<T> parent) {
            super(data, left, right, parent);
        }
    }

    protected CBinaryTreeNode<T> root;
    protected int size;
    protected final Comparator<? super T> cmp;

    public CBinaryTree() {
        this(CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CBinaryTree(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CBinaryTree(final CBinaryTreeNode<T> root) {
        this(root, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CBinaryTree(final CBinaryTreeNode<T> root, final Comparator<? super T> cmp) {
        this.root = root;
        this.cmp = cmp;
        this.size = 0;
    }

    @Override
    public void setRoot(final T value) {
        if (this.isEmpty()) {
            this.root = new CBinaryTreeNode<>(value);
            this.size++;
        }
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public CBinaryTreeNode<T> getRoot() {
//        if (this.isEmpty()) {
//            return null;
//        }
        return this.root;
    }

    @Override
    public boolean isRoot(final CBinaryTreeNode<T> node) {
        Objects.requireNonNull(node);
        return (this.root == node);
    }

    @Override
    public boolean hasParent(final CBinaryTreeNode<T> node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getParent()));
    }

    @Override
    public boolean isExternal(final CBinaryTreeNode<T> node) {
        Objects.requireNonNull(node);
        return (Objects.isNull(node.getLeft()) && Objects.isNull(node.getRight()));
    }

    @Override
    public boolean isInternal(final CBinaryTreeNode<T> node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getLeft()) || Objects.nonNull(node.getRight()));
    }

    @Override
    public CBinaryTreeNode<T> getParent(final CBinaryTreeNode<T> node) {
        if (this.hasParent(node)) {
            return node.getParent();
        }
        return null;
    }

    @Override
    public boolean hasLeftChild(final CBinaryTreeNode<T> node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getLeft()));
    }

    @Override
    public boolean hasRightChild(final CBinaryTreeNode<T> node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getRight()));
    }

    @Override
    public boolean isLeftChild(final CBinaryTreeNode<T> node) {
        Objects.requireNonNull(node);
        if (Objects.isNull(node.getParent())) {
            return false;
        }
        return (node.getParent().getLeft() == node);
    }

    @Override
    public boolean isRightChild(final CBinaryTreeNode<T> node) {
        Objects.requireNonNull(node);
        if (Objects.isNull(node.getParent())) {
            return false;
        }
        return (node.getParent().getRight() == node);
    }

    @Override
    public void insertLeft(final CBinaryTreeNode<T> node, final T value) {
        Objects.requireNonNull(node);
        if (Objects.nonNull(node.getLeft())) {
            LOGGER.debug("Node has already left child");
            return;
        }
        CBinaryTreeNode<T> newNode = new CBinaryTreeNode<>(value);
        node.setLeft(newNode);
        this.size++;
    }

    @Override
    public void insertRight(final CBinaryTreeNode<T> node, final T value) {
        Objects.requireNonNull(node);
        if (Objects.nonNull(node.getRight())) {
            LOGGER.debug("Node has already right child");
            return;
        }
        CBinaryTreeNode<T> newNode = new CBinaryTreeNode<>(value);
        node.setRight(newNode);
        this.size++;
    }

    @Override
    public CBinaryTreeNode<T> getLeftChild(final CBinaryTreeNode<T> node) {
        if (this.hasLeftChild(node)) {
            return node.getLeft();
        }
        return null;
    }

    @Override
    public CBinaryTreeNode<T> getRightChild(final CBinaryTreeNode<T> node) {
        if (this.hasRightChild(node)) {
            return node.getRight();
        }
        return null;
    }

    @Override
    public T replaceElement(final CBinaryTreeNode<T> node, final T newValue) {
        Objects.requireNonNull(node);
        final T value = node.getData();
        node.setData(newValue);
        return value;
    }

    @Override
    public void swapElements(final CBinaryTreeNode<T> first, final CBinaryTreeNode<T> last) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(last);
        final T value = this.replaceElement(first, last.getData());
        last.setData(value);
    }

    @Override
    public int depth(final CBinaryTreeNode<T> node) {
        if (this.isRoot(node)) {
            return 0;
        } else {
            return 1 + this.depth(node.getParent());
        }
    }

    @Override
    public int nodeSize(final CBinaryTreeNode<T> node) {
        if (this.hasLeftChild(node) && this.hasRightChild(node)) {
            return 1 + this.nodeSize(node.getLeft()) + this.nodeSize(node.getRight());
        } else if (this.hasLeftChild(node) && !this.hasRightChild(node)) {
            return 1 + this.nodeSize(node.getLeft());
        } else if (!this.hasLeftChild(node) && this.hasRightChild(node)) {
            return 1 + this.nodeSize(node.getRight());
        } else {
            return 1;
        }
    }

    @Override
    public int size() {
        return this.size;
    }
}
