package com.wildbeeslabs.jentle.algorithms.math;

public class Max implements AbstractAggregation {
    private Double max = Double.MIN_VALUE;

    @Override
    public void next(Number value) {
        if (max < value.doubleValue()) {
            max = value.doubleValue();
        }
    }

    @Override
    public Number getValue() {
        return max;
    }
}
