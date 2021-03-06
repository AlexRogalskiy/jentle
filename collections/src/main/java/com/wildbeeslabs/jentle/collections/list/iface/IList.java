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
package com.wildbeeslabs.jentle.collections.list.iface;

import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;

/**
 * Custom {@link IListCollection} interface declaration
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IList<T, E extends ACListNode<T, E>> extends IListCollection<T, E> {

    /**
     * Adds new value to the beginning of the list
     *
     * @param value - value to be added to list
     * @return true (if the value was added, false - otherwise)
     */
//    boolean offer(final T value);
    /**
     * Removes a value from the tail of the list
     *
     * @return value - value to be removed from the list
     */
//    T poll();

    /**
     * Adds new value to the tail of the list
     *
     * @param item - value to be added to the list
     */
    void addLast(final T item);

    /**
     * Adds new value to the head of the list
     *
     * @param item - value to be added to the list
     */
    void addFirst(final T item);

    /**
     * Adds new value to particular position of the list
     *
     * @param item  - value to be added to the list
     * @param index - position of new node
     */
    void insertAt(final T item, int index);

    /**
     * Removes value from the tail of the list
     *
     * @return value removed from the tail of the list
     * @throws EmptyListException
     */
    T removeLast() throws EmptyListException;

    /**
     * Removes value from the head of the list
     *
     * @return value removed from the head of the list
     * @throws EmptyListException
     */
    T removeFirst() throws EmptyListException;
}
