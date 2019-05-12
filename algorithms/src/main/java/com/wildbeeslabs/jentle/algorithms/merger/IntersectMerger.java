package com.wildbeeslabs.jentle.algorithms.merger;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

/**
 * Intersect {@link AbstractMerger} implementation
 *
 * @param <T> type of value to merge
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IntersectMerger<T> extends AbstractMerger<T> {

    @Override
    public void bothAreEqual(final T valueA, final T valueB, final Collection<T> collection) {
        collection.add(valueA);
    }
}
