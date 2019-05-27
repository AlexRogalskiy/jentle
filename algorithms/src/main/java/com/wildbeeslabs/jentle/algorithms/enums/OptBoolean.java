package com.wildbeeslabs.jentle.algorithms.enums;

import java.util.Objects;

public enum OptBoolean {
    /**
     * Value that indicates that the annotation property is explicitly defined to
     * be enabled, or true.
     */
    TRUE,

    /**
     * Value that indicates that the annotation property is explicitly defined to
     * be disabled, or false.
     */
    FALSE,

    /**
     * Value that indicates that the annotation property does NOT have an explicit
     * definition of enabled/disabled (or true/false); instead, a higher-level
     * configuration value is used; or lacking higher-level global setting,
     * default.
     */
    DEFAULT;

    public Boolean asBoolean() {
        if (this.equals(DEFAULT)) return null;
        return this.equals(TRUE) ? Boolean.TRUE : Boolean.FALSE;
    }

    public boolean asPrimitive() {
        return this.equals(TRUE);
    }

    public static OptBoolean fromBoolean(final Boolean value) {
        if (Objects.isNull(value)) {
            return DEFAULT;
        }
        return value.booleanValue() ? TRUE : FALSE;
    }

    public static boolean equals(final Boolean b1, final Boolean b2) {
        return Objects.equals(b1, b2);
    }
}
