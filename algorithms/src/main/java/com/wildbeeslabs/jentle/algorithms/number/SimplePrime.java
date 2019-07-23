package com.wildbeeslabs.jentle.algorithms.number;

import lombok.experimental.UtilityClass;

/**
 * @author Heiko Klein
 */
@UtilityClass
public class SimplePrime {

    /**
     * @return lowest number below or equal the current prime, return 0 for number < 2
     */
    public static long belowOrEqual(long number) {
        if (number < 2) {
            return 0;
        } else if (number == 2) {
            return 2;
        }
        if ((number & 1) == 0) { // even
            number--;
        }
        while (!testPrime(number)) {
            number -= 2;
            if (number <= 2) {
                return 2;
            }
        }
        return number;
    }

    public static long aboveOrEqual(long number) {
        if (number <= 2) {
            return 2;
        }
        if ((number & 1) == 0) { // even
            number++;
        }
        while (!testPrime(number)) {
            number += 2;
            if (number < 0) {
                return 0; // overflow
            }
        }
        return number;
    }

    public static boolean testPrime(long number) {
        if (number == 2) {
            return true;
        }
        if (number < 2) {
            return false;
        }
        if ((number & 1) == 0) { // even
            return false;
        }
        long sqrt = (long) Math.floor(Math.sqrt(number));
        for (long i = 3; i <= sqrt; i += 2) {
            if ((number % i) == 0) {
                return false;
            }
        }
        return true;
    }
}
