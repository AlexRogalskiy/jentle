package com.wildbeeslabs.jentle.collections.queue.impl;

import com.wildbeeslabs.jentle.collections.exception.PriorityQueueEmptyException;
import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
import com.wildbeeslabs.jentle.collections.queue.iface.IPriorityQueue;
import com.wildbeeslabs.jentle.collections.tree.iface.IHeapTree;
import com.wildbeeslabs.jentle.collections.tree.impl.CVectorHeapTree;
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
public class CHeapPriorityQueue<K, V, T extends CKeyValueNode<K, V>> implements IPriorityQueue<T> {

    private IHeapTree<T, CPositionalKeyValueTreeNode<K, V, T>> heapTree;

    private Comparator<? super T> comparator;

    public CHeapPriorityQueue(final Comparator<? super T> comparator) {
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
    public void insertItem(final T value) {
        CPositionalKeyValueTreeNode<K, V, T> node = this.heapTree.add(value);
        CPositionalKeyValueTreeNode<K, V, T> position;
        while (!this.heapTree.isRoot(node)) {
            position = this.heapTree.getParent(node);
            if (Objects.compare(position.element(), node.element(), this.comparator) <= 0) {
                break;
            }
            this.heapTree.swap(position, node);
            node = position;
        }
    }

    @Override
    public T minElement() {
        return null;
    }

    @Override
    public T removeMin() {
        if (this.isEmpty()) {
            throw new PriorityQueueEmptyException("ERROR: empty priority queue");
        }
        final CPositionalKeyValueTreeNode<K, V, T> min = this.heapTree.root();
        if (this.size() == 1) {
            this.heapTree.remove();
        } else {
            this.heapTree.replace(this.heapTree.root(), this.heapTree.remove());
            CPositionalKeyValueTreeNode<K, V, T> position = this.heapTree.root(), temp;
            while (this.heapTree.isInternal(this.heapTree.getLeft(position))) {
                if (this.heapTree.isExternal(this.heapTree.getRight(position)) || Objects.compare(this.heapTree.getLeft(position).element(), this.heapTree.getRight(position).element(), this.comparator) <= 0) {
                    temp = this.heapTree.getLeft(position);
                } else {
                    temp = this.heapTree.getRight(position);
                }
                if (Objects.compare(temp.element(), position.element(), this.comparator) < 0) {
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
    public <T> T[] toArray(final T[] array) {
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
    public boolean addAll(final Collection<? extends T> collection) {
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
    public boolean add(final T kvcKeyValueNode) {
        return false;
    }

    @Override
    public boolean offer(final T kvcKeyValueNode) {
        return false;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
