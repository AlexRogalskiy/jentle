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

package com.wildbeeslabs.jentle.collections.interfaces;

import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import java.util.Iterator;

/**
 *
 * Custom list interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface IList<T> {

    /**
     * Add a value to the beginning of the queue.
     *
     * @param value to add to queue.
     * @return True if added to queue.
     */
    public boolean offer(final T value);

    /**
     * Remove a value from the tail of the queue.
     *
     * @return value from the tail of the queue.
     */
    public T poll();

    /**
     * Remove the value from the queue.
     *
     * @param value to remove from the queue.
     * @return True if the value was removed from the queue.
     * @throws com.wildbeeslabs.jentle.collections.exception.EmptyListException
     */
    public boolean remove(final T value) throws EmptyListException;

    /**
     * Clear the entire queue.
     */
    public void clear();

    /**
     * Does the queue contain the value.
     *
     * @param value to find in the queue.
     * @return True if the queue contains the value.
     */
    public boolean contains(final T value);

    /**
     * Get the size of the list.
     *
     * @return size of the list.
     */
    public int size();

    /**
     * Check if the list contains values.
     *
     * @return boolean (true - if the list is empty, false - otherwise)
     */
    public boolean isEmpty();

    /**
     * Validate the queue according to the invariants.
     *
     * @return True if the queue is valid.
     */
    public boolean validate();

    /**
     * Get this Queue as a Java compatible Queue
     *
     * @return Java compatible Queue
     */
    public java.util.Queue<? extends T> toQueue();

    /**
     * Get this Queue as a Java compatible Collection
     *
     * @return Java compatible Collection
     */
    public java.util.Collection<? extends T> toCollection();
    
    public void addLast(final T item);
    
    public T getAt(int index);
    
    public Iterator<? extends T> iterator();
}
