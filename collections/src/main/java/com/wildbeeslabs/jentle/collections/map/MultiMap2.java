package com.wildbeeslabs.jentle.collections.map;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * Map from a key to multiple values.
 * Order is significant among values, and null values are allowed, although null keys are not.
 *
 * @param <K> The key of the multi-map
 * @param <V> The types in the multi-map
 * @author Kohsuke Kawaguchi
 * @author Jerome Dochez
 */
public class MultiMap2<K, V> implements Serializable, Cloneable {
    /**
     * For serialization
     */
    private static final long serialVersionUID = 893592003056170756L;

    private final Map<K, List<V>> store = new LinkedHashMap<K, List<V>>();

    /**
     * Creates an empty multi-map with default concurrency controls
     */
    public MultiMap2() {
    }

    /**
     * Copy constructor.
     *
     * @param base map to copy
     */
    public MultiMap2(MultiMap2<K, V> base) {
        this();
        for (Map.Entry<K, List<V>> e : base.entrySet()) {
            List<V> value = newList(e.getValue());
            if (!value.isEmpty()) {
                store.put(e.getKey(), newList(e.getValue()));
            }
        }
    }

    /**
     * Creates an optionally populated list to be used as an entry in the map.
     *
     * @param initialVals
     * @return
     */
    private List<V> newList(final Collection<? extends V> initialVals) {
        if (Objects.isNull(initialVals)) {
            return new LinkedList<V>();
        }
        return new LinkedList<V>(initialVals);
    }

    /**
     * Returns the set of keys associated with this MultiMap2
     *
     * @return The set of keys currently available in this MultiMap2.  Will not return null,
     * but may return a Set of lenght zero
     */
    public Set<K> keySet() {
        return this.store.keySet();
    }

    /**
     * Adds one more key-value pair.
     *
     * @param k key to store the entry under
     * @param v value to store in the k's values.
     */
    public final void add(final K k, final V v) {
        List<V> l = store.get(k);
        if (Objects.isNull(l)) {
            l = newList(null);
            this.store.put(k, l);
        }
        l.add(v);
    }

    /**
     * Replaces all the existing values associated with the key
     * by the given value.  If v is empty the key k will
     * be removed from the MultiMap2.
     *
     * @param k key for the values
     * @param v Can be null or empty.
     */
    public void set(final K k, final Collection<? extends V> v) {
        final List<V> addMe = newList(v);
        if (addMe.isEmpty()) {
            this.store.remove(k);
        } else {
            this.store.put(k, newList(v));
        }
    }

    /**
     * Replaces all the existing values associated with the key
     * by the given single value.
     *
     * @param k key for the values
     * @param v singleton value for k key
     *          <p/>
     *          This is short for <tt>set(k,Collections.singleton(v))</tt>
     */
    public void set(K k, V v) {
        final List<V> vlist = this.newList(null);
        vlist.add(v);
        this.store.put(k, vlist);
    }

