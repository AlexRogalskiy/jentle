package com.wildbeeslabs.jentle.algorithms.comparator.impl;

import com.wildbeeslabs.jentle.algorithms.comparator.entry.DefaultDiffEntry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

/**
 * Default difference comparator implementation
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class DefaultDiffComparator<T> extends AbstractDiffComparator<T, DefaultDiffEntry> {

    /**
     * Creates device information difference comparator
     */
    public DefaultDiffComparator() {
        super();
    }

    /**
     * Creates device information difference comparator
     *
     * @param comparator - initial comparator instance {@link Comparator}
     */
    public DefaultDiffComparator(final Comparator<? super T> comparator) {
        super(comparator);
    }

    /**
     * Creates default difference comparator with comparator instance and collection of included / excluded properties
     *
     * @param comparator          - initial comparator instance {@link Comparator}
     * @param propertiesToExclude - initial list of properties to be excluded from comparison {@link List}
     * @param propertiesToInclude - initial list of properties to be included in comparison {@link List}
     */
    public DefaultDiffComparator(final Comparator<? super T> comparator, final List<String> propertiesToExclude, final List<String> propertiesToInclude) {
        super(comparator, propertiesToExclude, propertiesToInclude);
    }

    /**
     * Returns default difference entry {@link DefaultDiffEntry}
     *
     * @param first        - initial first argument
     * @param last         - initial last argument
     * @param propertyName - initial property name
     * @return default difference entry {@link DefaultDiffEntry}
     */
    protected DefaultDiffEntry createDiffEntry(final Object first, final Object last, final String propertyName) {
        return DefaultDiffEntry.builder().first(first).last(last).propertyName(propertyName).build();
    }
}
