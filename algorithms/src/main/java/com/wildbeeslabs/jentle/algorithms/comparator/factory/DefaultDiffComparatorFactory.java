package com.wildbeeslabs.jentle.algorithms.comparator.factory;

import com.wildbeeslabs.jentle.algorithms.comparator.DiffComparator;
import com.wildbeeslabs.jentle.algorithms.comparator.entity.DeliveryInfo;
import com.wildbeeslabs.jentle.algorithms.comparator.impl.DefaultDiffComparator;
import com.wildbeeslabs.jentle.algorithms.comparator.impl.DeliveryInfoDiffComparator;

import java.util.Comparator;

/**
 * Default difference comparator factory implementation
 */
public class DefaultDiffComparatorFactory {

    /**
     * Creates difference comparator {@link DiffComparator} by class instance {@link Class}
     *
     * @param <T>
     * @param clazz - initial class instance {@link Class} to initialize comparator {@link DiffComparator}
     * @return difference comparator {@link DiffComparator}
     */
    public static <T, E extends DiffComparator<T>> E create(final Class<? extends T> clazz) {
        return (E) new DefaultDiffComparator<>(clazz);
    }

    /**
     * Creates difference comparator {@link DiffComparator} by class instance {@link Class} with comparator instance {@link Comparator}
     *
     * @param <T>
     * @param clazz      - initial class instance {@link Class} to initialize comparator {@link DiffComparator}
     * @param comparator - initial comparator instance {@link Comparator}
     * @return difference comparator {@link DiffComparator}
     */
    public static <T, E extends DiffComparator<T>> E create(final Class<? extends T> clazz, final Comparator<? super T> comparator) {
        return (E) new DefaultDiffComparator<>(clazz, comparator);
    }

    /**
     * Creates delivery information difference comparator {@link DeliveryInfoDiffComparator}
     *
     * @param <E>
     * @return delivery information difference comparator {@link DeliveryInfoDiffComparator}
     */
    public static <E extends DiffComparator<DeliveryInfo>> E create() {
        return (E) new DeliveryInfoDiffComparator();
    }

    /**
     * Creates delivery information difference comparator {@link DeliveryInfoDiffComparator}
     *
     * @param <E>
     * @param comparator - initial comparator instance {@link Comparator}
     * @return delivery information difference comparator {@link DeliveryInfoDiffComparator}
     */
    public static <E extends DiffComparator<DeliveryInfo>> E create(final Comparator<? super DeliveryInfo> comparator) {
        return (E) new DeliveryInfoDiffComparator(comparator);
    }
}
