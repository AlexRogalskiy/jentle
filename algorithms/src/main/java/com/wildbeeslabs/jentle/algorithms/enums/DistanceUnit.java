package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * {@link Metric}s supported by Redis.
 *
 * @author Christoph Strobl
 * @since 1.8
 */
@Getter
@RequiredArgsConstructor
public enum DistanceUnit {
    METERS(6378137, "m"),
    KILOMETERS(6378.137, "km"),
    MILES(3963.191, "mi"),
    FEET(20925646.325, "ft");

    private final double multiplier;
    private final String abbreviation;
}
