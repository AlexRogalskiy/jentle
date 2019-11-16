package com.wildbeeslabs.jentle.collections.set;

import com.google.common.collect.Iterators;

import javax.annotation.Nonnull;
import java.util.*;

public class ArraySortedSet<T> implements SortedSet<T> {
    @Nonnull
    private final Comparator<? super T> comparator;
    @Nonnull
    private final Object[] arr;

    private ArraySortedSet(@Nonnull Comparator<? super T> comparator, @Nonnull T[] arr) {
        // we assume arr is already sorted by comparator, and all entries are unique
        this.comparator = comparator;
        this.arr = arr;
    }

    public static <T> ArraySortedSet<T> of(@Nonnull Comparator<? super T> comparator, @Nonnull T[] arr) {
        return new ArraySortedSet<T>(comparator, arr);
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public boolean isEmpty() {
        return arr.length > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return Arrays.binarySearch((T[]) arr, (T) o, comparator) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return Iterators.forArray((T[]) arr);
    }

    @Override
    public Object[] toArray() {
        return arr.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length <= arr.length) {
            System.arraycopy(arr, 0, (Object[]) a, 0, arr.length);
            return a;
        }
        return Arrays.copyOf((T[]) arr, arr.length);
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T first() {
        if (arr.length == 0) {
            throw new NoSuchElementException();
        }
        return (T) arr[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public T last() {
        if (arr.length == 0) {
            throw new NoSuchElementException();
        }
        return (T) arr[arr.length - 1];
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Object o : arr) {
            result += o.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof SortedSet) {
            SortedSet other = (SortedSet) o;
            if (arr.length != other.size()) {
                return false;
            }
            return Iterators.elementsEqual(iterator(), other.iterator());
        }
        if (o instanceof Set) {
            Set other = (Set) o;
            if (arr.length != other.size()) {
                return false;
            }
            return this.containsAll(other);
        }
        return false;
    }
}
