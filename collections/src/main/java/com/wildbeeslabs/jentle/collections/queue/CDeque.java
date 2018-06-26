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
package com.wildbeeslabs.jentle.collections.queue;

import com.wildbeeslabs.jentle.collections.exception.EmptyQueueException;
import com.wildbeeslabs.jentle.collections.list.node.ACListNodeExtended;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom deque implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CDeque<T> extends ACQueue<T> {

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class CDequeNode<T> extends ACListNodeExtended<T, CDequeNode<T>> {

        public CDequeNode() {
            this(null);
        }

        public CDequeNode(final T data) {
            this(data, null);
        }

        public CDequeNode(final T data, final CDequeNode<T> next) {
            this(data, next, null);
        }

        public CDequeNode(final T data, final CDequeNode<T> next, final CDequeNode<T> previous) {
            super(data, next, previous);
        }
    }

    protected CDequeNode<T> head;
    protected CDequeNode<T> tail;
    protected int size;

    public CDeque() {
        this.head = this.tail = null;
        this.size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new CDeque.CDequeIterator<>(this);
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean offer(T e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T poll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T peek() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T head() throws EmptyQueueException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T tail() throws EmptyQueueException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Queue<? extends T> toQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<? extends T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected static class CDequeIterator<T> implements Iterator<T> {

        private CDeque.CDequeNode<? extends T> currentNode = null;

        public CDequeIterator(final CDeque<? extends T> source) {
            this.currentNode = source.head;
        }

        public T next() {
            if (!this.hasNext()) {
                return null;
            }
            final CDeque.CDequeNode<? extends T> node = this.currentNode;
            this.currentNode = node.getNext();
            return node.getData();
        }

        public boolean hasNext() {
            return Objects.nonNull(this.currentNode);
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }

    public boolean isEmpty() {
        return (this.size() == 0);
    }

    public void addFirst(Item item) {
        validateItem(item);

        if (first == null) {
            first = new Node();
            first.item = item;

            if (last == null) {
                last = first;
            }

        } else {
            Node oldFirst = first;

            first = new Node();
            first.next = oldFirst;
            first.item = item;

            oldFirst.previous = first;
        }

        size++;
    }

    public void addLast(Item item) {
        validateItem(item);

        if (last == null) {
            last = new Node();
            last.item = item;

            if (first == null) {
                first = last;
            }

        } else {
            Node oldLast = last;

            last = new Node();
            last.previous = oldLast;
            last.item = item;

            oldLast.next = last;
        }

        size++;
    }

    private void validateItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
    }

    public Item removeFirst() {
        checkNotEmpty();

        Node oldFirst = first;
        first = oldFirst.next;

        if (first != null) {
            first.previous = null;
        } else if (size == 1) {
            last = null;
        } else {
            first = last.previous;
        }

        size--;

        return oldFirst.item;
    }

    public Item removeLast() {
        checkNotEmpty();

        Node oldLast = last;
        last = last.previous;

        if (last != null) {
            last.next = null;
        } else if (size == 1) {
            first = null;
        } else {
            last = first.next;
        }

        size--;

        return oldLast.item;
    }

    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("The queue is empty");
        }
    }
}
