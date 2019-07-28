package com.wildbeeslabs.jentle.algorithms.math;

public class StandardDeviation implements AbstractAggregation {
    private Double sumSq = 0d;
    private Double sum = 0d;
    private Double count = 0d;

    @Override
    public void next(Number value) {
        sum += value.doubleValue();
        sumSq += value.doubleValue() * value.doubleValue();
        count++;
    }

    @Override
    public Double getValue() {
        return Math.sqrt(sumSq / count - sum * sum / count / count);
    }
}
