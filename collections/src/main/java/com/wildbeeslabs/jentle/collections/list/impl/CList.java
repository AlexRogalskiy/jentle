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
package com.wildbeeslabs.jentle.collections.list.impl;

import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.list.iface.IList;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

/**
 * Custom list implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CList<T> extends ACList<T, CList.CListNode<T>> implements IList<T, CList.CListNode<T>> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CListNode<T> extends ACListNode<T, CListNode<T>> {

        public CListNode() {
            this(null);
        }

        public CListNode(final T data) {
            this(data, null);
        }

        public CListNode(final T data, final CListNode<T> next) {
            super(data, next);
        }
    }

    public CList() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CList(final IList<T, CList.CListNode<T>> source) {
        this(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CList(final IList<T, CList.CListNode<T>> source, final Comparator<? super T> cmp) {
        super(source, cmp);
    }

    @Override
    public Iterator<T> iterator() {
        return new CListIterator<>(this);
    }

    protected static class CListIterator<T> implements Iterator<T> {

        private CList.CListNode<? extends T> cursor;

        public CListIterator(final CList<? extends T> source) {
            this.cursor = source.first;
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
            this.cursor = this.cursor.getNext();
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public Enumeration<T> enumerator() {
        return new CList.ListEnumeration<>(this);
    }

    protected static class ListEnumeration<T> implements Enumeration<T> {

        private CList.CListNode<T> cursor;

        public ListEnumeration(final CList<T> source) {
            this.cursor = source.getRoot();
        }

        public boolean hasMoreElements() {
            return Objects.nonNull(this.cursor);
        }

        public T nextElement() {
            if (!this.hasMoreElements()) {
                throw new EmptyListException("ERROR: list is empty");
            }
            final CList.CListNode<T> var1 = this.cursor;
            this.cursor = this.cursor.getNext();
            return var1.getData();
        }
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected CList.CListNode<T> createNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CList.CListNode<>(value.get());
        }
        return new CList.CListNode<>();
    }
}
