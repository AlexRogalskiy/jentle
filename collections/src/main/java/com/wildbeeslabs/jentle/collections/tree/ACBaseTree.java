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
        return this.size;//(Objects.isNull(this.root)) ? 0 : this.size;
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

    public boolean isBalanced(final ACTreeNode<T, U> root) {
        if (Objects.isNull(root)) {
            return true;
        }
        int heightDiff = this.height(root.getLeft()) - this.height(root.getRight());
        if (Math.abs(heightDiff) > 1) {
            return false;
        }
        return this.isBalanced(root.getLeft()) && this.isBalanced(root.getRight());
    }

    private int checkHeight(final ACTreeNode<T, U> root) {
        if (Objects.isNull(root)) {
            return -1;
        }
        int leftHeight = this.checkHeight(root.getLeft());
        if (Objects.equals(leftHeight, Integer.MIN_VALUE)) {
            return Integer.MIN_VALUE;
        }
        int rightHeight = this.checkHeight(root.getRight());
        if (Objects.equals(rightHeight, Integer.MIN_VALUE)) {
            return Integer.MIN_VALUE;
        }
        int heightDiff = leftHeight - rightHeight;
        if (Math.abs(heightDiff) > 1) {
            return Integer.MIN_VALUE;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public boolean isBalanced2(final ACTreeNode<T, U> root) {
        return (this.checkHeight(root) != Integer.MIN_VALUE);
    }

    public boolean checkBST(final ACTreeNode<T, U> root) {
        return this.checkBST(root, null, null);
    }

    public boolean checkBST(final ACTreeNode<T, U> root, final T min, final T max) {
        if (Objects.isNull(root)) {
            return true;
        }
        if ((Objects.nonNull(min) && Objects.compare(root.getData(), min, this.cmp) <= 0) || (Objects.nonNull(max) && Objects.compare(root.getData(), max, this.cmp) > 0)) {
            return false;
        }
        if (!this.checkBST(root.getLeft(), min, root.getData()) || !this.checkBST(root.getRight(), root.getData(), max)) {
            return false;
        }
        return true;
    }

    protected abstract U createTreeNode(final Optional<? extends T> value);
}
