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

import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * Custom binary tree2 implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CBinaryTree2<T> extends ACBinaryTree<T, CBinaryTree2.CTreeNode2<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString
    public static class CTreeNode2<T> extends ACTreeNode<T, CTreeNode2<T>> {

        @Setter(AccessLevel.NONE)
        protected int size;

        public CTreeNode2() {
            this(null);
        }

        public CTreeNode2(final T data) {
            this(data, null, null);
        }

        public CTreeNode2(final T data, final CTreeNode2<T> left, final CTreeNode2<T> right) {
            super(data, left, right);
            this.size = 1;
        }

        public CTreeNode2<T> getIthNode(int index) {
            int leftSize = Objects.isNull(this.getLeft()) ? 0 : this.getLeft().size;
            if (index < leftSize) {
                return this.left.getIthNode(index);
            } else if (index == leftSize) {
                return this;
            } else {
                return this.right.getIthNode(index - (leftSize + 1));
            }
        }
    }

    public CBinaryTree2() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CBinaryTree2(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CBinaryTree2(final CBinaryTree2.CTreeNode2<T> root) {
        this(root, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CBinaryTree2(final CBinaryTree2.CTreeNode2<T> root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    @Override
    public void insert(final T value) {
        this.insertTo(value);
    }

    public CTreeNode2<T> getRandom() {
        if (this.isEmpty()) {
            return null;
        }
        int index = new Random().nextInt(this.size());
        return this.root.getIthNode(index);
    }

    @Override
    protected boolean insert(final CTreeNode2<T> node, final CTreeNode2<T> newNode, boolean isExact) {
        if (this.insert(node, newNode, isExact)) {
            node.size++;
            return true;
        }
        return false;
    }

    @Override
    protected CBinaryTree2.CTreeNode2<T> createTreeNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CBinaryTree2.CTreeNode2<>(value.get());
        }
        return new CBinaryTree2.CTreeNode2<>();
    }
}
