package com.wildbeeslabs.jentle.algorithms.comparator.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Objects;

/**
 * Custom string utilities implementation
 */
public final class StringUtils {

    /**
     * Default regular expression (only alpha-numeric characters)
     */
    public static final String DEFAULT_ALPHANUMERIC_PATTERN = "[^a-zA-Z0-9]";

    /**
     * Default Logger instance
     */
    protected static final Logger LOGGER = LogManager.getLogger(StringUtils.class);

    /**
     * Default private constructor
     */
    private StringUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Returns string with replaced values by pattern
     *
     * @param initialValue - initial argument value
     * @param pattern      - initial pattern to be replaced by
     * @param replaceValue - initial value replacement by pattern occurrences
     * @return string with replaced values by pattern
     */
    public static String replaceAll(final String initialValue, final String pattern, final String replaceValue) {
        Objects.requireNonNull(initialValue);
        return initialValue.replaceAll(pattern, replaceValue);
    }

    /**
     * Returns string sanitized by default regex pattern {@see DEFAULT_ALPHANUMERIC_PATTERN}
     *
     * @param initialValue - initial argument value
     * @return string stripped by default regex pattern
     */
    public static String sanitize(final String initialValue) {
        return replaceAll(initialValue, DEFAULT_ALPHANUMERIC_PATTERN, org.apache.commons.lang3.StringUtils.EMPTY).trim();
    }
}
