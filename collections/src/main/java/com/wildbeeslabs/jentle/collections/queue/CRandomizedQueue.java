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
import edu.princeton.cs.introcs.StdRandom;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom randomized queue implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CRandomizedQueue<T> extends ACQueue<T> {

    private T[] queue;
    private int pointer = 0;
    private int size = 0;
    private int arraySize = 2;

    @Override
    public boolean offer(final T data) {
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

    protected static class QueueIterator<T> implements Iterator<T> {

        private final T[] iteratorQueue;
        private int iteratorPointer;

        public QueueIterator(final CRandomizedQueue<? extends T> source) {
            this.iteratorPointer = source.size() - 1;
            this.iteratorQueue = (T[]) source.toArray();
        }

        public T next() {
            if (!this.hasNext()) {
                return null;
            }
            int randomPointer = this.iteratorPointer;
            if (randomPointer > 0) {
                randomPointer = StdRandom.uniform(this.iteratorPointer);
            }
            final T data = this.iteratorQueue[randomPointer];
            this.iteratorPointer--;
            if (randomPointer != this.iteratorPointer) {
                this.iteratorQueue[randomPointer] = this.iteratorQueue[this.iteratorPointer];
            }
            return data;
        }

        public boolean hasNext() {
            return -1 <= this.iteratorPointer;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported");
        }
    }

    public CRandomizedQueue() {
        this.queue = (T[]) new Object[arraySize];
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    public int size() {
        return this.size;
    }

    public void enqueue(final T data) {
        if (this.pointer >= this.arraySize) {
            resizeArray((size + 1) * 2);
        }
        this.queue[this.pointer] = data;
        this.pointer++;
        this.size++;
    }

    public T dequeue() throws EmptyQueueException {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        int randomNumber = 0;
        if (this.size > 1) {
            randomNumber = StdRandom.uniform(size - 1);
        }
        final T returnItem = this.queue[randomNumber];
        this.pointer--;
        if (this.size != this.pointer) {
            this.queue[randomNumber] = this.queue[this.pointer];
        }
        if (this.size <= this.arraySize / 4) {
            this.resizeArray(this.arraySize / 2);
        }
        this.size--;
        return returnItem;
    }

    private void resizeArray(int newArraySize) {
        this.arraySize = newArraySize;
        final T[] tempArray = (T[]) new Object[this.arraySize];
        int tempPointer = 0;
        for (int i = 0; i < this.size; i++) {
            tempArray[i] = this.queue[i];
            tempPointer++;
        }
        this.queue = tempArray;
        this.pointer = tempPointer;
    }

    public T sample() throws EmptyQueueException {
        if (this.isEmpty()) {
            throw new EmptyQueueException(String.format("ERROR: %s (empty size=%d)", this.getClass().getName(), this.size()));
        }
        int randomNumber = 0;
        if (this.size > 1) {
            randomNumber = StdRandom.uniform(size - 1);
        }
        return this.queue[randomNumber];
    }

    @Override
    public Iterator<T> iterator() {
        return new CRandomizedQueue.QueueIterator<>(this);
    }
}
