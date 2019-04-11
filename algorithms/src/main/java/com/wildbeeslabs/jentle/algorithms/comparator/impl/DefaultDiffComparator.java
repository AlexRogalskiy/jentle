package com.wildbeeslabs.jentle.algorithms.comparator.impl;

import com.wildbeeslabs.jentle.algorithms.comparator.entry.DefaultDiffEntry;
import com.wildbeeslabs.jentle.algorithms.comparator.entry.DiffEntry;
import com.wildbeeslabs.jentle.algorithms.comparator.utils.CComparatorUtils;
import com.wildbeeslabs.jentle.algorithms.utils.CReflectionUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections.comparators.ComparableComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.wildbeeslabs.jentle.algorithms.string.utils.CStringUtils.sanitize;

/**
 * Default difference comparator implementation
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DefaultDiffComparator<T> extends AbstractDiffComparator<T, DefaultDiffEntry> {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 2088063953605270171L;

    /**
     * Creates default difference comparator with initial class {@link Class}
     *
     * @param clazz - initial class instance {@link Class}
     */
    public DefaultDiffComparator(final Class<? extends T> clazz) {
        super(clazz);
    }

    /**
     * Creates default difference comparator with initial class {@link Class} and comparator instance {@link Comparator}
     *
     * @param clazz      - initial class instance {@link Class}
     * @param comparator - initial comparator instance {@link Comparator}
     */
    public DefaultDiffComparator(final Class<? extends T> clazz, final Comparator<? super T> comparator) {
        super(clazz, comparator, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    /**
     * Creates default difference comparator with with initial class {@link Class}, comparator instance {@link Comparator} and collection of included / excluded properties {@link List}
     *
     * @param comparator          - initial comparator instance {@link Comparator}
     * @param propertiesToExclude - initial list of properties to be excluded from comparison {@link List}
     * @param propertiesToInclude - initial list of properties to be included in comparison {@link List}
     */
    public DefaultDiffComparator(final Class<? extends T> clazz, final Comparator<? super T> comparator, final List<String> propertiesToExclude, final List<String> propertiesToInclude) {
        super(clazz, comparator, propertiesToExclude, propertiesToInclude);
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

    /**
     * Returns iterable collection of difference entries {@link DefaultDiffEntry}
     *
     * @param <S>
     * @param first - initial first argument to be compared {@link DefaultDiffEntry}
     * @param last  - initial last argument to be compared with {@link DefaultDiffEntry}
     * @return collection of difference entries {@link DefaultDiffEntry}
     */
    @Override
    public <S extends Iterable<? extends DiffEntry<?>>> S diffCompare(final T first, final T last) {
        final List<DefaultDiffEntry> diffComparatorEntryList = new ArrayList<>();
        getPropertyCollection(getClazz()).stream().forEach(propertyName -> {
            final Object firstValue = CReflectionUtils.getProperty(first, sanitize(propertyName));
            final Object lastValue = CReflectionUtils.getProperty(last, sanitize(propertyName));
            if (0 != CComparatorUtils.compare(firstValue, lastValue, ComparableComparator.getInstance())) {
                diffComparatorEntryList.add(createDiffEntry(firstValue, lastValue, propertyName));
            }
        });
        return (S) diffComparatorEntryList;
    }
}
