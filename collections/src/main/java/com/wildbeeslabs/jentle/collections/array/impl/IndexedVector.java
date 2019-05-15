package com.wildbeeslabs.jentle.collections.array.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Vector;

/**
 * Custom indexed vector implementation
 *
 * @param <K> type of indexed key
 * @param <T> type of indexed value
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode
@ToString
public class IndexedVector<K, T> {

    /**
     * Default indexed vector size
     */
    public static final int DEFAULT_VECTOR_SIZE = 10;

    protected Vector<T> elements;
    protected Hashtable<K, T> index;

    public IndexedVector() {
        this(DEFAULT_VECTOR_SIZE);
    }

    public IndexedVector(int var1) {
        this.elements = new Vector(var1);
        this.index = new Hashtable(var1);
    }

    public synchronized void appendElement(final K key, final T value) {
        this.elements.add(value);
        this.index.put(key, value);
    }

    public T elementAt(int var1) {
        return this.elements.elementAt(var1);
    }

    public Enumeration<T> elements() {
        return this.elements.elements();
    }

    public T getElement(final K var1) {
        return this.index.get(var1);
    }

    public synchronized boolean removeElement(final K key) {
        final T value = this.index.get(key);
        if (Objects.isNull(value)) {
            return false;
        }
        this.index.remove(key);
        return this.elements.removeElement(value);
    }

    public int size() {
        return this.elements.size();
    }
}
