package com.wildbeeslabs.jentle.collections.queue.impl;

import com.wildbeeslabs.jentle.collections.exception.EmptyContainerException;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.list.impl.CPositionalList;
import com.wildbeeslabs.jentle.collections.queue.iface.IPriorityQueue;
import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

/**
 * Custom priority {@link AbstractQueue} implementation
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
public class CPriorityQueue<K, V> extends AbstractQueue<CKeyValueNode<K, V>> implements IPriorityQueue<CKeyValueNode<K, V>> {

    private final CPositionalList<CKeyValueNode<K, V>> list;
    private final Comparator<? super CKeyValueNode<K, V>> comparator;

    public CPriorityQueue(final Comparator<? super CKeyValueNode<K, V>> comparator) {
        this.list = new CPositionalList<>();
        this.comparator = comparator;
    }

    protected CKeyValueNode<K, V> element(final Position<CKeyValueNode<K, V>> position) {
        return position.element();
    }

    protected K key(final Position<CKeyValueNode<K, V>> position) {
        return Optional.ofNullable(element(position))
            .map(elem -> element().getKey())
            .orElseGet(null);
    }

    protected V value(final Position<CKeyValueNode<K, V>> position) {
        return Optional.ofNullable(element(position))
            .map(elem -> elem.getValue())
            .orElseGet(null);
    }

    @Override
    public void insertItem(final CKeyValueNode<K, V> value) {
        final int comp = Objects.compare(value, this.list.last().element(), this.comparator);
        if (this.isEmpty()) {
            this.list.insertFirst(value);
        } else if (comp >= 0) {
            this.list.insertAfter(this.list.last(), value);
        } else {
            Position<CKeyValueNode<K, V>> position = this.list.first();
            while (Objects.compare(value, position.element(), this.comparator) >= 0) {
                position = this.list.after(position);
            }
            this.list.insertBefore(position, value);
        }
    }

    @Override
    public CKeyValueNode<K, V> minElement() {
        return null;
    }

    @Override
    public CKeyValueNode<K, V> removeMin() {
        if (this.list.isEmpty()) {
            throw new EmptyContainerException("ERROR: priority queue is empty");
        }
        return this.list.remove(this.list.first());
    }

    @Override
    public Iterator<CKeyValueNode<K, V>> iterator() {
        return null;
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean offer(final CKeyValueNode<K, V> kvcKeyValueNode) {
        return false;
    }

    @Override
    public CKeyValueNode<K, V> poll() {
        return null;
    }

    @Override
    public CKeyValueNode<K, V> peek() {
        return null;
    }
}
