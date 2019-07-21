package com.wildbeeslabs.jentle.collections.queue.impl;

import com.wildbeeslabs.jentle.collections.iterator.QueueIterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public final class OneQueue<T> extends AtomicReference<T> implements Queue<T> {

    private static final long serialVersionUID = -16964295773058779L;

    @Override
    public boolean add(final T t) {
        while (!offer(t)) ;
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return false;
    }

    @Override
    public void clear() {
        set(null);
    }

    @Override
    public boolean contains(final Object o) {
        return Objects.equals(get(), o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return false;
    }

    @Override
    public T element() {
        return this.get();
    }

    @Override
    public boolean isEmpty() {
        return this.get() == null;
    }

    @Override
    public Iterator<T> iterator() {
        return new QueueIterator<>(this);
    }

    @Override
    public boolean offer(final T t) {
        if (get() != null) {
            return false;
        }
        lazySet(t);
        return true;
    }

    @Override
    public T peek() {
        return this.get();
    }

    @Override
    public T poll() {
        T v = get();
        if (v != null) {
            lazySet(null);
        }
        return v;
    }

    @Override
    public T remove() {
        return getAndSet(null);
    }

    @Override
    public boolean remove(final Object o) {
        return false;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return false;
    }

    @Override
    public int size() {
        return this.get() == null ? 0 : 1;
    }

    @Override
    public Object[] toArray() {
        T t = get();
        if (t == null) {
            return new Object[0];
        }
        return new Object[]{t};
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T1> T1[] toArray(final T1[] a) {
        int size = size();
        T1[] newArray = a;
        if (newArray.length < size) {
            newArray = (T1[]) java.lang.reflect.Array.newInstance(newArray.getClass().getComponentType(), size);
        }
        if (size == 1) {
            newArray[0] = (T1) get();
        }
        if (newArray.length > size) {
            newArray[size] = null;
        }
        return newArray;
    }
}
