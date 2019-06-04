package com.wildbeeslabs.jentle.algorithms.math;

import java.util.concurrent.RecursiveTask;

/**
 * @author ashraf
 */
public class MaxNumberCalculator extends RecursiveTask {

    private static final long serialVersionUID = -8801038992110195789L;

    public static final int THRESHOLD = 5;

    private int[] numbers;
    private int start;
    private int end;

    public MaxNumberCalculator(final int[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public MaxNumberCalculator(final int[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer compute() {
        int length = this.end - this.start;
        int max = 0;
        if (length < THRESHOLD) {
            for (int x = this.start; x < this.end; x++) {
                max = this.numbers[x];
            }
            return max;
        } else {
            int split = length / 2;
            final MaxNumberCalculator left = new MaxNumberCalculator(this.numbers, this.start, this.start + split);
            left.fork();
            final MaxNumberCalculator right = new MaxNumberCalculator(this.numbers, this.start + split, this.end);
            return Math.max(right.compute(), (Integer) left.join());
        }
    }
}
