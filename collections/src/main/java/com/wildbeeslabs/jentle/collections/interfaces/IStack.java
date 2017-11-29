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
