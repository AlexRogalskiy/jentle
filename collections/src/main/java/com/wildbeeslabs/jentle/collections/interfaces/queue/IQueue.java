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
package com.wildbeeslabs.jentle.collections.interfaces.queue;

import com.wildbeeslabs.jentle.collections.exception.EmptyQueueException;
import com.wildbeeslabs.jentle.collections.interfaces.service.IBase;

import java.util.Queue;

/**
 *
 * Custom queue interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface IQueue<T> extends IBase<T>, Queue<T> {

//    /**
//     * Add a value to the beginning of the queue.
//     *
//     * @param value to add to queue.
//     * @return True if added to queue.
//     */
//    public boolean offer(final T value);
//
//    /**
//     * Remove a value from the tail of the queue.
//     *
//     * @return value from the tail of the queue.
//     */
//    public T poll();
//
    /**
     * Adds value to the tail of the current queue
     *
     * @param value - element value to enqueue to the queue.
     */
    //boolean offer(final T value);
    /**
     * Removes element from the head of the current queue
     *
     * @return element value to dequeue from the queue.
     */
    //T poll();
    /**
     * Returns but do not remove element value from the head of the current
     * queue
     *
     * @return element value from the head of the current queue.
     * @throws EmptyQueueException
     */
    T head() throws EmptyQueueException;

    /**
     * Returns but do not remove element value from the tail of the current
     * queue
     *
     * @return element value from the tail of the current queue.
     * @throws EmptyQueueException
     */
    T tail() throws EmptyQueueException;

    /**
     * Removes value from the current queue.
     *
     * @param value to remove from the current queue.
     * @return true if element was removed, false - otherwise
     */
    //boolean remove(final Object value);
    /**
     * Clears the current queue.
     */
    //void clear();
    /**
     * Checks if the current queue contains the value.
     *
     * @param value to find in the queue.
     * @return true if the current queue contains the value, false - otherwise
     */
    //boolean contains(final Object value);
    /**
     * Returns the size of the current queue.
     *
     * @return size of the current queue.
     */
    //int size();
    /**
     * Validates the current queue according to the invariants.
     *
     * @return true if the current queue is valid, false - otherwise
     */
    boolean validate();

    /**
     * Converts the current queue to Java compatible Queue
     *
     * @return Java compatible Queue
     */
    java.util.Queue<? extends T> toQueue();

    /**
     * Converts the current queue to Java compatible Collection
     *
     * @return Java compatible Collection
     */
    java.util.Collection<? extends T> toCollection();

    /**
     * Checks if the current queue is empty
     *
     * @return true - if the current queue is empty, false - otherwise
     */
    //boolean isEmpty();
}
