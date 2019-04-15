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
import com.wildbeeslabs.jentle.collections.list.iface.IListLike;
import com.wildbeeslabs.jentle.collections.iface.visitor.IResultVisitor;
import com.wildbeeslabs.jentle.collections.iface.visitor.IVisitor;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom abstract list implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 * @param <E>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ACList<T, E extends ACListNode<T, E>> extends ACBaseList<T, E> implements IList<T, E> {

    protected E last;

    public ACList() {
        this(null, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACList(final Comparator<? super T> cmp) {
        this(null, cmp);
    }

    public ACList(final IList<T, E> source) {
        this(source, CUtils.DEFAULT_SORT_COMPARATOR);
    }

    public ACList(final IList<T, E> source, final Comparator<? super T> cmp) {
        super(source, cmp);
        this.last = null;
        this.addList(source);
    }

    protected void addList(final IListLike<T, E> source) {
        if (Objects.nonNull(source)) {
            for (Iterator<? extends T> iterator = source.iterator(); iterator.hasNext();) {
                this.addLast(iterator.next());
            }
        }
    }

    public T head() throws EmptyListException {
        if (Objects.isNull(this.first)) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.size));
        }
        return this.first.getData();
    }

    public T tail() throws EmptyListException {
        if (Objects.isNull(this.last)) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.size));
        }
        return this.last.getData();
    }

    @Override
    public void addFirst(final T item) {
        this.addToFirst(Optional.of(item));
    }

    @Override
    public void addLast(final T item) {
        this.addToLast(Optional.of(item));
    }

    @Override
    public void insertAt(final T item, int index) {
        this.insertToAt(Optional.of(item), index);
    }

    protected E addToFirst(final Optional<? extends T> item) {
        final E temp = this.createNode(item);
        temp.setNext(this.first);
        if (Objects.isNull(this.first)) {
            this.last = temp;
        }
        this.first = temp;
        this.size++;
        return this.first;
    }

    protected E addToLast(final Optional<? extends T> item) {
        final E temp = this.createNode(item);
        temp.setNext(null);
        if (Objects.isNull(this.last)) {
            this.first = temp;
        } else {
            this.last.setNext(temp);
        }
        this.last = temp;
        this.size++;
        return this.last;
    }

    @Override
    public T removeFirst() throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        final T removed = this.first.getData();
        this.first = this.first.getNext();
        this.size--;
        return removed;
    }

    @Override
    public T removeLast() throws EmptyListException {
        if (this.isEmpty()) {
            throw new EmptyListException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        E previous = this.first, next = this.first;
        while (Objects.nonNull(next.getNext())) {
            previous = next;
            next = next.getNext();
        }
        if (Objects.isNull(previous)) {
            this.first = null;
        } else {
            previous.setNext(null);
        }
        this.last = previous;
        this.size--;
        return next.getData();
    }

    @Override
    public boolean remove(final Object item) {
        if (this.isEmpty()) {
            throw new NoSuchElementException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        E previous = this.first, next = this.first;
        boolean removed = false;
        while (Objects.nonNull(next)) {
            if (Objects.compare((T) item, next.getData(), this.cmp) == 0) {
                removed = true;
                this.size--;
                if (Objects.nonNull(previous)) {
                    previous.setNext(next.getNext());
                }
            } else {
                previous = next;
            }
            next = next.getNext();
        }
        this.last = previous;
        return removed;
    }

    protected E insertToAt(final Optional<? extends T> item, int index) {
        this.checkRange(index);
        E previous = this.first, next = this.first;
        while (Objects.nonNull(next) && --index > 0) {
            previous = next;
            next = next.getNext();
        }
        final E temp = this.createNode(item);
        temp.setNext(next);
        if (Objects.isNull(next)) {
            this.last = temp;
        }
        if (Objects.isNull(previous)) {
            this.first = temp;
        } else {
            previous.setNext(temp);
        }
        this.size++;
        return temp;
    }

    protected E getToAt(int index) {
        this.checkRange(index);
        E current = this.first;
        while (Objects.nonNull(current) && --index > 0) {
            current = current.getNext();
        }
        if (Objects.nonNull(current)) {
            return current;
        }
        return null;
    }

    @Override
    public T get(int index) {
        final E current = this.getToAt(index);
        if (Objects.nonNull(current)) {
            return current.getData();
        }
        return null;
    }

    @Override
    public boolean contains(final Object item) {
        for (final Iterator<? extends T> i = this.iterator(); i.hasNext();) {
            if (Objects.compare(i.next(), (T) item, this.cmp) == 0) {
                return true;
            }
        }
        return false;
    }

    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index <= 0 || index > this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (index=%d is out of bounds [1, %d])", this.getClass().getName(), index, this.size));
        }
    }

    public E remove(E root) {
        E backup = null;
        while (Objects.nonNull(root) && root != this.last) {
            backup = root.getNext();
            root.setNext(null);
            root = backup;
        }
        return root;
    }

    @Override
    public void clear() {
        this.remove(this.first);
        this.first = this.last = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.length(this.first);//return this.size;
    }

    public boolean isDistinct() {
        for (Iterator<? extends T> i = this.iterator(); i.hasNext();) {
            T item = i.next();
            for (Iterator<? extends T> j = this.iterator(); j.hasNext();) {
                if (Objects.compare(j.next(), item, this.cmp) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean delete(final E node) {
        if (this.isEmpty() || Objects.isNull(node) || Objects.isNull(node.getNext())) {
            return false;
        }
        E current = node.getNext();
        node.setData(current.getData());
        node.setNext(current.getNext());
        return true;
    }

    protected E insertBefore(final E list, final Optional<? extends T> data) {
        return this.insertBefore(list, this.createNode(data));
    }

    @SuppressWarnings("null")
    protected E insertBefore(final E first, final E second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        second.setNext(first);
        E current = this.first, previous = null;
        while (Objects.nonNull(current) && current != first) {
            previous = current;
            current = current.getNext();
        }
        if (Objects.nonNull(previous)) {
            previous.setNext(second);
        }
        return second;
    }

    protected E insertAfter(final E list, final Optional<? extends T> data) {
        return this.insertAfter(list, this.createNode(data));
    }

    protected E insertAfter(final E first, final E second) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        second.setNext(first.getNext());
        first.setNext(second);
        return second;
    }

    public void addLists(final E first, final E last, final ACList<T, E> result, final IResultVisitor<T, T> visitor) {
        if (Objects.isNull(first) && Objects.isNull(last) || Objects.isNull(visitor)) {
            return;
        }
        if (Objects.nonNull(first)) {
            visitor.visit(first.getData());
        }
        if (Objects.nonNull(last)) {
            visitor.visit(last.getData());
        }
        result.addLast(visitor.getResult());
        if (Objects.nonNull(first) || Objects.nonNull(last)) {
            addLists(Objects.isNull(first) ? null : first.getNext(), Objects.isNull(last) ? null : last.getNext(), result, visitor);
        }
    }

    public void each(final IVisitor visitor) {
        for (final Iterator<? extends T> current = this.iterator(); current.hasNext();) {
            visitor.visit(current.next());
        }
    }

    @SuppressWarnings("LocalVariableHidesMemberVariable")
    protected int length(final E node) {
        int size = 0;
        E current = node;
        while (Objects.nonNull(current)) {
            size++;
            current = current.getNext();
        }
        return size;
    }

    protected boolean isEqual(final E last) {
        E headFirst = this.first;
        E headLast = last;
        while (Objects.nonNull(headFirst) && Objects.nonNull(headLast)) {
            if (Objects.compare(headFirst.getData(), headLast.getData(), this.cmp) != 0) {
                return false;
            }
            headFirst = headFirst.getNext();
            headLast = headLast.getNext();
        }
        return (Objects.nonNull(headFirst) && Objects.nonNull(headLast));
    }
}
