package com.wildbeeslabs.jentle.collections.list.iface;

/**
 * Interface for abstract lists
 *
 * @param <T>
 * @author Tomek
 */
public interface List<T> {
    long count();

    T get(long index);

    T first();

    T last();

    boolean includes(T item);

    void append(T item);

    void prepend(T item);

    void remove(T item);

    void removeLast();

    void removeFirst();

    void removeAll();

    T top();

    void push(T item);

    T pop();
}
