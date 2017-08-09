package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.exception.NoSpaceAvailableException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom heap implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CHeap<T> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    protected int size;
    protected int capacity;
    protected final Comparator<? super T> cmp;
    protected T[] array;

    public CHeap(final Class<T[]> clazz, int capacity) {
        this(clazz, capacity, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CHeap(final Class<T[]> clazz, int capacity, final Comparator<? super T> cmp) {
        this.size = 0;
        this.capacity = capacity;
        this.cmp = cmp;
        this.array = this.newArray(clazz, this.capacity);
    }

    public void insert(final T item) throws NoSpaceAvailableException {
        if (this.size >= this.capacity) {
            throw new NoSpaceAvailableException(String.format("ERROR: CHeap (no more space available size=%i, capacity=%i)", this.size(), this.capacity()));
        }
        int currentIndex = this.size++, parentIndex;
        while (currentIndex > 0 && this.cmp.compare(item, (this.array[parentIndex = currentIndex / 2])) < 0) {
            this.array[currentIndex] = this.array[parentIndex];
            currentIndex = parentIndex;
        }
        this.array[currentIndex] = item;
    }

    //@Override
    public void clear() {
        this.size = 0;
        this.array = this.newArray((Class<T[]>) this.array.getClass(), this.capacity());
    }

    public int size() {
        return this.size;
    }

    //@Override
    public int capacity() {
        return this.capacity;
    }

    public boolean isEmpty() {
        return (0 == this.size());
    }

    private T[] newArray(Class<T[]> type, int size) {
        return type.cast(Array.newInstance(type.getComponentType(), size));
    }

    @Override
    public String toString() {
        return String.format("CHeap {data: %s, size: %i, capacity: %i}", this.array, this.size, this.capacity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CHeap<T> other = (CHeap<T>) obj;
        if (this.size != other.size) {
            return false;
        }
        if (this.capacity != other.capacity) {
            return false;
        }
        if (!Arrays.deepEquals(this.array, other.array)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.size;
        hash = 79 * hash + this.capacity;
        hash = 79 * hash + Arrays.deepHashCode(this.array);
        return hash;
    }
}
