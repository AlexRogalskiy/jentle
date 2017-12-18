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

import com.wildbeeslabs.jentle.collections.interfaces.IBinaryTree;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract binary tree implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <U>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class ACBinaryTree<T, U extends ACTreeNode<T, U>> extends ACBaseTree<T, U> implements IBinaryTree<T, U> {

    public ACBinaryTree(final U root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    protected U insertTo(final T value) {
        return this.insertTo(value, Boolean.FALSE);
    }

    protected U insertTo(final T value, boolean isExact) {
        final U node = this.createTreeNode(Optional.ofNullable(value));
        if (this.isEmpty()) {
            this.root = node;
            this.size = 1;
        } else {
            if (!this.insert(this.getRoot(), node, isExact)) {
                return null;
            }
        }
        return node;
    }

    protected boolean insert(final U node, final U newNode, boolean isExact) {
        Objects.requireNonNull(node);
        if (isExact && Objects.compare(newNode.getData(), node.getData(), this.cmp) == 0) {
            return false;
        }
        if (Objects.compare(newNode.getData(), node.getData(), this.cmp) <= 0) {
            if (Objects.isNull(node.getLeft())) {
                node.setLeft(newNode);
            } else {
                return this.insert(node.getLeft(), newNode, isExact);
            }
        } else {
            if (Objects.isNull(node.getRight())) {
                node.setRight(newNode);
            } else {
                return this.insert(node.getRight(), newNode, isExact);
            }
        }
        this.size++;
        return true;
    }

    public void insertRoot(final T value) {
        final int LEFT = 1;
        final int RIGHT = 2;
        U current = this.getRoot();
        U nodeLeft = this.createTreeNode(Optional.ofNullable(value));
        int leftPos = LEFT;
        U nodeRight = nodeLeft;
        int rightPos = RIGHT;
        this.root = nodeLeft;
        while (Objects.nonNull(current)) {
            if (Objects.compare(current.getData(), value, this.cmp) < 0) {
                if (leftPos == LEFT) {
                    nodeLeft.setLeft(current);
                } else {
                    nodeLeft.setRight(current);
                }
                nodeLeft = current;
                leftPos = RIGHT;
                current = current.getRight();
            } else {
                if (rightPos == LEFT) {
                    nodeRight.setLeft(current);
                } else {
                    nodeRight.setRight(current);
                }
                rightPos = LEFT;
                nodeRight = current;
                current = current.getLeft();
            }
        }
        if (leftPos == LEFT) {
            nodeLeft.setLeft(null);
        } else {
            nodeLeft.setRight(null);
        }
        if (rightPos == RIGHT) {
            nodeRight.setLeft(null);
        } else {
            nodeRight.setRight(null);
        }
        this.size++;
    }

    public void remove(final U root, final T value) {
        this.removeTo(this.getRoot(), value);
    }

    protected boolean removeTo(U root, final T value) {
        final int GO_LEFT = -1;
        final int GO_RIGHT = 1;
        final int NO_STEP = 0;
        U parent = null;
        U current = this.getRoot();
        int comp = 0;
        int lastStep = NO_STEP;
        while (Objects.nonNull(current) && (comp = Objects.compare(current.getData(), value, this.cmp)) != 0) {
            parent = current;
            if (comp < 0) {
                lastStep = GO_RIGHT;
                current = current.getRight();
            } else {
                lastStep = GO_LEFT;
                current = current.getLeft();
            }
        }
        if (Objects.isNull(current)) {
            return false;
        }
        if (Objects.isNull(current.getLeft())) {
            if (lastStep == GO_RIGHT) {
                parent.setRight(current.getRight());
            } else if (lastStep == GO_LEFT) {
                parent.setLeft(current.getRight());
            } else {
                root = current.getRight();
            }
        } else if (Objects.isNull(current.getRight())) {
            if (lastStep == GO_RIGHT) {
                parent.setRight(current.getLeft());
            } else if (lastStep == GO_LEFT) {
                parent.setLeft(current.getLeft());
            } else {
                root = current.getLeft();
            }
        } else {
            U nodeToReplace = current.getRight();
            parent = current;
            while (Objects.nonNull(nodeToReplace.getLeft())) {
                parent = nodeToReplace;
                nodeToReplace = nodeToReplace.getLeft();
            }
            current.setData(nodeToReplace.getData());
            if (parent == current) {
                parent.setRight(nodeToReplace.getRight());
            } else {
                parent.setLeft(nodeToReplace.getRight());
            }
        }
        this.size--;
        return true;
    }

    @Override
    public U find(final T value) {
        return this.find(this.getRoot(), value);
    }

    protected U find(final U node, final T value) {
        if (Objects.isNull(node)) {
            return null;
        }
        if (Objects.compare(node.getData(), value, this.cmp) == 0) {
            return node;
        } else if (Objects.compare(node.getData(), value, cmp) <= 0) {
            return this.find(node.getLeft(), value);
        } else if (Objects.compare(node.getData(), value, cmp) > 0) {
            return this.find(node.getRight(), value);
        }
        return null;
    }
}
