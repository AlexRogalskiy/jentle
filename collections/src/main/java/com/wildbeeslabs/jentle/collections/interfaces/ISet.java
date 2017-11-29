package com.wildbeeslabs.jentle.collections.interfaces;

import java.util.Iterator;

/**
 *
 * Custom set interface declaration
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public interface ISet<T> {

    public boolean has(final T item);

    public ISet<T> disjunct(final T item);

    public ISet<T> remove(final T item);

    public int size();

    public Iterator<? extends T> iterator();
}
