package com.wildbeeslabs.jentle.algorithms.math;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

@Slf4j
@RequiredArgsConstructor
public class CFactorialSquareTask extends RecursiveTask<Integer> {

    private final Integer n;

    @Override
    protected Integer compute() {
        if (n <= 1) {
            return n;
        }
        final CFactorialSquareTask calculator = new CFactorialSquareTask(n - 1);
        calculator.fork();
        return n * n + calculator.join();
    }
}
