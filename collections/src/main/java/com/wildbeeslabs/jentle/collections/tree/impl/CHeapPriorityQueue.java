package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.exception.PriorityQueueEmptyException;
import com.wildbeeslabs.jentle.collections.queue.iface.IPriorityQueue;
import com.wildbeeslabs.jentle.collections.tree.iface.IHeapTree;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalTreeNode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.PriorityQueue;

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
public class CHeapPriorityQueue<T> implements IPriorityQueue<K, T> {

    private IHeapTree<T, CPositionalTreeNode<T>> heapTree;

    private Comparator<? extends T> comparator;

    public CHeapPriorityQueue(final Comparator<? extends T> comparator) {
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
    public void insertItem(Object key, Object value) {

    }

    public T minElement() throws PriorityQueueEmptyException {
        if (this.isEmpty()) {
            throw new PriorityQueueEmptyException("ERROR: priority queue is empty");
        }
        return this.element(this.heapTree.root());
    }

    public T minKey() throws PriorityQueueEmptyException {
        if (this.isEmpty()) {
            throw new PriorityQueueEmptyException("ERROR: priority queue is empty");
        }
        return this.key(this.heapTree.root());
    }

    @Override
    public Object removeMin() {
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
