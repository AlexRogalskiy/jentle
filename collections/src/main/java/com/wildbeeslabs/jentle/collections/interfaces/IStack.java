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

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;

/**
 *
 * Custom stack interface declaration
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface IStack<T> {
    /**
     * Push value on top of stack
     * 
     * @param value to push on the stack.
     */
    public void push(final T value);

    /**
     * Pop the value from the top of stack.
     * 
     * @return value popped off the top of the stack.
     * @throws com.wildbeeslabs.jentle.collections.exception.EmptyStackException
     */
    public T pop() throws EmptyStackException;

    /**
     * Peek the value from the top of stack.
     * 
     * @return value popped off the top of the stack.
     * @throws com.wildbeeslabs.jentle.collections.exception.EmptyStackException
     */
    public T peek() throws EmptyStackException;

    /**
     * Remove value from stack.
     * 
     * @param value to remove from stack.
     * @return True if value was removed.
     */
    public boolean remove(final T value);

    /**
     * Clear the entire stack.
     */
    public void clear();

    /**
     * Does stack contain object.
     * 
     * @param value object to find in stack.
     * @return True is stack contains object.
     */
    public boolean contains(final T value);

    /**
     * Size of the stack.
     * 
     * @return size of the stack.
     */
    public int size();

    /**
     * Validate the stack according to the invariants.
     * 
     * @return True if the stack is valid.
     */
    public boolean validate();

    /**
     * Get this Stack as a Java compatible Queue
     * 
     * @return Java compatible Queue
     */
    public java.util.Queue<? extends T> toLifoQueue();

    /**
     * Get this Stack as a Java compatible Collection
     * 
     * @return Java compatible Collection
     */
    public java.util.Collection<? extends T> toCollection();
}
