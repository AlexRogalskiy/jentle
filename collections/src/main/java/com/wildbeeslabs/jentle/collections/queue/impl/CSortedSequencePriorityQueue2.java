package com.wildbeeslabs.jentle.collections.queue.impl;

import com.wildbeeslabs.jentle.collections.exception.PriorityQueueEmptyException;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.list.node.CLocatorListNodeExtended;
import com.wildbeeslabs.jentle.collections.model.impl.CPositionalKeyValueNode;
import com.wildbeeslabs.jentle.collections.model.interfaces.Locator;
import com.wildbeeslabs.jentle.collections.queue.iface.IPriorityQueue2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.Objects;

/**
 * Custom sorted sequence {@link IPriorityQueue2} implementation (version 2)
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
public class CSortedSequencePriorityQueue2<K, V> extends CSortedSequencePriorityQueue<K, V, CPositionalKeyValueNode<K, V>, CLocatorListNodeExtended<K, V>> implements IPriorityQueue2<CLocatorListNodeExtended<K, V>> {

    public CSortedSequencePriorityQueue2(final Comparator<CPositionalKeyValueNode<K, V>> comparator) {
        super(comparator);
    }

    protected Locator<CPositionalKeyValueNode<K, V>> locatorInsert(final CPositionalKeyValueNode<K, V> locItem) {
        Position<CPositionalKeyValueNode<K, V>> position, current;
        if (this.isEmpty()) {
            position = this.getList().insertFirst(locItem);
        } else if (Objects.compare(locItem, this.getList().last().element(), this.getComparator()) > 0) {
            position = this.getList().insertAfter(this.getList().last(), locItem);
        } else {
            current = this.getList().first();
            while (Objects.compare(locItem, current.element(), this.getComparator()) > 0) {
                current = this.getList().after(current);
            }
            position = this.getList().insertBefore(current, locItem);
        }
        locItem.setPosition(position);
        return locItem;
    }

    protected CPositionalKeyValueNode<K, V> locatorRemove(final Locator<CPositionalKeyValueNode<K, V>> locator) {
        this.getList().remove(locator.position());
        return locator.position().element();
    }

    public Locator<CPositionalKeyValueNode<K, V>> min() throws PriorityQueueEmptyException {
        if (this.getList().isEmpty()) {
            throw new PriorityQueueEmptyException("ERROR: priority queue is empty");
        }
        return this.getList().first().element();
    }

    public void insert(final Locator<CPositionalKeyValueNode<K, V>> locator) {
        this.locatorInsert(locator.position().element());
    }

    public Locator<CPositionalKeyValueNode<K, V>> insert(final CLocatorListNodeExtended<K, V> value) {
        final CPositionalKeyValueNode<K, V> node = new CPositionalKeyValueNode(value);
        return this.locatorInsert(node);
    }

    @Override
    public void insertItem(final CLocatorListNodeExtended<K, V> value) {
        this.insert(value);
    }

    public void remove(final Locator<CPositionalKeyValueNode<K, V>> locator) {
        this.locatorRemove(locator);
    }

    @Override
    public CLocatorListNodeExtended<K, V> removeMin() throws PriorityQueueEmptyException {
        final CLocatorListNodeExtended<K, V> node = this.minElement();
        this.remove(this.min());
        return node;
    }

    public V replaceElement(final Locator<CPositionalKeyValueNode<K, V>> locator, final V value) {
        final V oldValue = locator.position().element().getValue();
        locator.position().element().setValue(value);
        return oldValue;
    }

    public K replaceKey(final Locator<CPositionalKeyValueNode<K, V>> locator, final K key) {
        final CPositionalKeyValueNode<K, V> locatorItem = this.locatorRemove(locator);
        final K oldKey = locatorItem.getKey();
        locatorItem.setKey(key);
        this.locatorInsert(locatorItem);
        return oldKey;
    }
}
