package com.wildbeeslabs.jentle.algorithms.math;

public interface AbstractAggregation {

    void next(final Number value);

    Double getValue();
}
