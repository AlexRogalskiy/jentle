package com.wildbeeslabs.jentle.algorithms.comparator.impl;

import com.wildbeeslabs.jentle.algorithms.comparator.entity.DeliveryInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

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
     * Creates delivery information difference comparator
     */
    public DeliveryInfoDiffComparator() {
        super(DeliveryInfo.class);
    }

    /**
     * Creates delivery information difference comparator with comparator instance {@link Comparator}
     *
     * @param comparator - initial comparator instance {@link Comparator}
     */
    public DeliveryInfoDiffComparator(final Comparator<? super DeliveryInfo> comparator) {
        super(DeliveryInfo.class, comparator);
    }

    /**
     * Creates delivery information difference comparator with comparator instance {@link Comparator} and collection of included / excluded properties {@link List}
     *
     * @param comparator          - initial comparator instance {@link Comparator}
     * @param propertiesToExclude - initial list of properties to be excluded from comparison {@link List}
     * @param propertiesToInclude - initial list of properties to be included in comparison {@link List}
     */
    public DeliveryInfoDiffComparator(final Comparator<? super DeliveryInfo> comparator, final List<String> propertiesToExclude, final List<String> propertiesToInclude) {
        super(DeliveryInfo.class, comparator, propertiesToExclude, propertiesToInclude);
    }
}
