package com.wildbeeslabs.jentle.collections.common;

/**
 * An index generator for a set of keys.  An indexer maps each key in its keyset
 * to a unique integer in the range [0..#keys)
 *
 * @author Emina Torlak
 * @specfield keys: set K
 * @specfield indices: keys lone->one [0..#keys)
 */
public interface Indexer<K> {

    /**
     * Returns the index of the given key, if it is in this.keys.
     * Otherwise returns a negative number.
     *
     * @return key in this.keys => this.indices[key], {i: int | i < 0 }
     */
    int indexOf(final K key);

    /**
     * Returns the key at the given index.
     *
     * @return this.indices.index
     * @throws IndexOutOfBoundsException index !in this.indices[this.keys]
     */
    K keyAt(final int index);

    /**
     * Returns the number of keys in this.indexer.
     *
     * @return #this.keys
     */
    int size();
}
