package com.wildbeeslabs.jentle.algorithms.merger;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

/**
 * Union {@link AbstractMerger} implementation
 *
 * @param <T> type of value to merge
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnionMerger<T> extends AbstractMerger<T> {

    @Override
    public void alsLess(final T value, final Collection<T> collection) {
        collection.add(value);
    }

    @Override
    public void bothAreEqual(final T valueA, final T valueB, final Collection<T> collection) {
        collection.add(valueA);
    }

    @Override
    public void blsLess(final T value, final Collection<T> collection) {
        collection.add(value);
    }
}
