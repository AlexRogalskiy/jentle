package com.wildbeeslabs.jentle.collections.set.impl;

import com.wildbeeslabs.jentle.collections.set.wrapper.ByteArrayWrapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ByteArraySet implements Set<ByteArrayWrapper> {
    private LinkedHashSet<ByteArrayWrapper> delegate;

    public ByteArraySet() {
        this.delegate = new LinkedHashSet<>();
    }

    public ByteArraySet(final Collection<byte[]> values) {
        this();
        this.addAll(values);
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean contains(final Object o) {
        if (o instanceof byte[]) {
            return this.delegate.contains(new ByteArrayWrapper((byte[]) o));
        }
        return this.delegate.contains(o);
    }

    public boolean add(final ByteArrayWrapper e) {
        return this.delegate.add(e);
    }

    public boolean add(final byte[] e) {
        return this.delegate.add(new ByteArrayWrapper(e));
    }

    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (o instanceof byte[]) {
                if (!contains(new ByteArrayWrapper((byte[]) o))) {
                    return false;
                }
            } else {
                if (!contains(o)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean addAll(final Collection<? extends ByteArrayWrapper> c) {
        return delegate.addAll(c);
    }

    public boolean addAll(final Iterable<byte[]> c) {
        for (final byte[] o : c) {
            add(o);
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public Iterator<ByteArrayWrapper> iterator() {
        return this.delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.delegate.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return this.delegate.toArray(a);
    }

    @Override
    public boolean remove(final Object o) {
        if (o instanceof byte[]) {
            this.delegate.remove(new ByteArrayWrapper((byte[]) o));
        }
        return this.delegate.remove(o);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        for (final Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    public Set<byte[]> asRawSet() {
        final Set<byte[]> result = new LinkedHashSet<>();
        for (final ByteArrayWrapper wrapper : this.delegate) {
            result.add(wrapper.getArray());
        }
        return result;
    }
}
