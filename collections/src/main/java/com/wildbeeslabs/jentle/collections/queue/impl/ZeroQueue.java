package com.wildbeeslabs.jentle.collections.queue.impl;

import java.io.Serializable;
import java.util.Queue;
import java.util.*;

public final class ZeroQueue<T> implements Queue<T>, Serializable {

    private static final long serialVersionUID = -71035316682360404L;

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public void clear() {
        //NO-OP
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public T element() {
        throw new NoSuchElementException("immutable empty queue");
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public boolean offer(T t) {
        return false;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T remove() {
        throw new NoSuchElementException("immutable empty queue");
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(final T1[] a) {
        if (a.length > 0) {
            a[0] = null;
        }
        return a;
    }
}
