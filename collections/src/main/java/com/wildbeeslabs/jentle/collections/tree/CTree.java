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
import com.wildbeeslabs.jentle.collections.interfaces.ITree;
import com.wildbeeslabs.jentle.collections.interfaces.IVisitor;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom tree implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CTree<T> implements ITree<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @Data
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @ToString
    protected static class CTreeNode<T> {

        protected T data;
        protected CTreeNode<T> left;
        protected CTreeNode<T> right;

        public CTreeNode() {
            this(null);
        }

        public CTreeNode(final T data) {
            this(data, null, null);
        }
    }

    protected CTreeNode<T> root;
    protected final Comparator<? super T> cmp;

    public CTree() {
        this(CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CTree(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CTree(final CTreeNode<T> root, final Comparator<? super T> cmp) {
        this.root = root;
        this.cmp = cmp;
    }

    public int height() {
        return this.height(this.root);
    }

    private int height(final CTreeNode<T> node) {
        if (Objects.isNull(node)) {
            return 0;
        } else {
            return Math.max(this.height(node.left), this.height(node.right)) + 1;
        }
    }

    public int nodesOnLevel(int level) {
        return this.nodesOnLevel(this.root, level);
    }

    private int nodesOnLevel(final CTreeNode<T> node, int level) {
        if (Objects.isNull(node)) {
            return 0;
        }
        if (1 == level) {
            return 1;
        }
        return this.nodesOnLevel(node.left, level - 1) + this.nodesOnLevel(node.right, level - 1);
    }

    public CTreeNode<T> getRoot() {
        return this.root;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    public void inOrderTraversal(final CTreeNode<T> node, final IVisitor<T> visitor) {
        if (Objects.nonNull(node)) {
            inOrderTraversal(node.left, visitor);
            visitor.visit(node.data);
            inOrderTraversal(node.right, visitor);
        }
    }

    public void preOrderTraversal(final CTreeNode<T> node, final IVisitor<T> visitor) {
        if (Objects.nonNull(node)) {
            visitor.visit(node.data);
            preOrderTraversal(node.left, visitor);
            preOrderTraversal(node.right, visitor);
        }
    }

    public void postOrderTraversal(final CTreeNode<T> node, final IVisitor<T> visitor) {
        if (Objects.nonNull(node)) {
            postOrderTraversal(node.left, visitor);
            postOrderTraversal(node.right, visitor);
            visitor.visit(node.data);
        }
    }

    public CTree<T> fromArray(final T[] array) {
        Objects.requireNonNull(array);
        if (array.length > 0) {
            final CTreeNode<T> root = new CTreeNode<>(array[0]);
            final Queue<CTreeNode<T>> queue = new LinkedList<>();
            queue.add(root);
            boolean done = false;
            int i = 1;
            while (!done) {
                CTreeNode<T> r = queue.element();
                if (Objects.isNull(r.left) && Objects.compare(array[i], r.data, this.cmp) < 0) {
                    r.left = new CTreeNode<>(array[i]);
                    i++;
                    queue.add(r.left);
                } else if (Objects.isNull(r.right) && Objects.compare(array[i], r.data, this.cmp) >= 0) {
                    r.right = new CTreeNode<>(array[i]);
                    i++;
                    queue.add(r.right);
                } else {
                    queue.remove();
                }
                if (i == array.length) {
                    done = true;
                }
            }
            return new CTree<>(root, this.cmp);
        }
        return null;
    }
}
