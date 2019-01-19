package com.wildbeeslabs.jentle.algorithms.comparator.factory;

import com.wildbeeslabs.jentle.algorithms.comparator.DiffComparator;

/**
 * Default difference comparator factory declaration
 */
public abstract class DiffComparatorFactory<T> {

    /**
     * Creates difference comparator {@link E} by class instance {@link Class}
     *
     * @param <E>
     * @param clazz - initial class instance {@link Class} to initialize comparator
     * @return difference comparator {@link E}
     */
    abstract <E extends DiffComparator<T>> E create(final Class<? extends T> clazz);
}
