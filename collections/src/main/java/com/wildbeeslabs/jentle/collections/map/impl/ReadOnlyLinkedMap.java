package com.wildbeeslabs.jentle.collections.map.impl;

import org.apache.commons.collections4.map.AbstractLinkedMap;
import org.apache.commons.collections4.map.LinkedMap;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Serializable, unmodifiable collection view for {@link LinkedMap}. <br>
 * Use this collection view instead of {@link LinkedMap#values()} to have ability to serialize collection.
 */
public class ReadOnlyLinkedMap<K, V> extends AbstractCollection<Map.Entry<K, V>> implements Serializable {
    private final LinkedMap<K, V> parent;

    public ReadOnlyLinkedMap(final LinkedMap<K, V> parent) {
        this.parent = parent;
    }

    @Override
    public int size() {
        return this.parent.size();
    }

    @Override
    public boolean contains(final Object value) {
        return this.parent.containsValue(value);
    }

    @Override
    public Iterator iterator() {
        return LinkedMapValuesIteratorProvider.createValuesIterator(this.parent);
    }

    @Override
    public boolean add(final Map.Entry<K, V> e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends Map.Entry<K, V>> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(final Predicate<? super Map.Entry<K, V>> filter) {
        throw new UnsupportedOperationException();
    }

    protected static class LinkedMapValuesIteratorProvider<K, V> extends AbstractLinkedMap<K, V> {

        public static <K, V> Iterator<Map.Entry<K, V>> createValuesIterator(final LinkedMap<K, V> linkedMap) {
            return new ReadOnlyValuesIterator(linkedMap);
        }

        protected static class ReadOnlyValuesIterator<K, V> extends ValuesIterator<V> {
            public ReadOnlyValuesIterator(final AbstractLinkedMap<K, V> parent) {
                super(parent);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
