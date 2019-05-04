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
package com.wildbeeslabs.jentle.collections.queue.iface;

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.exception.OverflowStackException;
import com.wildbeeslabs.jentle.collections.iface.collection.Collection;

/**
 * Custom stack {@link Collection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IStack<T> extends Collection<T> {

    /**
     * Adds value to the top of the current stack
     *
     * @param value element value to push on the stack.
     * @throws com.wildbeeslabs.jentle.collections.exception.OverflowStackException
     */
    void push(final T value) throws OverflowStackException;

    /**
     * Returns element value from the top of stack.
     *
     * @return element value from the top of the stack.
     * @throws com.wildbeeslabs.jentle.collections.exception.EmptyStackException
     */
    T pop() throws EmptyStackException;

    /**
     * Returns but do not remove element value from the top of the current the
     * stack.
     *
     * @return element value from the top of the stack.
     * @throws com.wildbeeslabs.jentle.collections.exception.EmptyStackException
     */
    T peek() throws EmptyStackException;

    /**
     * Removes element value from the current stack.
     *
     * @param value to remove from the current stack.
     * @return true if the value was removed, false - otherwise
     */
    boolean remove(final T value);

    /**
     * Clears the current stack.
     */
    void clear();

    /**
     * Checks if the current stack contains the value
     *
     * @param value object to find in stack.
     * @return true if the current stack contains the value, false - otherwise.
     */
    boolean contains(final T value);

    /**
     * Returns the size of the current stack.
     *
     * @return size of the current stack.
     */
    int size();

    /**
     * Validate the current stack according to the invariants.
     *
     * @return true if the current stack is valid, false - otherwise
     */
    boolean validate();

    /**
     * Converts the current stack to Java compatible Queue
     *
     * @return Java compatible Queue
     */
    java.util.Queue<? extends T> toLifoQueue();

    /**
     * Converts the current stack to Java compatible Collection
     *
     * @return Java compatible Collection
     */
    java.util.Collection<? extends T> toCollection();

    /**
     * Checks if the current stack is empty
     *
     * @return true - if the current stack is empty, false - otherwise
     */
    boolean isEmpty();
}
