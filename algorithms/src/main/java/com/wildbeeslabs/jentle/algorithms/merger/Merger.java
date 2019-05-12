package com.wildbeeslabs.jentle.algorithms.merger;

import lombok.NonNull;

import java.util.Collection;

/**
 * Merger interface declaration
 *
 * @param <T> type of value to merge
 */
public interface Merger<T> {

    default void bothAreEqual(final T valueA, final T valueB, @NonNull final Collection<T> collection) {
    }

    default void blsLess(final T value, @NonNull final Collection<T> collection) {
    }

    default void alsLess(final T value, @NonNull final Collection<T> collection) {
    }
}
