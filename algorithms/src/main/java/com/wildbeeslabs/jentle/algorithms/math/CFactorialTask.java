package com.wildbeeslabs.jentle.algorithms.math;

import com.wildbeeslabs.jentle.collections.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class CFactorialTask implements Callable<Integer> {

    private final int number;

    public Integer call() throws InvalidParameterException {
        int fact = 1;
        if (this.number < 0) {
            throw new InvalidParameterException("Number must be positive");
        }
        for (int count = this.number; count > 1; count--) {
            fact = fact * count;
        }
        return fact;
    }
}
