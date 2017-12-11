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
     * Adds new value to the beginning of the list
     *
     * @param value - value to be added to list
     * @return true (if the value was added, false - otherwise)
     */
    public boolean offer(final T value);

    /**
     * Removes a value from the tail of the list
     *
     * @return value - value to be removed from the list
     */
    public T poll();

    /**
     * Removes a value from the list
     *
     * @param value - value to be removed from the list
     * @return true (if the value was removed, false - otherwise)
     * @throws com.wildbeeslabs.jentle.collections.exception.EmptyListException
     */
    public boolean remove(final T value) throws EmptyListException;

    /**
     * Removes all values from the list
     */
    public void clear();

    /**
     * Checks if the list contains the value
     *
     * @param value - value to search for
     * @return boolean (true - if the list contains value, false - otherwise)
     */
    public boolean contains(final T value);

    /**
     * Gets the size of the list
     *
     * @return size of the list
     */
    public int size();

    /**
     * Checks if the list contains values
     *
     * @return boolean (true - if the list is empty, false - otherwise)
     */
    public boolean isEmpty();

    /**
     * Validates the list according to the invariants
     *
     * @return true (true - if the list is valid, false - otherwise)
     */
    public boolean validate();

    /**
     * Returns the list as a Java compatible Queue {@link java.util.Queue}
     *
     * @return Java compatible Queue {@link java.util.Queue}
     */
    public java.util.Queue<? extends T> toQueue();

    /**
     * Returns the list as a Java compatible Collection
     * {@link java.util.Collection}
     *
     * @return Java compatible Collection {@link java.util.Collection}
     */
    public java.util.Collection<? extends T> toCollection();

    /**
     * Adds new value to the list
     *
     * @param item - value to be added to the list
     */
    public void addLast(final T item);

    /**
     * Returns the value from the list by index
     *
     * @param index - node index
     * @return node value
     */
    public T getAt(int index);

    /**
     * Creates iterator instance for the list values
     *
     * @return iterator instance
     */
    public Iterator<? extends T> iterator();
}
