package com.wildbeeslabs.jentle.algorithms.sequence;

/**
 * This interface indicates, that a class can create a copy of type {@code T}.
 * Typically, classes which implement this interface, are able to create a copy
 * from itself.
 *
 * @param <T> the type of the copied object
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 1.0
 * @since 1.0
 */
public interface Copyable<T> {

    /**
     * Return a new copy of type {@code T}.
     *
     * @return a new copy of type {@code T}.
     */
    public T copy();

}
