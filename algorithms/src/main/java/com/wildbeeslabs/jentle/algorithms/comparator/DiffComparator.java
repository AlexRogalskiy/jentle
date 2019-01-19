package com.wildbeeslabs.jentle.algorithms.comparator;

import com.wildbeeslabs.jentle.algorithms.comparator.entry.DiffEntry;

import java.io.Serializable;

/**
 * Difference comparator declaration
 */
public interface DiffComparator<T> extends Serializable {

    /**
     * Returns collection of difference entries {@link S} with properties marked for comparison by initial arguments {@link T}
     *
     * @param first - initial first argument to be compared {@link T}
     * @param last  - initial last argument to be compared with {@link T}
     * @return collection of entries {@link S} with compared properties
     */
    <S extends Iterable<? extends DiffEntry<?>>> S diffCompare(final T first, final T last);
}