    /**
     * Returns the elements indexed by the provided key
     *
     * @param k key for the values
     * @return Can be empty but never null. Read-only.
     */
    public final List<V> get(K k) {
        List<V> l = this.store.get(k);
        if (Objects.isNull(l)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(l);
    }

    /**
     * This method merges all of the keys and values from another
     * MultiMap2 into this MultiMap2.  If a key/value pair is
     * found in both MultiMaps it is not re-added to this
     * MultiMap2, but is instead discarded
     *
     * @param another The MultiMap2 from which to add values
     *                to this MultiMap2.  If null this method does nothing
     */
    public void mergeAll(final MultiMap2<K, V> another) {
        if (Objects.isNull(another)) return;
        for (Map.Entry<K, List<V>> entry : another.entrySet()) {
            List<V> ourList = store.get(entry.getKey());
            if (Objects.isNull(ourList)) {
                ourList = newList(entry.getValue());
                if (!ourList.isEmpty()) {
                    this.store.put(entry.getKey(), ourList);
                }
            } else {
                for (final V v : entry.getValue()) {
                    if (!ourList.contains(v)) {
                        ourList.add(v);
                    }
                }
            }
        }
    }

    /**
     * Package private (for getting the raw map for direct manipulation by the habitat)
     *
     * @param k the key
     * @return
     */
    private final List<V> _get(K k) {
        final List<V> l = this.store.get(k);
        if (Objects.isNull(l)) {
            return Collections.emptyList();
        }
        return l;
    }

    /**
     * Checks if the map contains the given key.
     *
     * @param k key to test
     * @return true if the map contains at least one element for this key
     */
    public boolean containsKey(final K k) {
        return !this.get(k).isEmpty();
    }

    /**
     * Checks if the map contains the given key(s), also extending the search
     * to including the sub collection.
     *
     * @param k1 key from top collection
     * @param k2 key (value) from inner collection
     * @return true if the map contains at least one element for these keys
     */
    public boolean contains(final K k1, final V k2) {
        final List<V> list = this._get(k1);
        return list.contains(k2);
    }

    /**
     * Removes an key value from the map
     *
     * @param key key to be removed
     * @return the value stored under this key or null if there was none
     */
    public List<V> remove(final K key) {
        return this.store.remove(key);
    }

    /**
     * Removes an key value pair from the map.  If the list of
     * entries for that key is empty after the remove
     * it will be removed from the set of keys
     *
     * @param key   key to be removed
     * @param entry the entry to be removed from the key'ed list
     * @return true if there was none that was deleted
     */
    public boolean remove(final K key, final V entry) {
        final List<V> list = store.get(key);
        if (Objects.isNull(list)) return false;
        boolean retVal = list.remove(entry);
        if (list.isEmpty()) {
            store.remove(key);
        }
        return retVal;
    }

    /**
     * Gets the first value if any, or null.
     * <p/>
     * This is useful when you know the given key only has one value and you'd like
     * to get to that value.
     *
     * @param k key for the values
     * @return null if the key has no values or it has a value but the value is null.
     */
    public V getOne(final K k) {
        return getFirst(k);
    }

    private V getFirst(final K k) {
        List<V> lst = this.store.get(k);
        if (Objects.isNull(lst)) {
            return null;
        }
        if (lst.isEmpty()) {
            return null;
        }
        return lst.get(0);
    }

    /**
     * Lists up all entries.
     *
     * @return a {@link java.util.Set} of {@link java.util.Map.Entry} of entries
     */
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return this.store.entrySet();
    }

    /**
     * @return the map as "key=value1,key=value2,...."
     */
    public String toCommaSeparatedString() {
        final StringBuilder buf = new StringBuilder();
        for (Map.Entry<K, List<V>> e : entrySet()) {
            for (V v : e.getValue()) {
                if (buf.length() > 0) {
                    buf.append(',');
                }
                buf.append(e.getKey()).append('=').append(v);
            }
        }
        return buf.toString();
    }

    /**
     * Creates a copy of the map that contains the exact same key and value set.
     * Keys and values won't cloned.
     */
    @Override
    public MultiMap2<K, V> clone() throws CloneNotSupportedException {
        super.clone();
        return new MultiMap2<K, V>(this);
    }

    /**
     * Returns the size of the map.  This returns the numbers
     * of keys in the map, not the number of values
     *
     * @return integer or 0 if the map is empty
     */
    public int size() {
        return store.size();
    }

    @Override
    public int hashCode() {
        return store.hashCode();
    }

    @SuppressWarnings("unchecked")
    public boolean equal(final Object another) {
        if (Objects.isNull(another) || !(another instanceof MultiMap2)) return false;
        final MultiMap2<K, V> other = (MultiMap2<K, V>) another;
        return this.store.equals(other.store);
    }

    private final static String NEWLINE = AccessController.doPrivileged(new PrivilegedAction<String>() {
        @Override
        public String run() {
            return System.getProperty("line.separator");
        }
    });

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (final K key : store.keySet()) {
            builder.append(key).append(": ");
            builder.append(store.get(key));
            builder.append(NEWLINE);
        }
        builder.append("}");
        return builder.toString();
    }
}
