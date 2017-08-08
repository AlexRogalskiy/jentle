package com.wildbeeslabs.jentle.algorithms.calc;

/**
 *
 * Custom calculation implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CCalc {

    // downward dynamic
    public static int fibonacci1(int n) {
        return fibonacci2(n, new int[n + 1]);
    }

    public static int fibonacci2(int i, int[] memo) {
        if (0 == i || 1 == i) {
            return i;
        }
        if (0 == memo[i]) {
            memo[i] = fibonacci2(i - 1, memo) + fibonacci2(i - 2, memo);
        }
        return memo[i];
    }

    //upward dynamic
    public static int fibonacci3(int n) {
        if (0 == n) {
            return 0;
        } else if (1 == n) {
            return 1;
        }

        int[] memo = new int[n];
        memo[0] = 0;
        memo[1] = 1;
        for (int i = 2; i < n; i++) {
            memo[i] = memo[i - 1] + memo[i - 2];
        }
        return memo[n - 1] + memo[n - 2];
    }

    public static int fibonacci4(int n) {
        if (0 == n) {
            return 0;
        }
        int a = 0, b = 1;
        for (int i = 2; i < n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return (a + b);
    }
}
