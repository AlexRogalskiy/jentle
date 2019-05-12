package com.wildbeeslabs.jentle.algorithms.merger;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

/**
 * Subtract {@link AbstractMerger} implementation
 *
 * @param <T> type of value to merge
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubtractMerger<T> extends AbstractMerger<T> {

    @Override
    public void alsLess(final T value, final Collection<T> collection) {
        collection.add(value);
    }
}
