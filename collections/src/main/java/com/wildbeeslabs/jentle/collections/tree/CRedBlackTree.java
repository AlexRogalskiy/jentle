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
package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNodeExtended3;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom red-black tree implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CRedBlackTree<T> extends ACBaseTree<T, CRedBlackTree.CTreeNode<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString
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

    protected CRedBlackTree.CTreeNode<T> node;

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

    @Override
    protected CRedBlackTree.CTreeNode<T> createTreeNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CRedBlackTree.CTreeNode<>(value.get());
        }
        return new CRedBlackTree.CTreeNode<>();
    }
}
