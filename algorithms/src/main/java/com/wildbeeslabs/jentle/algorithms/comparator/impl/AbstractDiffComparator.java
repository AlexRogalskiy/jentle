package com.wildbeeslabs.jentle.algorithms.comparator.impl;

import com.google.common.collect.Sets;
import com.wildbeeslabs.jentle.algorithms.comparator.DiffComparator;
import com.wildbeeslabs.jentle.algorithms.comparator.entry.DiffEntry;
import com.wildbeeslabs.jentle.algorithms.comparator.utils.CComparatorUtils;
import com.wildbeeslabs.jentle.algorithms.utils.CReflectionUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.comparators.ComparableComparator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract difference comparator implementation
 */
@Slf4j
@Data
@EqualsAndHashCode
@ToString
public abstract class AbstractDiffComparator<T, E extends DiffEntry<?>> implements DiffComparator<T> {

    /**
     * Default initial legacy difference comparator
     */
    private final Comparator<? super T> comparator;
    /**
     * Default list of properties {@link List} to be excluded from comparison
     */
    private final List<String> propertiesToExclude = new ArrayList<>();
    /**
     * Default list of properties {@link List} to be included in comparison
     */
    private final List<String> propertiesToInclude = new ArrayList<>();
    /**
     * Default class instance {@link Class}
     */
    private final Class<? extends T> clazz;

    /**
     * Creates difference comparator with initial class {@link Class}
     *
     * @param clazz - initial class instance {@link Class}
     */
    public AbstractDiffComparator(final Class<? extends T> clazz) {
        this(clazz, null);
    }

