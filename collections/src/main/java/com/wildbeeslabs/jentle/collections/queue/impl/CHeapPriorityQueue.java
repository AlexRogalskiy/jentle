package com.wildbeeslabs.jentle.collections.queue.impl;

import com.wildbeeslabs.jentle.collections.exception.PriorityQueueEmptyException;
import com.wildbeeslabs.jentle.collections.queue.iface.IPriorityQueue;
import com.wildbeeslabs.jentle.collections.tree.iface.IHeapTree;
import com.wildbeeslabs.jentle.collections.tree.impl.CVectorHeapTree;
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
public class CHeapPriorityQueue<K, V> implements IPriorityQueue<CKeyValueNode<K, V>> {

    private IHeapTree<CKeyValueNode<K, V>, CPositionalKeyValueTreeNode<K, V>> heapTree;

    private Comparator<? super CPositionalKeyValueTreeNode<K, V>> comparator;

    public CHeapPriorityQueue(final Comparator<? super CPositionalKeyValueTreeNode<K, V>> comparator) {
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
    public void insertItem(final CKeyValueNode<K, V> value) {
        CPositionalKeyValueTreeNode<K, V> node = this.heapTree.add(value);
        CPositionalKeyValueTreeNode<K, V> position;
        while (!this.heapTree.isRoot(node)) {
            position = this.heapTree.getParent(node);
            if (Objects.compare(position, node, this.comparator) <= 0) {
                break;
            }
            this.heapTree.swap(position, node);
            node = position;
        }
    }

    @Override
    public CKeyValueNode<K, V> minElement() {
        return null;
    }

    @Override
    public CKeyValueNode<K, V> removeMin() {
        if (this.isEmpty()) {
            throw new PriorityQueueEmptyException("ERROR: empty priority queue");
        }
        final CPositionalKeyValueTreeNode<K, V> min = this.heapTree.root();
        if (this.size() == 1) {
            this.heapTree.remove();
        } else {
            this.heapTree.replace(this.heapTree.root(), this.heapTree.remove());
            CPositionalKeyValueTreeNode<K, V> position = this.heapTree.root(), temp;
            while (this.heapTree.isInternal(this.heapTree.getLeft(position))) {
                if (this.heapTree.isExternal(this.heapTree.getRight(position)) || Objects.compare(this.heapTree.getLeft(position), this.heapTree.getRight(position), this.comparator) <= 0) {
                    temp = this.heapTree.getLeft(position);
                } else {
                    temp = this.heapTree.getRight(position);
                }
                if (Objects.compare(temp, position, this.comparator) < 0) {
                    this.heapTree.swap(position, temp);
                    position = temp;
                } else {
                    break;
                }
            }
        }
        return min.element();
    }

    @Override
    public boolean contains(final Object o) {
        return false;
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
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return null;
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
    public boolean addAll(final Collection<? extends CKeyValueNode<K, V>> c) {
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
    public boolean add(final CKeyValueNode<K, V> kvcKeyValueNode) {
        return false;
    }

    @Override
    public boolean offer(final CKeyValueNode<K, V> kvcKeyValueNode) {
        return false;
    }

    @Override
    public CKeyValueNode<K, V> remove() {
        return null;
    }

    @Override
    public CKeyValueNode<K, V> poll() {
        return null;
    }

    @Override
    public CKeyValueNode<K, V> element() {
        return null;
    }

    @Override
    public CKeyValueNode<K, V> peek() {
        return null;
    }

    @Override
    public Iterator<CKeyValueNode<K, V>> iterator() {
        return null;
    }
}
