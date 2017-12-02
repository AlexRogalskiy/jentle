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
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;
import java.util.Comparator;

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
public abstract class CBinaryTree<T> implements ITreeExtended<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class CBinaryTreeNode<T> extends ACTreeNode<T, CBinaryTreeNode<T>> {

        protected CBinaryTreeNode<T> parent;

        public void setLeft(final CBinaryTreeNode<T> left) {
            this.left = left;
            this.left.parent = this;
        }

        public void setRight(final CBinaryTreeNode<T> right) {
            this.right = right;
            this.right.parent = this;
        }

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
            super(data, left, right);
            this.parent = parent;
        }
    }

    protected CBinaryTreeNode<T> root;
    protected final Comparator<? super T> cmp;

    public CBinaryTree() {
        this(CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CBinaryTree(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CBinaryTree(final CBinaryTreeNode<T> root, final Comparator<? super T> cmp) {
        this.root = root;
        this.cmp = cmp;
    }
}