    /**
     * Creates difference comparator with initial class {@link Class} and comparator instance {@link Comparator}
     *
     * @param clazz      - initial class instance {@link Class}
     * @param comparator - initial comparator instance {@link Comparator}
     */
    public AbstractDiffComparator(final Class<? extends T> clazz, final Comparator<? super T> comparator) {
        this(clazz, comparator, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    /**
     * Creates difference comparator with initial class {@link Class}, comparator instance {@link Comparator} and collection of included / excluded properties {@link List}
     *
     * @param clazz               - initial class instance {@link Class}
     * @param comparator          - initial comparator instance {@link Comparator}
     * @param propertiesToExclude - initial list of properties to be excluded from comparison {@link List}
     * @param propertiesToInclude - initial list of properties to be included in comparison {@link List}
     */
    public AbstractDiffComparator(final Class<? extends T> clazz, final Comparator<? super T> comparator, final List<String> propertiesToExclude, final List<String> propertiesToInclude) {
        this.clazz = Objects.requireNonNull(clazz);
        this.comparator = Objects.nonNull(comparator) ? comparator : ComparableComparator.getInstance();
        setPropertiesToExclude(propertiesToExclude);
        setPropertiesToInclude(propertiesToInclude);
    }

    /**
     * Updates properties in exclude compare collection
     *
     * @param propertiesToExclude - collection of properties to be updated in exclude compare collection
     */
    public void setPropertiesToExclude(final List<String> propertiesToExclude) {
        this.propertiesToExclude.clear();
        if (Objects.nonNull(propertiesToExclude)) {
            this.propertiesToExclude.addAll(propertiesToExclude);
        }
    }

    /**
     * Adds property to exclude compare collection
     *
     * @param propertyToExclude - property to be added to exclude compare collection
     */
    public void addPropertyToExclude(final String propertyToExclude) {
        if (Objects.nonNull(propertyToExclude)) {
            this.propertiesToExclude.add(propertyToExclude);
        }
    }

    /**
     * Removes property from exclude compare collection
     *
     * @param propertyToExclude - property to be removed from exclude compare collection
     */
    public void removePropertyFromExclude(final String propertyToExclude) {
        if (Objects.nonNull(propertyToExclude)) {
            this.propertiesToExclude.remove(propertyToExclude);
        }
    }

    /**
     * Updates properties in include compare collection
     *
     * @param propertiesToInclude - collection of properties to be updated in include compare collection
     */
    public void setPropertiesToInclude(final List<String> propertiesToInclude) {
        this.propertiesToInclude.clear();
        if (Objects.nonNull(propertiesToInclude)) {
            this.propertiesToInclude.addAll(propertiesToInclude);
        }
    }

    /**
     * Adds property to include compare collection
     *
     * @param propertyToInclude - property to be added to include compare collection
     */
    public void addPropertyToInclude(final String propertyToInclude) {
        if (Objects.nonNull(propertyToInclude)) {
            this.propertiesToInclude.add(propertyToInclude);
        }
    }

    /**
     * Removes property from include compare collection
     *
     * @param propertyToInclude - property to be removed from include compare collection
     */
    public void removePropertyFromInclude(final String propertyToInclude) {
        if (Objects.nonNull(propertyToInclude)) {
            this.propertiesToInclude.remove(propertyToInclude);
        }
    }

    /**
     * Returns binary result of initial arguments comparison by property value
     *
     * @param first    - initial first argument to be compared {@link T}
     * @param last     - initial last argument to be compared to {@link T}
     * @param property - initial property value
     * @return binary result of initial arguments comparison
     */
    protected boolean isEqual(final T first, final T last, final String property) {
        return isEqualTo(first, last, property, ComparableComparator.getInstance(), 0);
    }

    /**
     * Returns binary result of initial arguments comparison by property value, comparator {@link Comparator} and numeric compareResult
     *
     * @param first         - initial first argument to be compared {@link T}
     * @param last          - initial last argument to be compared to {@link T}
     * @param property      - initial property value
     * @param comparator    - initial comparator instance {@link Comparator}
     * @param compareResult - initial compare result
     * @return binary result of initial arguments comparison
     */
    protected boolean isEqualTo(final T first, final T last, final String property, final Comparator<Object> comparator, int compareResult) {
        return (compareResult == compare(first, last, property, comparator));
    }

    /**
     * Returns numeric result of initial entities comparison by property value
     *
     * @param first        - initial first argument to be compared {@link T}
     * @param last         - initial last argument to be compared to {@link T}
     * @param propertyName - initial property name
     * @param comparator   - initial comparator instance {@link Comparator}
     * @return numeric result of initial arguments comparison
     */
    protected int compare(final T first, final T last, final String propertyName, final Comparator<Object> comparator) {
        if (Objects.isNull(propertyName)) {
            return getComparator().compare(first, last);
        }
        final Object firstValue = CReflectionUtils.getProperty(first, propertyName);
        final Object lastValue = CReflectionUtils.getProperty(first, propertyName);
        return CComparatorUtils.compare(firstValue, lastValue, comparator);
    }

    /**
     * Returns list of field {@link List} names (except static/final/accessible) by argument class {@link Class}
     *
     * @param clazz - initial argument class {@link Class}
     * @return list of field names {@link List}
     */
    protected List<String> getFieldsList(final Class<? extends T> clazz) {
        return CReflectionUtils.getValidFields(CReflectionUtils.getAllFields(clazz), false, false).stream().map(field -> field.getName()).collect(Collectors.toList());
    }

    /**
     * Returns set of field names {@link Set} by argument class {@link Class}
     *
     * @param clazz - initial argument class {@link Class}
     * @return set of field names {@link Set}
     */
    protected Set<String> getFieldsSet(final Class<? extends T> clazz) {
        return Sets.newHashSet(this.getFieldsList(clazz));
    }

    /**
     * Returns collection of property names {@link Collection} by class {@link Class} based on excluded / included properties
     *
     * @param clazz - initial class instance {@link Class}
     * @return collection of property names {@link Collection}
     */
    protected Collection<String> getPropertyCollection(final Class<? extends T> clazz) {
        if (getPropertiesToInclude().isEmpty() || getPropertiesToExclude().isEmpty()) {
            if (getPropertiesToInclude().isEmpty()) {
                return CollectionUtils.subtract(getFieldsList(clazz), getPropertiesToExclude());
            }
            if (getPropertiesToExclude().isEmpty()) {
                return getPropertiesToInclude();
            }
            return getFieldsList(clazz);
        }
        return CollectionUtils.subtract(getPropertiesToInclude(), getPropertiesToExclude());
    }
}
