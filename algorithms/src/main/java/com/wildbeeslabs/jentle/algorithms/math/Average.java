package com.wildbeeslabs.jentle.algorithms.math;

public class Average implements AbstractAggregation {

    private Double summation = 0d;
    private Double count = 0d;

    @Override
    public void next(Number value) {
        count++;
        summation += value.doubleValue();
    }

    @Override
    public Double getValue() {
        if (count != 0d) {
            return summation / count;
        }
        return 0d;
    }
}
