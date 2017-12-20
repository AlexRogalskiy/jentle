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

import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.interfaces.IVisitor;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom circular list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CCircularList<T> extends ACList<T, CList.CListNode<T>> implements IList<T> {

//    protected CList.CListNode<T> first;
//    protected CList.CListNode<T> last;
//    protected int size;

    public CCircularList() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CCircularList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public CCircularList(final IList<T> source) {
        this(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public CCircularList(final IList<T> source, final Comparator<? super T> cmp) {
        super(source, cmp);
        this.first = this.last = null;
        this.size = 0;
    }

    @Override
    public void addFirst(final T value) {
        final CList.CListNode<T> temp = new CList.CListNode<>(value);
        if (Objects.isNull(this.last)) {
            this.last = temp;
            this.last.setNext(this.last);
        } else {
            temp.setNext(this.last.getNext());
            this.last.setNext(temp);
        }
        this.size++;
    }

    @Override
    public void addLast(final T value) {
        this.addFirst(value);
        this.last = this.last.getNext();
    }

    public T removeAtHead() throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: CCircularList (empty size=%i)", this.getSize()));
        }
        final T removed = this.last.getNext().getData();
        if (this.last.getNext() == this.last) {
            this.last = null;
        } else {
            this.last.setNext(this.last.getNext().getNext());
        }
        this.size--;
        return removed;
    }

    @Override
    public T head() throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.getSize()));
        }
        return this.last.getNext().getData();
    }

    @Override
    public T tail() throws EmptyListException {
        if (Objects.isNull(this.last)) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.getSize()));
        }
        return this.last.getData();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());//Objects.isNull(this.last)
    }

    @Override
    public void insertAt(T item, int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<? extends T> iterator() {
        return new CCircularListIterator<>(this);
    }

    protected static class CCircularListIterator<T> implements Iterator<T> {

        private CList.CListNode<? extends T> first = null;
        private CList.CListNode<? extends T> last = null;

        public CCircularListIterator(final CCircularList<? extends T> source) {
            this.first = source.first;
            this.last = source.last;
        }

        @Override
        public boolean hasNext() {
            return (this.first != this.last);
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                return null;
            }
            final T current = this.first.getData();
            this.first = this.first.getNext();
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    protected CList.CListNode<T> createNode(final T value) {
        return new CList.CListNode<>(value);
    }
}
