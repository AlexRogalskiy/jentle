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

import com.wildbeeslabs.jentle.collections.tree.node.CExtendedTreeNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Optional;

/**
 * Custom extended tree implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CTreeExtended<T> extends ACTreeExtended<T, CExtendedTreeNode<T>> {

    public CTreeExtended() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTreeExtended(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CTreeExtended(final CExtendedTreeNode<T> root) {
        this(root, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTreeExtended(final CExtendedTreeNode<T> root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    @Override
    public void insertLeft(final CExtendedTreeNode<T> node, final Optional<? extends T> value) {
        this.insertLeft(node, value);
    }

    @Override
    public void insertRight(final CExtendedTreeNode<T> node, final Optional<? extends T> value) {
        this.insertToRight(node, value);
    }

    @Override
    protected CExtendedTreeNode<T> createTreeNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CExtendedTreeNode<>(value.get());
        }
        return new CExtendedTreeNode<>();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
}
