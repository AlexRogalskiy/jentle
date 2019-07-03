package com.wildbeeslabs.jentle.algorithms.interfaces;

/**
 * Binary operator for edge weights. There are some prewritten operators.
 */
public interface WeightCombiner {
    /**
     * Sum of weights.
     */
    WeightCombiner SUM = (a, b) -> a + b;

    /**
     * Multiplication of weights.
     */
    WeightCombiner MULT = (a, b) -> a * b;

    /**
     * Minimum weight.
     */
    WeightCombiner MIN = (a, b) -> Math.min(a, b);

    /**
     * Maximum weight.
     */
    WeightCombiner MAX = (a, b) -> Math.max(a, b);

    /**
     * First weight.
     */
    WeightCombiner FIRST = (a, b) -> a;

    /**
     * Second weight.
     */
    WeightCombiner SECOND = (a, b) -> b;

    /**
     * Combines two weights.
     *
     * @param a first weight
     * @param b second weight
     * @return result of the operator
     */
    double combine(double a, double b);
}
