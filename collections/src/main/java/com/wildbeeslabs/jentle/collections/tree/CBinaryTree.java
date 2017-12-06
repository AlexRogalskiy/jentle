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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@EqualsAndHashCode(callSuper = true)
@ToString
public class CBinaryTree<T> extends ACBinaryTree<T, CTree.CTreeNode<T>> {

    public CBinaryTree() {
        this(CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CBinaryTree(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CBinaryTree(final CTree.CTreeNode<T> root) {
        this(root, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CBinaryTree(final CTree.CTreeNode<T> root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    public static <T> CTree<T> createMinimalBST(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        Arrays.sort(array, cmp);
        final CTree.CTreeNode<T> node = createMinimalBST(array, 0, array.length - 1);
        return new CTree<>(node);
    }

    protected static <T> CTree.CTreeNode<T> createMinimalBST(final T[] array, int start, int end) {
        if (end < start) {
            return null;
        }
        int middle = (start + end) / 2;
        final CTree.CTreeNode<T> node = new CTree.CTreeNode<>(array[middle]);
        node.setLeft(createMinimalBST(array, start, middle - 1));
        node.setRight(createMinimalBST(array, middle + 1, end));
        return node;
    }

    @Override
    protected CTree.CTreeNode<T> createTreeNode(Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CTree.CTreeNode<>(value.get());
        }
        return new CTree.CTreeNode<>();
    }
}
