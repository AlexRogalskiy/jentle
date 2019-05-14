package com.wildbeeslabs.jentle.collections.set.impl;

import com.wildbeeslabs.jentle.collections.set.wrapper.ByteArrayWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode
@ToString
public class CByteArraySet implements Set<ByteArrayWrapper> {

    private final LinkedHashSet<ByteArrayWrapper> delegate;

    public CByteArraySet() {
        this.delegate = new LinkedHashSet<>();
    }

    public CByteArraySet(final Collection<byte[]> values) {
        this();
        this.addAll(values);
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean contains(final Object o) {
        if (o instanceof byte[]) {
            return this.delegate.contains(ByteArrayWrapper.of((byte[]) o));
        }
        return this.delegate.contains(o);
    }

    public boolean add(final ByteArrayWrapper e) {
        return this.delegate.add(e);
    }

    public boolean add(final byte[] e) {
        return this.delegate.add(ByteArrayWrapper.of(e));
    }

    public boolean containsAll(final Collection<?> c) {
        return Optional.ofNullable(c).orElseGet(Collections::emptyList).stream().allMatch(i -> {
            if (i instanceof byte[]) {
                if (!this.contains(ByteArrayWrapper.of((byte[]) i))) {
                    return false;
                }
            } else {
                if (!this.contains(i)) {
                    return false;
                }
            }
            return true;
        });
    }

    public boolean addAll(final Collection<? extends ByteArrayWrapper> c) {
        return this.delegate.addAll(c);
    }

    public boolean addAll(final Iterable<byte[]> c) {
        Optional.ofNullable(c).orElseGet(Collections::emptyList).forEach(this::add);
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
            this.delegate.remove(ByteArrayWrapper.of((byte[]) o));
        }
        return this.delegate.remove(o);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        Optional.ofNullable(c).orElseGet(Collections::emptyList).forEach(this::remove);
        return true;
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    public Set<byte[]> asRawSet() {
        return this.delegate.stream().map(ByteArrayWrapper::getArray).collect(Collectors.toSet());
    }
}
