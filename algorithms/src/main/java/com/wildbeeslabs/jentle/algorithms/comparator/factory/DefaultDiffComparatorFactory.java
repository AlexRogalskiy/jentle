package com.wildbeeslabs.jentle.algorithms.comparator.factory;

import com.wildbeeslabs.jentle.algorithms.comparator.DiffComparator;
import com.wildbeeslabs.jentle.algorithms.comparator.entity.DeliveryInfo;
import com.wildbeeslabs.jentle.algorithms.comparator.impl.DefaultDiffComparator;
import com.wildbeeslabs.jentle.algorithms.comparator.impl.DeliveryInfoDiffComparator;

/**
 * Default difference comparator factory implementation
 */
public class DefaultDiffComparatorFactory {

    /**
     * Creates difference comparator {@link DiffComparator} by class instance {@link Class}
     *
     * @param <E>
     * @param clazz - initial class instance {@link Class} to initialize comparator {@link DiffComparator}
     * @return difference comparator {@link DiffComparator}
     */
    public static <T> DiffComparator<T> create(final Class<? extends T> clazz) {
        return new DefaultDiffComparator<>(clazz);
    }

    /**
     * Creates delivery information difference comparator {@link DeliveryInfoDiffComparator}
     *
     * @param <E>
     * @return delivery information difference comparator {@link DeliveryInfoDiffComparator}
     */
    public static <E extends DiffComparator<DeliveryInfo>> DeliveryInfoDiffComparator create() {
        return new DeliveryInfoDiffComparator();
    }
}
