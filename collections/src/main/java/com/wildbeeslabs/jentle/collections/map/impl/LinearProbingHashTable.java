package com.wildbeeslabs.jentle.collections.map.impl;

import com.wildbeeslabs.jentle.collections.exception.HashTableFullException;
import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Custom hashtable linear probing {@link Dictionary} implementation
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LinearProbingHashTable<K, V> extends Dictionary {

    private final CKeyValueNode<K, V> AVAILABLE = new CKeyValueNode(null, null);
    private int size = 0;
    private int capacity;
    private CKeyValueNode<K, V>[] array;
    private Comparator<? super K> comparator;

    public LinearProbingHashTable(final Comparator<? super K> comparator) {
        this(comparator, 1023);
    }

    public LinearProbingHashTable(final Comparator<? super K> comparator, final int size) {
        this.comparator = comparator;
        this.size = size;
        this.array = new CKeyValueNode[this.size];
    }

    private boolean available(final int index) {
        checkRange(index);
        return (this.array[index] == AVAILABLE);
    }

    private boolean empty(final int index) {
        checkRange(index);
        return Objects.isNull(this.array[index]);
    }

    private K key(final int index) {
        checkRange(index);
        return this.array[index].getKey();
    }

    private V element(final int index) {
        checkRange(index);
        return this.array[index].getValue();
    }

    private int findItem(final K key) {
        int i = Objects.hash(key) % this.capacity;
        int j = i;
        do {
            if (empty(i)) {
                return -1;
            }
            if (available(i)) {
                i = (i + 1) % this.capacity;
            } else if (Objects.compare(key(i), key, this.comparator) == 0) {
                return i;
            } else {
                i = (i + 1) % this.capacity;
            }
        } while (i != j);
        return -1;
    }

    public Object findElement(final K key) {
        final int i = this.findItem(key);
        if (i < 0) {
            return null;
        }
        return this.element(i);
    }

    public void insertItem(final K key, final V value) {
        int i = Objects.hash(key) % this.capacity;
        int j = i;
        do {
            if (empty(i) | available(i)) {
                this.array[i] = new CKeyValueNode(key, value);
                this.size++;
                return;
            }
            i = (i + 1 % this.capacity);
        } while (i != j);
        throw new HashTableFullException("ERROR: hashtable is full");
    }

    public V removeElement(final K key) {
        final int i = this.findItem(key);
        if (i < 0) return null;
        final V result = this.element(i);
        this.array[i] = AVAILABLE;
        this.size--;
        return result;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public Enumeration keys() {
        return null;
    }

    @Override
    public Enumeration elements() {
        return null;
    }

    @Override
    public Object get(final Object key) {
        return null;
    }

    @Override
    public Object put(final Object key, final Object value) {
        return null;
    }

    @Override
    public Object remove(final Object key) {
        return null;
    }

    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (index=%d is out of bounds [0, %d])", this.getClass().getName(), index, this.size() - 1));
        }
    }
}
