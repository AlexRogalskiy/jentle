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
package com.wildbeeslabs.jentle.algorithms.tictactoe;

import com.wildbeeslabs.jentle.collections.tree.impl.ACTreeLike;
import com.wildbeeslabs.jentle.collections.tree.node.ACTreeNode;
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
 * Custom tree implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CTreeSearch<T extends CState> extends ACTreeLike<T, CTreeSearch.CTreeSearchNode<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CTreeSearchNode<T extends CState> extends ACTreeNode<T, CTreeSearchNode<T>> {

        private static final Comparator<? super CState> DEFAULT_COMPARATOR = Comparator.comparing(d -> d.getVisitCount());

        public CTreeSearchNode() {
            this(null, DEFAULT_COMPARATOR);
        }

        public CTreeSearchNode(final T data) {
            this(data, DEFAULT_COMPARATOR);
        }

        public CTreeSearchNode(final T data, final Comparator<? super T> comparator) {
            this(data, null, comparator);
        }

        public CTreeSearchNode(final T data, final CTreeSearchNode<T> parent, final Comparator<? super T> comparator) {
            super(data, parent, comparator);
        }

        public CTreeSearchNode(final CTreeSearchNode<T> node) {
            super(node);
        }
    }

    public CTreeSearch() {
        this(CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTreeSearch(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CTreeSearch(final CTreeSearch.CTreeSearchNode<T> root) {
        this(root, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CTreeSearch(final CTreeSearch.CTreeSearchNode<T> root, final Comparator<? super T> cmp) {
        super(root, cmp);
    }

    public void addChild(final CTreeSearch.CTreeSearchNode<T> parent, final CTreeSearch.CTreeSearchNode<T> child) {
        if (Objects.nonNull(parent)) {
            parent.addChild(child);
        }
    }

    @Override
    protected CTreeSearchNode<T> createTreeNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CTreeSearch.CTreeSearchNode<>(value.get());
        }
        return new CTreeSearch.CTreeSearchNode<>();
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
