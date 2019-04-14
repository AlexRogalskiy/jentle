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

import com.wildbeeslabs.jentle.collections.exception.EmptyDequeException;
import com.wildbeeslabs.jentle.collections.list.node.ACListNodeExtended;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

/**
 * Custom deque implementation {@link ACDeque}
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CDeque<T> extends ACDeque<T> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
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

    /**
     * Default head {@link CDequeNode}
     */
    protected CDequeNode<T> head;
    /**
     * Default tail {@link CDequeNode}
     */
    protected CDequeNode<T> tail;
    /**
     * Default {@link CDeque} size
     */
    protected int size;

    /**
     * Default deque constructor
     */
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
        return this.size;
    }

    @Override
    public boolean offer(final T data) {
        this.addLast(data);
        return true;
    }

    @Override
    public T peekFirst() {
        try {
            return this.getFirst();
        } catch (EmptyDequeException ex) {
            log.error(String.format("ERROR: message={%s}", ex.getMessage()));
        }
        return null;
    }

    @Override
    public T peekLast() {
        try {
            return this.getLast();
        } catch (EmptyDequeException ex) {
            log.error(String.format("ERROR: message={%s}", ex.getMessage()));
        }
        return null;
    }

    @Override
    public T poll() {
        return this.removeFirst();
    }

    @Override
    public T peek() {
        if (this.isEmpty()) {
            throw new EmptyDequeException("ERROR: deque is empty");
        }
        return this.head.getData();
    }

    @Override
    public T head() throws EmptyDequeException {
        if (this.isEmpty()) {
            throw new EmptyDequeException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        return this.head.getData();
    }

    @Override
    public T tail() throws EmptyDequeException {
        if (this.isEmpty()) {
            throw new EmptyDequeException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        return this.tail.getData();
    }

    @Override
    public T getLast() {
        return this.tail();
    }

    @Override
    public T getFirst() {
        return this.head();
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

    /**
     * Deque {@link Iterator} implementation
     *
     * @param <T> type {@link CDeque} element
     */
    protected static class CDequeIterator<T> implements Iterator<T> {

        private CDeque.CDequeNode<? extends T> currentNode = null;

        public CDequeIterator(final CDeque<? extends T> source) {
            this.currentNode = source.head;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                return null;
            }
            final CDeque.CDequeNode<? extends T> node = this.currentNode;
            this.currentNode = node.getNext();
            return node.getData();
        }

        @Override
        public boolean hasNext() {
            return Objects.nonNull(this.currentNode);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public void addFirst(final T data) {
        if (Objects.isNull(this.head)) {
            this.head = new CDequeNode<>(data);
            if (Objects.isNull(this.tail)) {
                this.tail = this.head;
            }
        } else {
            final CDequeNode<T> oldFirst = this.head;
            this.head = new CDequeNode<>(data, oldFirst);
            oldFirst.setPrevious(this.head);
        }
        this.size++;
    }

    @Override
    public void addLast(final T data) {
        if (Objects.isNull(this.tail)) {
            this.tail = new CDequeNode<>(data);
            if (Objects.isNull(this.head)) {
                this.head = this.tail;
            }
        } else {
            final CDequeNode<T> oldLast = this.tail;
            this.tail = new CDequeNode<>(data, null, oldLast);
            oldLast.setNext(this.tail);
        }
        this.size++;
    }

    @Override
    public T removeFirst() throws EmptyDequeException {
        if (this.isEmpty()) {
            throw new EmptyDequeException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        final CDequeNode<T> oldFirst = this.head;
        this.head = oldFirst.getNext();

        if (Objects.nonNull(this.head)) {
            this.head.setPrevious(null);
        } else if (this.size == 1) {
            this.tail = null;
        } else {
            this.head = this.tail.getPrevious();
        }
        this.size--;
        return oldFirst.getData();
    }

    @Override
    public T removeLast() throws EmptyDequeException {
        if (this.isEmpty()) {
            throw new EmptyDequeException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        final CDequeNode<T> oldLast = this.tail;
        this.tail = this.tail.getPrevious();

        if (Objects.nonNull(this.tail)) {
            this.tail.setNext(null);
        } else if (this.size == 1) {
            this.head = null;
        } else {
            this.tail = this.head.getNext();
        }
        this.size--;
        return oldLast.getData();
    }
}
