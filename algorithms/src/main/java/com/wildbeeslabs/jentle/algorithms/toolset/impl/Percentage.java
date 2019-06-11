package com.wildbeeslabs.jentle.algorithms.toolset.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A positive percentage value.
 *
 * @author Alexander Bishcof
 */
@Data
@EqualsAndHashCode
@ToString
public class Percentage {
    public final double value;

    /**
     * Creates a new {@link org.assertj.core.data.Percentage}.
     *
     * @param value the value of the percentage.
     * @return the created {@code Percentage}.
     * @throws NullPointerException     if the given value is {@code null}.
     * @throws IllegalArgumentException if the given value is negative.
     */
    public static Percentage withPercentage(double value) {
        assert value >= 0 : String.format("The percentage value <%s> should be greater than or equal to zero", value);
        return new Percentage(value);
    }

    private Percentage(double value) {
        this.value = value;
    }

    private boolean noFractionalPart() {
        return (this.value % 1) == 0;
    }
}
