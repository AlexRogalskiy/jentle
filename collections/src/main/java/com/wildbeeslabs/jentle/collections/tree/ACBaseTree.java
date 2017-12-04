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

import com.wildbeeslabs.jentle.collections.interfaces.IBaseTree;
import com.wildbeeslabs.jentle.collections.interfaces.IVisitor;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom abstract base tree implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class ACBaseTree<T, U extends ACTreeNode<T, U>> implements IBaseTree<T, U> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    protected U root;
    protected int size;
    protected final Comparator<? super T> cmp;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ACBaseTree(final U root, final Comparator<? super T> cmp) {
        this.root = root;
        this.cmp = cmp;
        this.size = this.nodeSize(this.root);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public void setRoot(final Optional<? extends T> value) {
        this.root = this.createTreeNode(value);
        this.size = 1;
    }

    @Override
    public U getRoot() {
        if (this.isEmpty()) {
            return null;
        }
        return this.root;
    }

    @Override
    public boolean isRoot(final U node) {
        Objects.requireNonNull(node);
        return (this.root == node);
    }

    @Override
    public boolean hasLeftChild(final U node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getLeft()));
    }

    @Override
    public boolean hasRightChild(final U node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getRight()));
    }

    @Override
    public boolean isExternal(final U node) {
        Objects.requireNonNull(node);
        return (Objects.isNull(node.getLeft()) && Objects.isNull(node.getRight()));
    }

    @Override
    public boolean isInternal(final U node) {
        Objects.requireNonNull(node);
        return (Objects.nonNull(node.getLeft()) || Objects.nonNull(node.getRight()));
    }

    @Override
    public U getLeftChild(final U node) {
        if (this.hasLeftChild(node)) {
            return node.getLeft();
        }
        return null;
    }

    @Override
    public U getRightChild(final U node) {
        if (this.hasRightChild(node)) {
            return node.getRight();
        }
        return null;
    }

    @Override
    public int nodeSize(final U node) {
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

    @Override
    public int height(final U node) {
        if (Objects.isNull(node)) {
            return 0;
        }
        return Math.max(this.height(node.getLeft()), this.height(node.getRight())) + 1;
    }

    @Override
    public int nodesOnLevel(final U node, int level) {
        if (Objects.isNull(node)) {
            return 0;
        }
        if (1 == level) {
            return 1;
        }
        return this.nodesOnLevel(node.getLeft(), level - 1) + this.nodesOnLevel(node.getRight(), level - 1);
    }

    @Override
    public void inOrderTraversal(final U node, final IVisitor<T> visitor) {
        Objects.requireNonNull(node);
        inOrderTraversal(node.getLeft(), visitor);
        visitor.visit(node.getData());
        inOrderTraversal(node.getRight(), visitor);
    }

    @Override
    public void preOrderTraversal(final U node, final IVisitor<T> visitor) {
        Objects.requireNonNull(node);
        visitor.visit(node.getData());
        preOrderTraversal(node.getLeft(), visitor);
        preOrderTraversal(node.getRight(), visitor);
    }

    @Override
    public void postOrderTraversal(final U node, final IVisitor<T> visitor) {
        Objects.requireNonNull(node);
        postOrderTraversal(node.getLeft(), visitor);
        postOrderTraversal(node.getRight(), visitor);
        visitor.visit(node.getData());
    }

    protected abstract U createTreeNode(final Optional<? extends T> value);
}
