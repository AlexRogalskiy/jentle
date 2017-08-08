package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;
import com.wildbeeslabs.jentle.collections.exception.NoSpaceAvailableException;
import java.lang.reflect.Array;
import java.util.Comparator;

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

    private int size;
    private int capacity;
    private final Comparator<? super T> cmp;
    private T[] array;

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
        if(this.size >= this.capacity) {
            throw new NoSpaceAvailableException(String.format("ERROR: CHeap (no more space available size=%i, capacity=%i)", this.size(), this.capacity()));
        }
        int currentIndex = this.size++, parentIndex;
        while(currentIndex > 0 && this.cmp.compare(item, (this.array[parentIndex = currentIndex / 2])) < 0) {
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
}
