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

import com.wildbeeslabs.jentle.collections.tree.node.ACBaseTreeNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@EqualsAndHashCode(callSuper = true)
@ToString
public class CTree<T> extends ACTree<T, CTree.CTreeNode<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString
    public static class CTreeNode<T> extends ACBaseTreeNode<T, CTreeNode<T>> {

        public CTreeNode() {
            this(null);
        }

        public CTreeNode(final T data) {
            this(data, null, null);
        }

        public CTreeNode(final T data, final CTreeNode<T> left, final CTreeNode<T> right) {
            super(data, left, right);
        }
    }

    public CTree() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTree(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CTree(final CTree.CTreeNode<T> root) {
        this(root, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTree(final CTree.CTreeNode<T> root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    @Override
    public void insertLeft(final CTree.CTreeNode<T> node, final Optional<? extends T> value) {
        this.insertLeft(null, value);
    }

    @Override
    public void insertRight(final CTree.CTreeNode<T> node, final Optional<? extends T> value) {
        this.insertToRight(node, value);
    }

    public int height() {
        return this.height(this.root);
    }

    public int nodesOnLevel(int level) {
        return this.nodesOnLevel(this.root, level);
    }

    public static <T> CTree<T> fromArray(final T[] array) {
        return fromArray(array, 0, array.length - 1);
    }

    public static <T> CTree<T> fromArray(final T[] array, int start, int end) {
        if (Objects.isNull(array) || array.length == 0) {
            return null;
        }
        assert (start < array.length && end < array.length && start <= end);
        final CTree.CTreeNode<T> rootNode = new CTreeNode<>(array[start]);
        if (end - start > 0) {
            final Queue<CTree.CTreeNode<T>> queue = new LinkedList<>();
            queue.add(rootNode);
            boolean done = false;
            int current = start + 1;
            while (!done) {
                CTree.CTreeNode<T> node = queue.element();
                if (Objects.isNull(node.getLeft())) {
                    node.setLeft(new CTreeNode<>(array[current++]));
                    queue.add(node.getLeft());
                } else if (Objects.isNull(node.getRight())) {
                    node.setRight(new CTreeNode<>(array[current++]));
                    queue.add(node.getRight());
                } else {
                    queue.remove();
                }
                if (current == end) {
                    done = true;
                }
            }
        }
        return new CTree<>(rootNode, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    @Override
    protected CTree.CTreeNode<T> createTreeNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CTree.CTreeNode<>(value.get());
        }
        return new CTree.CTreeNode<>();
    }
}
