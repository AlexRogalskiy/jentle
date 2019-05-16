package com.wildbeeslabs.jentle.algorithms.toolset.impl;

import com.wildbeeslabs.jentle.algorithms.toolset.iface.RangeIF;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;

/**
 * {@link RangeIF} implementation
 *
 * @param <T> type of range bound element
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Range<T extends Number> implements RangeIF<T> {

    /**
     * Range lower bound {@code T1}
     */
    private final T lowerBound;
    /**
     * Range upper bound {@code T}
     */
    private final T upperBound;

    /**
     * Returns {@link Range} by input parameters
     *
     * @param <T>        type of range bound element
     * @param lowerBound - initial input range lower bound {@code T}
     * @param upperBound - initial input range upper bound {@code T}
     * @return {@link Range}
     */
    public static <T extends Number> Range<T> of(final T lowerBound, final T upperBound) {
        return new Range<>(lowerBound, upperBound);
    }

    /**
     * Returns {@link Range} if both {@link Optional} instances have values or {@link Optional#empty()} if one or both
     * are missing.
     *
     * @param <T>        type of range bound element
     * @param lowerBound - initial input range lower bound {@link Optional}
     * @param upperBound - initial input range upper bound {@link Optional}
     * @return {@link Optional} of {@link Range}
     */
    public static <T extends Number> Optional<Range<T>> with(final Optional<T> lowerBound, final Optional<T> upperBound) {
        return lowerBound.flatMap(l -> upperBound.map(u -> Range.of(l, u)));
    }
}
