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
package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.tree.iface.tree.ITreeCollection;
import com.wildbeeslabs.jentle.collections.iface.visitor.Visitor;
import com.wildbeeslabs.jentle.collections.tree.node.ACBaseTreeNode;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ACBaseTree<T, U extends ACBaseTreeNode<T, U>> extends ACTreeLike<T, U> implements ITreeCollection<T, U> {

    public ACBaseTree(final U root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    @Override
    public int size() {
        //return (Objects.isNull(this.root)) ? 0 : this.size;
        return this.nodeSize(this.getRoot());
    }

    public boolean isBalanced(final ACBaseTreeNode<T, U> root) {
        if (Objects.isNull(root)) {
            return true;
        }
        int heightDiff = this.height(root.getLeft()) - this.height(root.getRight());
        if (Math.abs(heightDiff) > 1) {
            return false;
        }
        return this.isBalanced(root.getLeft()) && this.isBalanced(root.getRight());
    }

    private int checkHeight(final ACBaseTreeNode<T, U> node) {
        if (Objects.isNull(node)) {
            return -1;
        }
        int leftHeight = this.checkHeight(node.getLeft());
        if (Objects.equals(leftHeight, Integer.MIN_VALUE)) {
            return Integer.MIN_VALUE;
        }
        int rightHeight = this.checkHeight(node.getRight());
        if (Objects.equals(rightHeight, Integer.MIN_VALUE)) {
            return Integer.MIN_VALUE;
        }
        int heightDiff = leftHeight - rightHeight;
        if (Math.abs(heightDiff) > 1) {
            return Integer.MIN_VALUE;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public boolean isBalanced2(final ACBaseTreeNode<T, U> node) {
        return (this.checkHeight(node) != Integer.MIN_VALUE);
    }

    public boolean checkBST(final ACBaseTreeNode<T, U> node) {
        return this.checkBST(node, null, null);
    }

    public boolean checkBST(final ACBaseTreeNode<T, U> node, final T min, final T max) {
        if (Objects.isNull(node)) {
            return true;
        }
        if ((Objects.nonNull(min) && Objects.compare(node.getData(), min, this.cmp) <= 0) || (Objects.nonNull(max) && Objects.compare(node.getData(), max, this.cmp) > 0)) {
            return false;
        }
        if (!this.checkBST(node.getLeft(), min, node.getData()) || !this.checkBST(node.getRight(), node.getData(), max)) {
            return false;
        }
        return true;
    }

    protected void traverseUpDown(final U node, final Visitor<T> visitor) {
        Objects.requireNonNull(node);
        final Stack<U> stack = new Stack<>();
        U current = node;
        while (true) {
            visitor.visit(current.getData());
            if (Objects.nonNull(current.getRight())) {
                stack.push(current.getRight());
            }
            if (Objects.nonNull(current.getLeft())) {
                current = current.getLeft();
            } else {
                current = stack.pop();
            }
        }
    }

    protected void traverseInfix(final U node, final Visitor<T> visitor) {
        Objects.requireNonNull(node);
        final Stack<U> stack = new Stack<>();
        U current = node;
        while (true) {
            stack.push(current);
            if (Objects.nonNull(current.getLeft())) {
                current = current.getLeft();
            } else {
                do {
                    current = stack.pop();
                    visitor.visit(current.getData());
                    current = current.getRight();
                } while (Objects.isNull(current));
            }
        }
    }

    protected U leftMostChild(U node) {
        if (Objects.isNull(node)) {
            return null;
        }
        while (Objects.nonNull(node.getLeft())) {
            node = node.getLeft();
        }
        return node;
    }

    public Iterator<T> breadthFirstIterator() {
        return new BreadthFirstIterator<>(this);
    }

    protected static class BreadthFirstIterator<T, U extends ACBaseTreeNode<T, U>> implements Iterator<T> {

        private Queue<U> queue = null;

        public BreadthFirstIterator(final ITreeCollection<T, U> source) {
            this.queue = new LinkedList<>();
            this.queue.offer(source.getRoot());
        }

        @Override
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override
        public T next() {
            final U current = this.queue.poll();
            if (Objects.nonNull(current.getLeft())) {
                this.queue.offer(current.getLeft());
            }
            if (Objects.nonNull(current.getRight())) {
                this.queue.offer(current.getRight());
            }
            return current.getData();
        }
    }

    protected static class UpDownIterator<T, U extends ACTreeNode2<T, U>> implements Iterator<T> {

        private U cursor = null;

        public UpDownIterator(final ITreeCollection<T, U> source) {
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
                while (Objects.nonNull(this.cursor) && this.cursor.isYoungest()) {
                    this.cursor = this.cursor.getRight();
                }
                if (Objects.nonNull(this.cursor)) {
                    this.cursor = this.cursor.getRight();
                }
            }
            return current;
        }
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
