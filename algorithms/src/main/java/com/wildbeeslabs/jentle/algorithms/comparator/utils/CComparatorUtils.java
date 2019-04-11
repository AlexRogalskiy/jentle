package com.wildbeeslabs.jentle.algorithms.comparator.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

/**
 * Custom comparators utilities with null-safe implementation
 */
@Slf4j
@UtilityClass
public class CComparatorUtils {

    /**
     * Default comparator class
     *
     * @param <T>
     */
    public static class DefaultComparator<T extends Comparable<? super T>> implements Comparator<T>, Serializable {

        /**
         * Default explicit serialVersionUID for interoperability
         */
        private static final long serialVersionUID = 483179982991933496L;

        /**
         * Returns numeric result of arguments comparison:
         * "-1" - first argument is greater than the last one
         * "1" - last argument is greater than the first one
         * "0" - arguments are equal
         *
         * @param first - initial first argument
         * @param last  - initial last argument
         * @return numeric result of two entries comparison
         */
        @Override
        public int compare(final T first, final T last) {
            return CComparatorUtils.compareTo(first, last);
        }
    }

    /**
     * Returns default comparator instance {@link Comparator}
     *
     * @param <T>
     * @return default comparator instance {@link Comparator}
     */
    public static <T> Comparator<? super T> getDefaultComparator() {
        return new DefaultComparator();
    }

    /**
     * Returns numeric result of initial arguments {@link T} comparison by {@link Comparator}
     *
     * @param <T>
     * @param first - initial first argument {@link T}
     * @param last  - initial last argument {@link T}
     * @return numeric value of comparison
     */
    public static <T> int compare(final T first, final T last) {
        return compare(first, last, getDefaultComparator());
    }

    /**
     * Returns numeric result of initial arguments {@link T} comparison by {@link Comparator}
     *
     * @param <T>
     * @param first      - initial first argument {@link T}
     * @param last       - initial last argument {@link T}
     * @param comparator - initial comparator instance {@link Comparator}
     * @return numeric value of comparison
     */
    public static <T> int compare(final T first, final T last, final Comparator<? super T> comparator) {
        return Objects.compare(first, last, comparator);
    }

    /**
     * Returns numeric result of null-safe integer arguments {@link T} comparison
     *
     * @param <T>
     * @param first - initial first argument {@link T}
     * @param last  - initial last argument {@link T}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int intCompareTo(final T first, final T last) {
        return compareTo(Objects.nonNull(first) ? first.intValue() : null, Objects.nonNull(last) ? last.intValue() : null);
    }

    /**
     * Returns numeric result of null-safe long arguments {@link T} comparison
     *
     * @param <T>
     * @param first - initial first argument {@link T}
     * @param last  - initial last argument {@link T}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int longCompareTo(final T first, final T last) {
        return compareTo(Objects.nonNull(first) ? first.longValue() : null, Objects.nonNull(last) ? last.longValue() : null);
    }

    /**
     * Returns numeric result of null-safe float arguments {@link T} comparison
     *
     * @param <T>
     * @param first - initial first argument {@link T}
     * @param last  - initial last argument {@link T}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int floatCompareTo(final T first, final T last) {
        return compareTo(Objects.nonNull(first) ? first.floatValue() : null, Objects.nonNull(last) ? last.floatValue() : null);
    }

    /**
     * Returns numeric result of null-safe double arguments {@link T} comparison
     *
     * @param <T>
     * @param first - initial first argument {@link T}
     * @param last  - initial last argument {@link T}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int doubleCompareTo(final T first, final T last) {
        return compareTo(Objects.nonNull(first) ? first.doubleValue() : null, Objects.nonNull(last) ? last.doubleValue() : null);
    }

    /**
     * Returns numeric result of null-safe numeric arguments as bigDecimal {@link T} comparison
     *
     * @param <T>
     * @param first - initial first argument {@link T}
     * @param last  - initial last argument {@link T}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int bigDecimalCompareTo(final T first, final T last) {
        return bigDecimalCompareTo(Objects.nonNull(first) ? new BigDecimal(first.toString()) : null, Objects.nonNull(last) ? new BigDecimal(last.toString()) : null);
    }

    /**
     * Returns numeric result of null-safe bigDecimal arguments {@link BigDecimal} comparison
     *
     * @param <T>
     * @param first - initial first argument {@link BigDecimal}
     * @param last  - initial last argument {@link BigDecimal}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Number> int bigDecimalCompareTo(final BigDecimal first, final BigDecimal last) {
        return compareTo(first, last);
    }

    /**
     * Returns numeric result of null-safe object arguments as string {@link T} comparison
     *
     * @param <T>
     * @param first - initial first argument {@link BigDecimal}
     * @param last  - initial last argument {@link BigDecimal}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T> int stringCompareTo(final T first, final T last) {
        return stringCompareTo(String.valueOf(first), String.valueOf(last));
    }

    /**
     * Returns numeric result of null-safe string arguments {@link String} comparison
     *
     * @param first - initial first argument {@link String}
     * @param last  - initial last argument {@link String}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static int stringCompareTo(final String first, final String last) {
        final boolean f1, f2;
        return (f1 = Objects.isNull(first)) ^ (f2 = Objects.isNull(last)) ? (f1 ? -1 : 1) : (f1 && f2 ? 0 : first.compareToIgnoreCase(last));
    }

    /**
     * Returns numeric result of null-safe object arguments {@link T} comparison
     *
     * @param <T>
     * @param first - initial first argument {@link T}
     * @param last  - initial last argument {@link T}
     * @return a negative integer, zero, or a positive integer if the first
     * argument is less than, equal to, or greater than the second
     */
    public static <T extends Comparable<? super T>> int compareTo(final T first, final T last) {
        final boolean f1, f2;
        return (f1 = Objects.isNull(first)) ^ (f2 = Objects.isNull(last)) ? f1 ? -1 : 1 : f1 && f2 ? 0 : first.compareTo(last);
    }
}
