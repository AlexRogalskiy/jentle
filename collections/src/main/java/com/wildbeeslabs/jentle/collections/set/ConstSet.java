package com.wildbeeslabs.jentle.collections.set;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.*;

/**
 * Immutable; implements a set based on hashCode() and equals(); null value is allowed.
 *
 * @param <K> - the type of element
 */

public final class ConstSet<K> extends AbstractSet<K> implements Serializable {

    /**
     * This ensures this class can be serialized reliably.
     */
    private static final long serialVersionUID = 8583016211809808382L;

    /**
     * The underlying Collections.unmodifiableSet set.
     */
    private final Set<K> set;

    /**
     * This caches a readonly empty Set.
     */
    private static final ConstSet<Object> emptyset = new ConstSet<Object>(new HashSet<Object>(0));

    /**
     * Constructs an unmodifiable map with the given set as the backing store.
     */
    private ConstSet(Set<? extends K> set) {
        this.set = Collections.unmodifiableSet(set);
    }

    /**
     * Returns an unmodifiable empty set.
     */
    @SuppressWarnings("unchecked")
    public static <K> ConstSet<K> make() {
        return (ConstSet<K>) emptyset;
    }

    /**
     * Returns an unmodifiable set with the same elements and traversal order as the given set.
     * (If set==null, we'll return an unmodifiable empty set)
     */
    public static <K> ConstSet<K> make(Iterable<K> collection) {
        if (collection instanceof ConstSet) return (ConstSet<K>) collection;
        LinkedHashSet<K> ans = null;
        if (collection != null) for (K element : collection) {
            if (ans == null) ans = new LinkedHashSet<K>();
            ans.add(element);
        }
        if (ans == null) return make();
        else return new ConstSet<K>(ans);
    }

    /**
     * Returns the number of objects in this set.
     */
    @Override
    public int size() {
        return set.size();
    }

    /**
     * Returns a read-only iterator over this set.
     */
    @Override
    public Iterator<K> iterator() {
        return set.iterator();
    }

    /**
     * Returns true if the given object is in this set.
     */
    @Override
    public boolean contains(Object element) {
        return set.contains(element);
    }
}
