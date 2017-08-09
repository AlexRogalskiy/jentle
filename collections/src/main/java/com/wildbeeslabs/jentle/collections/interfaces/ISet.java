package com.wildbeeslabs.jentle.collections.interfaces;

/**
 *
 * Custom set interface declaration
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface ISet<T> {
    public boolean has(T item);

    public ISet<T> disjunct(T item);

    public ISet<T> remove(T b);
    
    public int size();
}
