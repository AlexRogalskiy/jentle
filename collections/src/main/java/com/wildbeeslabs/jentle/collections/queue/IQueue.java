package com.wildbeeslabs.jentle.collections.queue;

import com.wildbeeslabs.jentle.collections.exception.EmptyQueueException;

/**
 *
 * Custom queue interface declaration
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface IQueue<T> {
    /**
     * Add a value to the beginning of the queue.
     * 
     * @param value to add to queue.
     * @return True if added to queue.
     */
    public boolean offer(T value);

    /**
     * Remove a value from the tail of the queue.
     * 
     * @return value from the tail of the queue.
     */
    public T poll();

    /**
     * Get but do not remove tail of the queue.
     * 
     * @return value from the tail of the queue.
     * @throws com.wildbeeslabs.jentle.collections.exception.EmptyQueueException
     */
    public T peek() throws EmptyQueueException;

    /**
     * Remove the value from the queue.
     * 
     * @param value to remove from the queue.
     * @return True if the value was removed from the queue.
     */
    public boolean remove(T value);

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
    public boolean contains(T value);

    /**
     * Get the size of the queue.
     * 
     * @return size of the queue.
     */
    public int size();

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
    public java.util.Queue<T> toQueue();

    /**
     * Get this Queue as a Java compatible Collection
     * 
     * @return Java compatible Collection
     */
    public java.util.Collection<T> toCollection();
}
