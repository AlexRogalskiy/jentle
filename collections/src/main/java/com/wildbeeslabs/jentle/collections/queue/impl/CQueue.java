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
package com.wildbeeslabs.jentle.collections.queue.impl;

import com.wildbeeslabs.jentle.collections.exception.EmptyQueueException;
import com.wildbeeslabs.jentle.collections.list.node.ACNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

/**
 * Custom queue implementation {@link ACQueue}
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CQueue<T> extends ACQueue<T> {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CQueueNode<T> extends ACNode<T> {

        private CQueueNode<T> next;

        public CQueueNode() {
            this(null);
        }

        public CQueueNode(final T data) {
            this(data, null);
        }

        public CQueueNode(final T data, final CQueueNode<T> next) {
            super(data);
            this.next = next;
        }
    }

    /**
     * {@link CQueue} head {@link CQueueNode}
     */
    protected CQueueNode<T> head;
    /**
     * {@link CQueue} tail {@link CQueueNode}
     */
    protected CQueueNode<T> tail;
    protected int size;

    /**
     * Default {@link CQueue} constructor
     */
    public CQueue() {
        this.head = this.tail = null;
        this.size = 0;
    }

    @Override
    public T head() throws EmptyQueueException {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.size));
        }
        return this.head.getData();
    }

    @Override
    public T tail() throws EmptyQueueException {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.size));
        }
        return this.tail.getData();
    }

    @Override
    public boolean offer(final T value) {
        final CQueueNode<T> temp = new CQueueNode<>(value);
        if (Objects.nonNull(this.tail)) {
            this.tail.setNext(temp);
        }
        this.tail = temp;
        if (Objects.isNull(this.head)) {
            this.head = this.tail;
        }
        this.size++;
        return true;
    }

    @Override
    public T poll() {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: %s (empty size=%i)", this.getClass().getName(), this.size()));
        }
        final T data = this.head.getData();
        this.head = this.head.getNext();
        if (Objects.isNull(head)) {
            this.tail = null;
        }
        this.size--;
        return data;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        @SuppressWarnings("UnusedAssignment")
        CQueue.CQueueNode<T> temp = null;
        while (Objects.nonNull(this.head)) {
            temp = this.head.getNext();
            this.head = null;
            this.head = temp;
        }
        this.tail = null;
        this.size = 0;
    }

    @Override
    public T peek() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Queue<T> toQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
