package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * A description of the value contained in a measurement.
 *
 * @author Jon Schneider
 */
@Getter
@RequiredArgsConstructor
public enum StatisticType {
    /**
     * The sum of the amounts recorded.
     */
    TOTAL("total"),

    /**
     * The sum of the times recorded. Reported in the monitoring system's base unit of time
     */
    TOTAL_TIME("total"),

    /**
     * Rate per second for calls.
     */
    COUNT("count"),

    /**
     * The maximum amount recorded. When this represents a time, it is reported in the monitoring system's base unit of time.
     */
    MAX("max"),

    /**
     * Instantaneous value, such as those reported by gauges.
     */
    VALUE("value"),

    /**
     * Undetermined.
     */
    UNKNOWN("unknown"),

    /**
     * Number of currently active tasks for a long task timer.
     */
    ACTIVE_TASKS("active"),

    /**
     * Duration of a running task in a long task timer. Always reported in the monitoring system's base unit of time.
     */
    DURATION("duration");

    private final String value;

    public static StatisticType findByValue(final String value) {
        if (Objects.nonNull(value)) {
            for (final StatisticType type : values()) {
                if (type.getValue().equalsIgnoreCase(value)) {
                    return type;
                }
            }
        }
        return null;
    }
}
