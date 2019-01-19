package com.wildbeeslabs.jentle.algorithms.comparator.impl;

import com.wildbeeslabs.jentle.algorithms.comparator.entity.DeliveryInfo;
import com.wildbeeslabs.jentle.algorithms.comparator.entry.DefaultDiffEntry;
import com.wildbeeslabs.jentle.algorithms.comparator.utils.ComparatorUtils;
import com.wildbeeslabs.jentle.algorithms.comparator.utils.ReflectionUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections.comparators.ComparableComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.wildbeeslabs.jentle.algorithms.comparator.utils.StringUtils.sanitize;

/**
 * Delivery info difference comparator implementation
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"serialVersionUID"})
public class DeliveryInfoDiffComparator extends DefaultDiffComparator<DeliveryInfo> {

    /**
     * Default explicit serialVersionUID for interoperability
     */
    private static final long serialVersionUID = 436117742331557518L;

    /**
     * Creates device information difference comparator
     */
    public DeliveryInfoDiffComparator() {
        super();
    }

    /**
     * Creates device information difference comparator
     *
     * @param comparator - initial comparator instance {@link Comparator}
     */
    public DeliveryInfoDiffComparator(final Comparator<? super DeliveryInfo> comparator) {
        super(comparator);
    }

    /**
     * Creates default difference comparator with comparator instance and collection of included / excluded properties
     *
     * @param comparator          - initial comparator instance {@link Comparator}
     * @param propertiesToExclude - initial list of properties to be excluded from comparison {@link List}
     * @param propertiesToInclude - initial list of properties to be included in comparison {@link List}
     */
    public DeliveryInfoDiffComparator(final Comparator<? super DeliveryInfo> comparator, final List<String> propertiesToExclude, final List<String> propertiesToInclude) {
        super(comparator, propertiesToExclude, propertiesToInclude);
    }

    /**
     * Returns iterable collection of difference entries {@link DefaultDiffEntry}
     *
     * @param first - initial first argument to be compared {@link DefaultDiffEntry}
     * @param last  - initial last argument to be compared with {@link DefaultDiffEntry}
     * @param <S>
     * @return collection of difference entries {@link DefaultDiffEntry}
     */
    @Override
    public <S extends Iterable<? extends DefaultDiffEntry>> S diffCompare(final DeliveryInfo first, final DeliveryInfo last) {
        final List<DefaultDiffEntry> diffComparatorEntryList = new ArrayList<>();
        getPropertyCollection(DeliveryInfo.class).stream().forEach(propertyName -> {
            final Object firstValue = ReflectionUtils.getProperty(first, sanitize(propertyName));
            final Object lastValue = ReflectionUtils.getProperty(last, sanitize(propertyName));
            if (0 != ComparatorUtils.compare(firstValue, lastValue, ComparableComparator.getInstance())) {
                diffComparatorEntryList.add(createDiffEntry(firstValue, lastValue, propertyName));
            }
        });
        return (S) diffComparatorEntryList;
    }
}
