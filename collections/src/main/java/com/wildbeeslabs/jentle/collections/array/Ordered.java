package com.wildbeeslabs.jentle.collections.array;

import java.util.Comparator;

/**
 * An ordered collection interface.
 *
 * @param <T> Component type
 */
public interface Ordered<T> {

    /**
     * Returns the comparator which defines the order of the elements contained in this collection.
     *
     * @return The comparator that defines the order of this collection's elements.
     */
    Comparator<T> comparator();
}
