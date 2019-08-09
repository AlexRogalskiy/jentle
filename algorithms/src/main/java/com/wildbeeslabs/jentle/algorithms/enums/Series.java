package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of HTTP status series.
 */
@Getter
@RequiredArgsConstructor
public enum Series {
    INFORMATIONAL(1),
    SUCCESSFUL(2),
    REDIRECTION(3),
    CLIENT_ERROR(4),
    SERVER_ERROR(5);

    private final int value;

    /**
     * Return the enum constant of this type with the corresponding series.
     *
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the enum constant of this type with the corresponding series
     * @throws IllegalArgumentException if this enum has no corresponding constant
     */
    public static Series valueOf(int statusCode) {
        final Series series = resolve(statusCode);
        if (series == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return series;
    }

    /**
     * Resolve the given status code to an {@code HttpStatus.Series}, if possible.
     *
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the corresponding {@code Series}, or {@code null} if not found
     * @since 5.1.3
     */
    public static Series resolve(final int statusCode) {
        int seriesCode = statusCode / 100;
        for (Series series : values()) {
            if (series.value == seriesCode) {
                return series;
            }
        }
        return null;
    }
}
