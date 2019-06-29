package com.wildbeeslabs.jentle.algorithms.math;

public class Min implements AbstractAggregation {
    private Double min = Double.MAX_VALUE;

    @Override
    public void next(Number value) {
        if (min > value.doubleValue()) {
            min = value.doubleValue();
        }
    }

    @Override
    public Number getValue() {
        return min;
    }
}
