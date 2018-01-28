/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.utils;

import com.wildbeeslabs.jentle.algorithms.bitwise.CBitwise;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * Custom numeric utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CNumericUtils {

    private CNumericUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static long generateRandomLong() {
        return new Random().nextLong();
        //new RandomDataGenerator().getRandomGenerator().nextLong();
    }

    public static int generateRandomInt() {
        return new Random().nextInt();
        //new RandomDataGenerator().getRandomGenerator().nextInt();
    }

    public static float generateRandomFloat() {
        return new Random().nextFloat();
        //new RandomDataGenerator().getRandomGenerator().nextFloat();
    }

    public static double generateRandomDouble() {
        return new Random().nextDouble();
        //new RandomDataGenerator().getRandomGenerator().nextDouble();
    }

    public static long generateRandomLong(long bottomLimit, long upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + (long) (Math.random() * (upLimit - bottomLimit)));
        //new RandomDataGenerator().nextLong(bottomLimit, upLimit);
    }

    public static int generateRandomInt(int bottomLimit, int upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + (int) (new Random().nextFloat() * (upLimit - bottomLimit)));
        //new RandomDataGenerator().nextInt(bottomLimit, upLimit);
    }

    public static float generateRandomFloat(float bottomLimit, float upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + new Random().nextFloat() * (upLimit - bottomLimit));
        //float randomFloat = new RandomDataGenerator().getRandomGenerator().nextFloat();
        //bottomLimit + randomFloat * (upLimit - bottomLimit);
    }

    public static double generateRandomDouble(double bottomLimit, double upLimit) {
        assert (bottomLimit <= upLimit);
        return (bottomLimit + new Random().nextDouble() * (upLimit - bottomLimit));
        //new RandomDataGenerator().nextUniform(bottomLimit, upLimit);
    }

    public static boolean generateRandomBoolean() {
        return new Random().nextBoolean();
        //new RandomDataGenerator().getRandomGenerator().nextBoolean();
    }

    public static double round(double value, int places) {
        assert (places > 0);
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
        //Precision.round(PI, 3);
    }

    public static int numOfDigits(int value) {
        assert (value > 0);
        return (int) (Math.log10(value) + 1);
    }

    public static boolean hasPrimeNumbers(int n) {
        assert (n >= 0);
        int sqrt = (int) Math.sqrt(n);
        for (int i = 2; i <= sqrt; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> sieveOfEratosthenes(int n) {
        assert (n >= 0);
        boolean prime[] = new boolean[n + 1];
        Arrays.fill(prime, true);
        for (int p = 2; p * p <= n; p++) {
            if (prime[p]) {
                for (int i = p * 2; i <= n; i += p) {
                    prime[i] = false;
                }
            }
        }
        final List<Integer> primeNumbers = new LinkedList<>();
        for (int i = 2; i <= n; i++) {
            if (prime[i]) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }

    public static List<Integer> primeNumbersTill(int n) {
        assert (n >= 0);
        return IntStream.rangeClosed(2, n)
                .filter(x -> isPrime(x)).boxed()
                .collect(Collectors.toList());
    }

    private static boolean isPrime(int n) {
        return IntStream.rangeClosed(2, (int) (Math.sqrt(n)))
                .filter(i -> (i & 0X1) != 0)
                .allMatch(i -> n % i != 0);
    }

    public static int negate(int num) {
        int neg = 0;
        int newSign = num < 0 ? 1 : -1;
        while (num != 0) {
            neg += newSign;
            num += newSign;
        }
        return neg;
    }

    public static int negate2(int num) {
        int neg = 0;
        int newSign = num < 0 ? 1 : -1;
        int delta = newSign;
        while (num != 0) {
            boolean diffSigns = (num + delta > 0) != (num > 0);
            if (num + delta != 0 && diffSigns) {
                delta = newSign;
            }
            neg += delta;
            num += delta;
            delta += delta;
        }
        return neg;
    }

    public static int minus(int a, int b) {
        return a + negate(b);
    }

    public static int multiply(int a, int b) {
        if (a < b) {
            return multiply(b, a);
        }
        int sum = 0;
        for (int i = abs(b); i > 0; i = minus(i, 1)) {
            sum += a;
        }
        if (b < 0) {
            sum = negate(sum);
        }
        return sum;
    }

    private static int abs(int a) {
        if (a < 0) {
            return negate(a);
        }
        return a;
    }

    public static int power(int a, int n) {
        assert (n > 0);
        int b = 1, c = a;
        while (n > 0) {
            if (n % 2 == 0) {
                n /= 2;
                c *= c;
            } else {
                n--;
                b *= c;
            }
        }
        return b;
    }

    public static boolean isApproxEqual(double d1, double d2) {
        double epsilon = .001;
        if (Math.abs(d1 - d2) < epsilon) {
            return true;
        }
        return false;
    }

    public static int divide(int a, int b) throws java.lang.ArithmeticException {
        if (0 == b) {
            throw new java.lang.ArithmeticException("ERROR: divider cannot be null");
        }
        int absa = abs(a);
        int absb = abs(b);
        int product = 0;
        int x = 0;
        while (product + absb <= absa) {
            product += absb;
            x++;
        }
        if ((a < 0 && b < 0) || (a > 0 && b > 0)) {
            return x;
        }
        return negate(x);
    }

    public static int rand7() {
        while (true) {
            int r1 = 2 * rand5();
            int r2 = rand5();
            if (r2 != 4) {
                int rand1 = r2 % 2;
                int num = r1 + rand1;
                if (num < 7) {
                    return num;
                }
            }
        }
    }

    public static int rand72() {
        while (true) {
            int num = 5 * rand5() + rand5();
            if (num < 21) {
                return num % 7;
            }
        }
    }

    public static int rand5() {
        return CNumericUtils.generateRandomInt(0, 5);
    }

    public static int add(int a, int b) {
        while (b != 0) {
            int sum = a ^ b;
            int carry = (a & b) << 1;
            a = sum;
            b = carry;
        }
        return a;
    }

    private static int count2sInRangeDigit(int number, int d) {
        int powerOf10 = (int) Math.pow(10, d);
        int nextPowerOf10 = powerOf10 * 10;
        int right = number % powerOf10;
        int roundDown = number - number % nextPowerOf10;
        int roundUp = roundDown + nextPowerOf10;

        int digit = (number / powerOf10) % 10;
        if (digit < 2) {
            return roundDown / 10;
        } else if (digit == 2) {
            return roundDown / 10 + right + 1;
        }
        return roundUp / 10;
    }

    public static int count2sInRange(int number) {
        int count = 0;
        int len = String.valueOf(number).length();
        for (int digit = 0; digit < len; digit++) {
            count += count2sInRangeDigit(number, digit);
        }
        return count;
    }

    public static int getKthMagicNumber(int k) {
        if (k < 0) {
            return 0;
        }
        int val = 0;
        final Queue<Integer> queue3 = new LinkedList<>();
        final Queue<Integer> queue5 = new LinkedList<>();
        final Queue<Integer> queue7 = new LinkedList<>();
        queue3.add(1);
        for (int i = 0; i <= k; i++) {
            int v3 = queue3.size() > 0 ? queue3.peek() : Integer.MAX_VALUE;
            int v5 = queue5.size() > 0 ? queue5.peek() : Integer.MAX_VALUE;
            int v7 = queue7.size() > 0 ? queue7.peek() : Integer.MAX_VALUE;
            val = Collections.min(Arrays.asList(v3, v5, v7));
            if (val == v3) {
                queue3.remove();
                queue3.add(3 * val);
                queue5.add(5 * val);
            } else if (val == v5) {
                queue5.remove();
                queue5.add(5 * val);
            } else if (val == v7) {
                queue7.remove();
            }
            queue7.add(7 * val);
        }
        return val;
    }

    public static List<Integer> generatePrimeNumbers(int maxNumber) {
        final List<Integer> result = new ArrayList<>();
        boolean[] isPrime = new boolean[maxNumber + 1];
        Arrays.fill(isPrime, true);
        int primeRange = (int) Math.sqrt(maxNumber);
        for (int num = 2; num <= primeRange; num++) {
            if (isPrime[num]) {
                for (int iEliminate = num * num; iEliminate <= maxNumber; iEliminate += num) {
                    isPrime[iEliminate] = false;
                }
            }
        }
        int num = 2;
        while (num <= maxNumber) {
            if (isPrime[num]) {
                result.add(num);
            }
            num++;
        }
        return result;
    }

//    public static boolean isPrime(int num) {
//        int sqrt = (int) Math.sqrt(num);
//        for (int range = 2; range <= sqrt; range++) {
//            if (num % range == 0) {
//                return false;
//            }
//        }
//        return true;
//    }
    public static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    public static int gcd(int a, int b) {
        int temp = 0;
        while (b != 0) {
            temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static int gcd2(int a, int b) {
        while (a != b) {
            if (a > b) {
                a -= b;
            } else {
                b -= a;
            }
        }
        return a;
    }

    /**
     * Calculates the Greatest Common Divisor (GCD) of the supplied array of
     * positive integer numbers.
     *
     * @param array the supplied array of positive integer numbers
     * @return GCD of the supplied array
     */
    public static final int gcd(int[] array) {
        if (array.length < 2) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input argument, array=(%s) has less than 2 elements", array));
        }
        int tmp = gcd(array[array.length - 1], array[array.length - 2]);
        for (int i = array.length - 3; i >= 0; i--) {
            if (array[i] < 0) {
                throw new IllegalArgumentException(String.format("ERROR: invalid input argument, array=(%s) has several numbers where one, at least, is negative.", array));
            }
            tmp = gcd(tmp, array[i]);
        }
        return tmp;
    }

    public static boolean isPalindrome(int num) {
        if (num == reverse(num)) {
            return true;
        }
        return false;
    }

    public static int[] swap(int x, int y) {
        x = x ^ y;
        y = x ^ y;
        x = x ^ y;
        return new int[]{x, y};
    }

    private static int[] swap2(int x, int y) {
        x = x + y;
        y = x - y;
        x = x - y;
        return new int[]{x, y};
    }

    private static int[] swap3(int x, int y) {
        x = x * y;
        y = x / y;
        x = x / y;
        return new int[]{x, y};
    }

    public static int reverse(int num) {
        int reverseNum = 0;
        int digit = 0;
        while (num > 0) {
            digit = num % 10;
            reverseNum = reverseNum * 10 + digit;
            num /= 10;
        }
        return reverseNum;
    }

    public static List<Integer> convertIntToSet(int x) {
        final List<Integer> subset = new LinkedList<>();
        for (int k = x; k > 0; k >>= 1) {
            subset.add(k & 1);
        }
        Collections.reverse(subset);
        return subset;
    }

    public static String toFullBinaryString(int a) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < Integer.BYTES * Byte.SIZE; i++) {
            sb.append(a & 1);
            a = a >> 1;
        }
        return sb.toString();
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    public static int max(int... numbers) {
        return Arrays.stream(numbers).max().orElse(Integer.MIN_VALUE);
    }

    public static long getBiggerPrime(int m) {
        final BigInteger prime = BigInteger.probablePrime(CBitwise.getNumberOfBits(m) + 1, new Random());
        return prime.longValue();
    }

    public static long getLowerPrime(long number) {
        final BigInteger prime = BigInteger.probablePrime(CBitwise.getNumberOfBits(number) - 1, new Random());
        return prime.longValue();
    }
}
