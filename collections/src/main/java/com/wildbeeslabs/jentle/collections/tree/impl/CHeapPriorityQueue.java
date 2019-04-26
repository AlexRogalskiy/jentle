package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.exception.PriorityQueueEmptyException;
import com.wildbeeslabs.jentle.collections.queue.iface.IPriorityQueue;
import com.wildbeeslabs.jentle.collections.tree.iface.IHeapTree;
import com.wildbeeslabs.jentle.collections.tree.node.CKeyValueNode;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalKeyValueTreeNode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

/**
 * Custom {@link PriorityQueue} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@EqualsAndHashCode
@ToString
public class CHeapPriorityQueue<K, V> implements IPriorityQueue<CPositionalKeyValueTreeNode<K, V>> {

    private IHeapTree<CKeyValueNode<K, V>, CPositionalKeyValueTreeNode<K, V>> heapTree;

    private Comparator<? extends CPositionalKeyValueTreeNode<K, V>> comparator;

    public CHeapPriorityQueue(final Comparator<? extends CPositionalKeyValueTreeNode<K, V>> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
        this.heapTree = new CVectorHeapTree();
    }

    @Override
    public int size() {
        return (this.heapTree.size() - 1) / 2;
    }

    public boolean isEmpty() {
        return (1 == this.size());
    }

    @Override
    public boolean contains(final Object o) {
        return false;
    }

    @Override
    public void insertItem(final CPositionalKeyValueTreeNode<K, V> value) {
    }

    @Override
    public CPositionalKeyValueTreeNode<K, V> minElement() {
        return null;
    }

    public V minValue() throws PriorityQueueEmptyException {
        if (this.isEmpty()) {
            throw new PriorityQueueEmptyException("ERROR: priority queue is empty");
        }
        return this.heapTree.root().value();
    }

    public K minKey() throws PriorityQueueEmptyException {
        if (this.isEmpty()) {
            throw new PriorityQueueEmptyException("ERROR: priority queue is empty");
        }
        return this.heapTree.root().key();
    }

    @Override
    public CPositionalKeyValueTreeNode<K, V> removeMin() {
        return null;
    }

    @Override
    public Iterator<CPositionalKeyValueTreeNode<K, V>> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return null;
    }

    @Override
    public boolean add(final CPositionalKeyValueTreeNode<K, V> value) {
        return false;
    }

    @Override
    public boolean remove(final Object value) {
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(final Collection<? extends CPositionalKeyValueTreeNode<K, V>> collection) {
        return false;
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(final CPositionalKeyValueTreeNode<K, V> value) {
        return false;
    }

    @Override
    public CPositionalKeyValueTreeNode<K, V> remove() {
        return null;
    }

    @Override
    public CPositionalKeyValueTreeNode<K, V> poll() {
        return null;
    }

    @Override
    public CPositionalKeyValueTreeNode<K, V> element() {
        return null;
    }

    @Override
    public CPositionalKeyValueTreeNode<K, V> peek() {
        return null;
    }
}
