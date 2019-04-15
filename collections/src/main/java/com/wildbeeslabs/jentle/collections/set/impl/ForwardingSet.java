package com.wildbeeslabs.jentle.collections.set.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class ForwardingSet<E> implements Set<E> {

    private final Set<E> s;

    public ForwardingSet(final Set<E> s) {
        this.s = s;
    }

    public void clear() {
        s.clear();
    }

    public boolean contains(final Object o) {
        return s.contains(o);
    }

    public boolean isEmpty() {
        return s.isEmpty();
    }

    public int size() {
        return s.size();
    }

    public Iterator<E> iterator() {
        return s.iterator();
    }

    public boolean add(E e) {
        return s.add(e);
    }

    public boolean remove(final Object o) {
        return s.remove(o);
    }

    public boolean containsAll(final Collection<?> c) {
        return s.containsAll(c);
    }

    public boolean addAll(final Collection<? extends E> c) {
        return s.addAll(c);
    }

    public boolean removeAll(final Collection<?> c) {
        return s.removeAll(c);
    }

    public boolean retainAll(final Collection<?> c) {
        return s.retainAll(c);
    }

    public Object[] toArray() {
        return s.toArray();
    }

    public <T> T[] toArray(final T[] a) {
        return s.toArray(a);
    }
}
