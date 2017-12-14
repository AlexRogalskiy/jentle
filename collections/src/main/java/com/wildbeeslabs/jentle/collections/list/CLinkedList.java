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
package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.list.CLinkedList.CLinkedListNode;
import com.wildbeeslabs.jentle.collections.list.node.ACListNodeExtended;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;
import java.util.Iterator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom linked list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CLinkedList<T> extends ACList<T, CLinkedList.CLinkedListNode<T>> implements IList<T> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString
    public static class CLinkedListNode<T> extends ACListNodeExtended<T, CLinkedListNode<T>> {

        public CLinkedListNode() {
            this(null);
        }

        public CLinkedListNode(final T data) {
            this(data, null, null);
        }

        public CLinkedListNode(final T data, final CLinkedListNode<T> previous, final CLinkedListNode<T> next) {
            super(data, next, previous);
        }
    }

    public CLinkedList() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CLinkedList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CLinkedList(final CLinkedList<T> source) {
        this(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CLinkedList(final CLinkedList<T> source, final Comparator<? super T> cmp) {
        super(source, cmp);
    }

    @Override
    public void addLast(final T item) {
        this.addToLast(item);
    }

    @Override
    public void addFirst(final T item) {
        this.addToFirst(item);
    }

    @Override
    public void insertAt(final T item, int index) {
        this.insertAt(item, index);
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected CLinkedList.CLinkedListNode<T> createNode(final T value) {
        return new CLinkedList.CLinkedListNode<>(value);
    }
}
