package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Enumeration of HTTP status series.
 * <p>Retrievable via {@link HttpStatus#series()}.
 */
public enum Series {
    INFORMATIONAL(1),
    SUCCESSFUL(2),
    REDIRECTION(3),
    CLIENT_ERROR(4),
    SERVER_ERROR(5);

    private final int value;

    Series(int value) {
        this.value = value;
    }

    /**
     * Return the integer value of this status series. Ranges from 1 to 5.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the enum constant of this type with the corresponding series.
     *
     * @param status a standard HTTP status enum value
     * @return the enum constant of this type with the corresponding series
     * @throws IllegalArgumentException if this enum has no corresponding constant
     */
    public static Series valueOf(HttpStatus status) {
        return valueOf(status.value);
    }

    /**
     * Return the enum constant of this type with the corresponding series.
     *
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the enum constant of this type with the corresponding series
     * @throws IllegalArgumentException if this enum has no corresponding constant
     */
    public static Series valueOf(int statusCode) {
        Series series = resolve(statusCode);
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
    @Nullable
    public static Series resolve(int statusCode) {
        int seriesCode = statusCode / 100;
        for (Series series : values()) {
            if (series.value == seriesCode) {
                return series;
            }
        }
        return null;
    }
}
