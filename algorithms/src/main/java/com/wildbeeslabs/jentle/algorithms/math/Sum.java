package com.wildbeeslabs.jentle.algorithms.math;

public class Sum implements AbstractAggregation {
    private Double summation = 0d;

    @Override
    public void next(Number value) {
        summation += value.doubleValue();
    }

    @Override
    public Double getValue() {
        return summation;
    }
}
