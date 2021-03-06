/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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
package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended3;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Custom red-black tree implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CRedBlackTree<T> extends ACBaseTree<T, CRedBlackTree.CTreeNode<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CTreeNode<T> extends ACTreeNodeExtended3<T, CTreeNode<T>> {

        public CTreeNode() {
            this(null);
        }

        public CTreeNode(final T data) {
            this(data, null, null, null);
        }

        public CTreeNode(final T data, final CTreeNode<T> node, final CTreeNode<T> left, final CTreeNode<T> right) {
            super(data, node, left, right);
        }
    }

    public CRedBlackTree() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CRedBlackTree(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CRedBlackTree(final CRedBlackTree.CTreeNode<T> root) {
        this(root, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CRedBlackTree(final CRedBlackTree.CTreeNode<T> root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    @Override
    public Iterator<T> iterator() {
        return new CRedBlackTreeIterator<>(this);
    }

    protected static class CRedBlackTreeIterator<T> implements Iterator<T> {

        private CRedBlackTree.CTreeNode<? extends T> cursor = null;

        public CRedBlackTreeIterator(final CRedBlackTree<? extends T> source) {
            this.cursor = source.getRoot();
        }

        @Override
        public boolean hasNext() {
            return Objects.nonNull(this.cursor);
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                return null;
            }
            final T current = this.cursor.getData();
            if (Objects.nonNull(this.cursor.getLeft())) {
                this.cursor = this.cursor.getLeft();
            } else {
                while (Objects.nonNull(this.cursor)) {
                    this.cursor = this.cursor.getRight();
                }
                if (Objects.nonNull(this.cursor)) {
                    this.cursor = this.cursor.getRight();
                }
            }
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    protected T removeNode(final CRedBlackTree.CTreeNode<T> node) {
        final T data = node.getData();
        node.setLeft(null);
        node.setRight(null);
        node.setNode(null);
        return data;
    }

    protected void destroy() {
        CRedBlackTree.CTreeNode<T> current = this.getRoot();
        while (Objects.nonNull(current)) {
            removeNode(current);
            current = this.getRoot();
        }
    }

    protected void rotateLeft(final CRedBlackTree.CTreeNode<T> node) {
        if (Objects.isNull(node)) {
            return;
        }
        final CRedBlackTree.CTreeNode<T> rightNode = node.getRight();
        node.setRight(rightNode.getLeft());
        if (Objects.nonNull(rightNode.getLeft())) {
            rightNode.getLeft().setNode(node);
        }
        rightNode.setNode(node.getNode());
        if (Objects.isNull(node.getNode())) {
            this.setRoot(rightNode);
        } else if (node == node.getNode().getLeft()) {
            node.getNode().setLeft(rightNode);
        } else {
            node.getNode().setRight(rightNode);
        }
        rightNode.setLeft(node);
        node.setNode(rightNode);
    }

    protected void rotateRight(final CRedBlackTree.CTreeNode<T> node) {
        if (Objects.isNull(node)) {
            return;
        }
        final CRedBlackTree.CTreeNode<T> leftNode = node.getLeft();
        node.setLeft(leftNode.getRight());
        if (Objects.nonNull(leftNode.getRight())) {
            leftNode.getRight().setNode(node);
        }
        leftNode.setNode(node.getNode());
        if (Objects.isNull(node.getNode())) {
            this.setRoot(leftNode);
        } else if (node == node.getNode().getRight()) {
            node.getNode().setRight(leftNode);
        } else {
            node.getNode().setLeft(leftNode);
        }
        leftNode.setRight(node);
        node.setNode(leftNode);
    }

    @SuppressWarnings("null")
    public void insertNode(final CRedBlackTree.CTreeNode<T> node) {
        CRedBlackTree.CTreeNode<T> currentY = null;
        CRedBlackTree.CTreeNode<T> currentX = this.getRoot();
        while (Objects.nonNull(currentX)) {
            currentY = currentX;
            if (Objects.compare(node.getData(), currentX.getData(), this.cmp) < 0) {
                currentX = currentX.getLeft();
            } else {
                currentX = currentX.getRight();
            }
        }
        node.setNode(currentY);
        if (Objects.isNull(currentY)) {
            this.setRoot(node);
        } else if (Objects.compare(node.getData(), currentY.getData(), this.cmp) < 0) {
            currentY.setLeft(node);
        } else {
            currentY.setRight(node);
        }
        node.setLeft(null);
        node.setRight(null);
        node.setColor(ACTreeNodeExtended3.Color.BLACK);
        this.insertNodeInOrder(node);
    }

    public void deleteNode(final CRedBlackTree.CTreeNode<T> node) {
        if (Objects.isNull(node)) {
            return;
        }
        CRedBlackTree.CTreeNode<T> temp = node, temp2;
        ACTreeNodeExtended3.Color tempColor = temp.getColor();
        if (Objects.isNull(node.getLeft())) {
            temp2 = node.getRight();
            this.transplant(node, node.getRight());
        } else if (Objects.isNull(node.getRight())) {
            temp2 = node.getLeft();
            this.transplant(node, node.getLeft());
        } else {
            temp = this.min(node.getRight());
            tempColor = temp.getColor();
            temp2 = temp.getRight();
            if (temp.getNode() == node) {
                temp2.setNode(temp);
            } else {
                this.transplant(temp, temp.getRight());
                temp.setRight(node.getRight());
                temp.getRight().setNode(temp);
            }
            this.transplant(node, temp);
            temp.setLeft(node.getLeft());
            temp.getLeft().setNode(temp);
            temp.setColor(node.getColor());
            if (temp.getLeft() == temp.getRight()) {
                temp.setRight(null);
            }
        }
        if (Objects.equals(tempColor, ACTreeNodeExtended3.Color.BLACK)) {
            this.deleteNodeInOrder(temp2);
        }
    }

    protected void insertNodeInOrder(final CRedBlackTree.CTreeNode<T> node) {
        CRedBlackTree.CTreeNode<T> currentNode = node;
        while (Objects.equals(currentNode.getNode().getColor(), ACTreeNodeExtended3.Color.RED)) {
            if (currentNode.getNode() == currentNode.getNode().getNode().getLeft()) {
                final CRedBlackTree.CTreeNode<T> temp = currentNode.getNode().getNode().getRight();
                if (Objects.equals(temp.getColor(), ACTreeNodeExtended3.Color.RED)) {
                    currentNode.getNode().setColor(ACTreeNodeExtended3.Color.BLACK);
                    temp.setColor(ACTreeNodeExtended3.Color.BLACK);
                    currentNode.getNode().getNode().setColor(ACTreeNodeExtended3.Color.RED);
                    currentNode = currentNode.getNode().getNode();
                } else {
                    if (currentNode == currentNode.getNode().getRight()) {
                        currentNode = currentNode.getNode();
                        this.rotateLeft(currentNode);
                    }
                    currentNode.getNode().setColor(ACTreeNodeExtended3.Color.BLACK);
                    currentNode.getNode().getNode().setColor(ACTreeNodeExtended3.Color.RED);
                    this.rotateRight(currentNode.getNode().getNode());
                }
            } else {
                final CRedBlackTree.CTreeNode<T> temp = currentNode.getNode().getNode().getLeft();
                if (Objects.equals(temp.getColor(), ACTreeNodeExtended3.Color.RED)) {
                    currentNode.getNode().setColor(ACTreeNodeExtended3.Color.BLACK);
                    temp.setColor(ACTreeNodeExtended3.Color.BLACK);
                    currentNode.getNode().getNode().setColor(ACTreeNodeExtended3.Color.RED);
                    currentNode = currentNode.getNode().getNode();
                } else {
                    if (currentNode == currentNode.getNode().getLeft()) {
                        currentNode = currentNode.getNode();
                        this.rotateRight(currentNode);
                    }
                    currentNode.getNode().setColor(ACTreeNodeExtended3.Color.BLACK);
                    currentNode.getNode().getNode().setColor(ACTreeNodeExtended3.Color.RED);
                    this.rotateLeft(currentNode.getNode().getNode());
                }
            }
        }
        root.setColor(ACTreeNodeExtended3.Color.BLACK);
    }

    protected void deleteNodeInOrder(final CRedBlackTree.CTreeNode<T> node) {
        CRedBlackTree.CTreeNode<T> currentNode = node;
        while (!this.isRoot(currentNode) && Objects.equals(currentNode.getColor(), ACTreeNodeExtended3.Color.BLACK)) {
            if (currentNode == currentNode.getNode().getLeft()) {
                CRedBlackTree.CTreeNode<T> temp = currentNode.getNode().getRight();
                if (Objects.equals(temp.getColor(), ACTreeNodeExtended3.Color.RED)) {
                    temp.setColor(ACTreeNodeExtended3.Color.BLACK);
                    temp.getNode().setColor(ACTreeNodeExtended3.Color.RED);
                    this.rotateLeft(currentNode.getNode());
                    temp = currentNode.getNode().getRight();
                }
                if (Objects.equals(temp.getLeft().getColor(), ACTreeNodeExtended3.Color.BLACK) && Objects.equals(temp.getRight().getColor(), ACTreeNodeExtended3.Color.BLACK)) {
                    temp.setColor(ACTreeNodeExtended3.Color.RED);
                    currentNode = currentNode.getNode();
                } else {
                    if (Objects.equals(temp.getRight().getColor(), ACTreeNodeExtended3.Color.BLACK)) {
                        temp.getLeft().setColor(ACTreeNodeExtended3.Color.BLACK);
                        temp.setColor(ACTreeNodeExtended3.Color.RED);
                        this.rotateRight(temp);
                        temp = currentNode.getNode().getRight();
                    }
                    temp.setColor(currentNode.getNode().getColor());
                    currentNode.getNode().setColor(ACTreeNodeExtended3.Color.BLACK);
                    temp.getRight().setColor(ACTreeNodeExtended3.Color.BLACK);
                    this.rotateLeft(currentNode.getNode());
                    currentNode = this.getRoot();
                }
            } else {
                CRedBlackTree.CTreeNode<T> temp = currentNode.getNode().getLeft();
                if (Objects.equals(temp.getColor(), ACTreeNodeExtended3.Color.RED)) {
                    temp.setColor(ACTreeNodeExtended3.Color.BLACK);
                    currentNode.getNode().setColor(ACTreeNodeExtended3.Color.RED);
                    this.rotateRight(currentNode.getNode());
                    temp = currentNode.getNode().getLeft();
                }
                if (Objects.equals(temp.getRight().getColor(), ACTreeNodeExtended3.Color.BLACK) && Objects.equals(temp.getLeft().getColor(), ACTreeNodeExtended3.Color.BLACK)) {
                    temp.setColor(ACTreeNodeExtended3.Color.RED);
                    currentNode = currentNode.getNode();
                } else {
                    if (Objects.equals(temp.getLeft().getColor(), ACTreeNodeExtended3.Color.BLACK)) {
                        temp.getRight().setColor(ACTreeNodeExtended3.Color.BLACK);
                        temp.setColor(ACTreeNodeExtended3.Color.RED);
                        this.rotateLeft(temp);
                        temp = currentNode.getNode().getLeft();
                    }
                    temp.setColor(currentNode.getNode().getColor());
                    currentNode.getNode().setColor(ACTreeNodeExtended3.Color.BLACK);
                    temp.getLeft().setColor(ACTreeNodeExtended3.Color.BLACK);
                    this.rotateRight(currentNode.getNode());
                    currentNode = this.getRoot();
                }
            }
        }
        currentNode.setColor(ACTreeNodeExtended3.Color.BLACK);
    }

    public CRedBlackTree.CTreeNode<T> search(final T value) {
        CRedBlackTree.CTreeNode<T> current = this.getRoot();
        while (Objects.nonNull(current) && Objects.compare(current.getData(), value, this.cmp) != 0) {
            if (Objects.compare(current.getData(), value, this.cmp) > 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return current;
    }

    public CRedBlackTree.CTreeNode<T> min(CRedBlackTree.CTreeNode<T> node) {
        if (Objects.isNull(node)) {
            return null;
        }
        while (Objects.nonNull(node.getLeft())) {
            node = node.getLeft();
        }
        return node;
    }

    public CRedBlackTree.CTreeNode<T> max(final CRedBlackTree.CTreeNode<T> node) {
        CRedBlackTree.CTreeNode<T> currentNode = node;
        if (Objects.isNull(currentNode)) {
            return null;
        }
        while (Objects.nonNull(currentNode.getRight())) {
            currentNode = currentNode.getRight();
        }
        return currentNode;
    }

    protected void transplant(final CRedBlackTree.CTreeNode<T> node1, final CRedBlackTree.CTreeNode<T> node2) {
        if (Objects.isNull(node1)) {
            return;
        }
        if (Objects.isNull(node1.getNode())) {
            this.setRoot(node2);
        } else if (node1 == node1.getNode().getLeft()) {
            node1.getNode().setLeft(node2);
        } else {
            node1.getNode().setRight(node2);
        }
        node2.setNode(node1.getNode());
    }

    @Override
    protected CRedBlackTree.CTreeNode<T> createTreeNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CRedBlackTree.CTreeNode<>(value.get());
        }
        return new CRedBlackTree.CTreeNode<>();
    }
}
