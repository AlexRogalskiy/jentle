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
package com.wildbeeslabs.jentle.collections.queue;

import com.wildbeeslabs.jentle.collections.exception.EmptyQueueException;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom bound queue implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CBoundQueue<T> extends ACQueue<T> {

    protected T[] queue;
    private int size;
    private int head;
    private int tail;

    public CBoundQueue(int maxSize, final Class<? extends T[]> clazz) {
        assert (size > 0);
        this.queue = CUtils.newArray(clazz, maxSize);
        this.head = 0;
        this.tail = maxSize - 1;
        this.size = 0;
    }

    @Override
    public boolean offer(final T value) {
        if (this.isFull()) {
            throw new IllegalStateException(String.format("ERROR: %s (full size=%d)", this.getClass().getName(), this.size()));
        }
        if (++this.tail == this.getSize()) {
            this.tail = 0;
        }
        this.queue[this.tail] = value;
        this.size++;
        return true;
    }

    @Override
    public T poll() {
        if (this.isEmpty()) {
            return null;//throw new EmptyQueueException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        final T removed = this.queue[this.head];
        this.queue[this.head] = null;
        if (++this.head == this.getSize()) {
            this.head = 0;
        }
        this.size--;
        return removed;
    }

    @Override
    public T head() throws EmptyQueueException {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        return this.queue[this.head];
    }

    @Override
    public T tail() throws EmptyQueueException {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        return this.queue[this.tail];
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSize(); i++) {
            this.queue[i] = null;
        }
        this.head = 0;
        this.tail = this.queue.length - 1;
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean validate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Queue<? extends T> toQueue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<? extends T> toCollection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFull() {
        return (this.size() == this.queue.length);
    }

    @Override
    public T peek() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<T> iterator() {
        return new CBoundQueue.CBoundQueueIterator<>(this);
    }

    protected static class CBoundQueueIterator<T> implements Iterator<T> {

        private final CBoundQueue<? extends T> source;
        private int cursor;

        public CBoundQueueIterator(final CBoundQueue<? extends T> source) {
            this.source = source;
            this.cursor = 0;
        }

        public boolean hasNext() {
            return this.cursor < this.source.size;
        }

        public T next() {
            int index = (this.source.head + this.cursor);// % this.source.queue.length;
            final T r = (T) this.source.queue[index];
            this.cursor++;
            return r;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
