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
package com.wildbeeslabs.jentle.algorithms.number;

import com.wildbeeslabs.jentle.algorithms.bitwise.CBitwise;
import com.wildbeeslabs.jentle.algorithms.random.CRandom;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Custom numeric utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CNumericUtils {

    /**
     * Default two pi constance
     */
    private final static double TWO_PI = 2 * Math.PI;
    /**
     * Default three pi over two constant
     */
    private final static double THREE_PI_OVER_TWO = 3 * Math.PI / 2;
    /**
     * Default pi over two constant
     */
    private final static double PI_OVER_TWO = Math.PI / 2;
    /**
     * Default near delta error (precision)
     */
    public static final double NEAR_DELTA = .00001;
    /**
     * Default zero percentage constant
     */
    public static final double ZERO_PERCENTAGE = 0.0;
    /**
     * Default hundred percentage constant
     */
    public static final double HUNDRED_PERCENTAGE = 100.0;

    public static double round(double value, int places) {
        assert (places > 0);
        final BigDecimal bd = new BigDecimal(Double.toString(value));
        return bd.setScale(places, RoundingMode.HALF_UP).doubleValue();
        //Precision.round(PI, 3);
    }

    public static int numOfDigits(int value) {
        assert (value > 0);
        return (int) (Math.log10(value) + 1);
    }

    /**
     * @param x the int to test
     * @return true if x is a power of 2
     */
    public static boolean isPowerOfTwo(final int x) {
        return Integer.bitCount(x) == 1;
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
        return CNumericUtils.isApproxEqual(d1, d1, .001);
    }

    public static boolean isApproxEqual(double d1, double d2, double epsilon) {
        assert (epsilon > 0);
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
        return CRandom.generateRandomInt(0, 5);
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

    public static boolean isPrime2(int num) {
        int sqrt = (int) Math.sqrt(num);
        for (int range = 2; range <= sqrt; range++) {
            if (num % range == 0) {
                return false;
            }
        }
        return true;
    }

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
     * Calculates the Greatest Common Divisor (GCD) of the supplied ArrayUtils of
     * positive integer numbers.
     *
     * @param array the supplied ArrayUtils of positive integer numbers
     * @return GCD of the supplied ArrayUtils
     */
    public static final int gcd(final int[] array) {
        Objects.requireNonNull(array, "Array should not be null");
        if (array.length < 2) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input argument, ArrayUtils=(%s) has less than 2 elements", array));
        }
        int tmp = gcd(array[array.length - 1], array[array.length - 2]);
        for (int i = array.length - 3; i >= 0; i--) {
            if (array[i] < 0) {
                throw new IllegalArgumentException(String.format("ERROR: invalid input argument, ArrayUtils=(%s) has several numbers where one, at least, is negative.", array));
            }
            tmp = gcd(tmp, array[i]);
        }
        return tmp;
    }

    /**
     * Calculates the Least Common Multiplier (LCM) of the supplied ArrayUtils of
     * positive integer numbers.
     *
     * @param array the supplied ArrayUtils of positive integer numbers
     * @return LCM of the supplied ArrayUtils
     */
    public static final int lcm(final int[] array) {
        Objects.requireNonNull(array, "Array should not be null");
        if (array.length < 2) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input argument, ArrayUtils=(%s) has less than 2 elements", array));
        }
        int tmp = lcm(array[array.length - 1], array[array.length - 2]);
        for (int i = array.length - 3; i >= 0; i--) {
            if (array[i] <= 0) {
                throw new IllegalArgumentException(String.format("ERROR: invalid input argument, ArrayUtils=(%s) has several numbers where one, at least, is negative or zero.", array));
            }
            tmp = lcm(tmp, array[i]);
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

    public static int min(final int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    public static int max(final int... numbers) {
        return Arrays.stream(numbers).max().orElse(Integer.MIN_VALUE);
    }

    public static long getBiggerPrime(int m) {
        return BigInteger.probablePrime(CBitwise.getNumberOfBits(m) + 1, new Random()).longValue();
    }

    public static long getLowerPrime(long m) {
        return BigInteger.probablePrime(CBitwise.getNumberOfBits(m) - 1, new Random()).longValue();
    }

    /**
     * Normalizes an angle to be near an absolute angle. The normalized angle
     * will be in the range from 0 to 2*PI, where 2*PI itself is not included.
     * If the normalized angle is near to 0, PI/2, PI, 3*PI/2 or 2*PI, that
     * angle will be returned.
     *
     * @param angle the angle to normalize
     * @return the normalized angle that will be in the range of [0,2*PI[
     * @see #isNear(double, double)
     */
    public static double normalNearAbsoluteAngle(double angle) {
        angle = ((angle %= CNumericUtils.TWO_PI) >= 0) ? angle : (angle + CNumericUtils.TWO_PI);
        if (isNear(angle, Math.PI)) {
            return Math.PI;
        } else if (angle < Math.PI) {
            if (isNear(angle, 0)) {
                return 0;
            } else if (isNear(angle, CNumericUtils.PI_OVER_TWO)) {
                return CNumericUtils.PI_OVER_TWO;
            }
        } else {
            if (isNear(angle, CNumericUtils.THREE_PI_OVER_TWO)) {
                return CNumericUtils.THREE_PI_OVER_TWO;
            } else if (isNear(angle, CNumericUtils.TWO_PI)) {
                return 0;
            }
        }
        return angle;
    }

    /**
     * Tests if the two {@code double} values are near to each other.
     *
     * @param value1 the first double value
     * @param value2 the second double value
     * @return {@code true} if the two doubles are near to each other;
     * {@code false} otherwise.
     */
    public static boolean isNear(double value1, double value2) {
        return (Math.abs(value1 - value2) < CNumericUtils.NEAR_DELTA);
    }

    /**
     * Normalizes an angle to an absolute angle. The normalized angle will be in
     * the range from 0 to 360, where 360 itself is not included.
     *
     * @param angle the angle to normalize
     * @return the normalized angle that will be in the range of [0,360[
     */
    public static double normalAbsoluteAngleDegrees(double angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }

    /**
     * Normalizes an angle to a relative angle. The normalized angle will be in
     * the range from -PI to PI, where PI itself is not included.
     *
     * @param angle the angle to normalize
     * @return the normalized angle that will be in the range of [-PI,PI)
     */
    public static double normalRelativeAngle(double angle) {
        return (angle %= CNumericUtils.TWO_PI) >= 0 ? (angle < Math.PI) ? angle : (angle - CNumericUtils.TWO_PI) : (angle >= -Math.PI) ? angle : angle + CNumericUtils.TWO_PI;
    }

    /**
     * Normalize an angle in a 2*Math.PI wide interval around a center value.
     * This method has three main uses:
     * <ul>
     * <li>normalize an angle between 0 and 2&pi;:<br/>
     * <code>a = MathUtils.normalizeAngle(a, Math.PI);</code></li>
     * <li>normalize an angle between -&pi; and +&pi;<br/>
     * <code>a = MathUtils.normalizeAngle(a, 0.0);</code></li>
     * <li>compute the angle between two defining angular positions:<br>
     * <code>angle = MathUtils.normalizeAngle(end, start) - start;</code></li>
     * </ul>
     * Note that due to numerical accuracy and since &pi; cannot be represented
     * exactly, the result interval is <em>closed</em>, it cannot be half-closed
     * as would be more satisfactory in a purely mathematical view.
     *
     * @param a      angle to normalize
     * @param center center of the desired 2&pi; interval for the result
     * @return a-2k * Math.PI; with integer k and center-Math.pi; &lt;= a-2k *
     * Math.PI; &lt;= center+Math.PI;
     */
    public static double normalizeAngle(double a, double center) {
        return a - CNumericUtils.TWO_PI * Math.floor((a + Math.PI - center) / CNumericUtils.TWO_PI);
    }

    public static List<Integer> noNegative(final List<Integer> nums) {
        return nums.stream().filter(n -> n >= 0).collect(Collectors.toList());
    }

    public static List<Integer> noNegative2(final List<Integer> nums) {
        nums.removeIf(n -> n < 0);
        return nums;
    }

    public static List<Integer> doubling(final List<Integer> nums) {
        nums.replaceAll(n -> n * 2);
        return nums;
    }

    public static List<Integer> doubling2(final List<Integer> nums) {
        return nums.stream().map(n -> n * 2).collect(Collectors.toList());
    }

    public static Double averageDouble(final Stream<String> stream) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.averagingDouble(n -> Double.parseDouble(n)));
    }

    public static Double averageInt(final Stream<String> stream) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.averagingInt(n -> Integer.parseInt(n)));
    }

    public static Double averageLong(final Stream<String> stream) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.averagingLong(n -> Long.parseLong(n)));
    }

    public static Integer sumInt(final Stream<Integer> stream) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.summingInt(Integer::intValue));
    }

    public static Long sumParseLong(final Stream<String> stream) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.summingLong(n -> Long.parseLong(n)));
    }

    public static Long sumLong(final Stream<Long> stream) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.summingLong(n -> n));
    }

    public static Double sumDouble(final Stream<Double> stream) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.summingDouble(Double::doubleValue));
    }

    public static long counting(final Stream<String> stream) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.counting());
    }

    public static List<BigDecimal> generateBigDecimals(int limit) {
        return Stream.iterate(BigDecimal.ONE, bigDecimal -> bigDecimal.add(BigDecimal.ONE)).limit(limit).collect(Collectors.toList());
    }

    public static <T> LongSummaryStatistics countBy(final Stream<? extends T> stream, final ToLongFunction<? super T> mapper) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.summarizingLong(mapper));
    }

    public static <T> IntSummaryStatistics countBy(final Stream<? extends T> stream, final ToIntFunction<? super T> mapper) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.summarizingInt(mapper));
    }

    public static <T> DoubleSummaryStatistics countBy(final Stream<? extends T> stream, final ToDoubleFunction<? super T> mapper) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.collect(Collectors.summarizingDouble(mapper));
    }

    public static Double reduce(final DoubleStream stream, final DoubleBinaryOperator operator) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.reduce(operator).orElseGet(null);
    }

    public static Integer reduce(final IntStream stream, final IntBinaryOperator operator) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.reduce(operator).orElseGet(null);
    }

    public static Long reduce(final LongStream stream, final LongBinaryOperator operator) {
        Objects.requireNonNull(stream, "Stream should not be null");
        return stream.reduce(operator).orElseGet(null);
    }

    public static double calculatePercentage(Double firstValue, Double lastValue) {
        if (Objects.nonNull(firstValue) && Objects.nonNull(lastValue)) {
            return ((lastValue - firstValue) / firstValue) * HUNDRED_PERCENTAGE;
        }
        return ZERO_PERCENTAGE;
    }

    public static boolean inRange(Double value, Double lowerBound, Double upperBound) {
        Objects.requireNonNull(lowerBound, "Lower bound should not be null");
        Objects.requireNonNull(upperBound, "Upper bound should not be null");

        final Range<Double> range = Range.between(lowerBound, upperBound);
        return range.contains(value);
    }

    public static Integer compare(Integer x, Integer y) {
        if (Objects.isNull(x) || Objects.isNull(y)) {
            return null;
        }
        return Integer.compare(x, y);
    }

    public static Integer compare(Double x, Double y) {
        if (Objects.isNull(x) || Objects.isNull(y)) {
            return null;
        }
        return Double.compare(x, y);
    }

    public static int getFactorsOf(int i, int factor) {
        assert factor > 0 : "Should be greater than zero";
        int count = 0;
        while (i % factor == 0) {
            count++;
            i /= factor;
        }
        return count;
    }

    public static int nullToZero(final Integer value) {
        return Optional.ofNullable(value).orElse(0);
    }

    public static long nullToZero(final Long value) {
        return Optional.ofNullable(value).orElse(0L);
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Averager implements IntConsumer {
        private int total;
        private int count;

        public double average() {
            return this.count > 0 ? ((double) this.total) / this.count : 0;
        }

        public void accept(int i) {
            this.total += i;
            this.count++;
        }

        public void combine(@NonNull final Averager other) {
            this.total += other.total;
            this.count += other.count;
        }
    }

    public static String bytesToHex(final byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for (final byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static List<Integer> asList(final int... ints) {
        return Arrays.stream(ints).boxed().collect(Collectors.toList());
    }

    public static boolean doubleIsDifferent(double d1, double d2, double delta) {
        if (Double.compare(d1, d2) == 0) {
            return false;
        }
        if ((Math.abs(d1 - d2) <= delta)) {
            return false;
        }

        return true;
    }

    public static boolean floatIsDifferent(float f1, float f2, float delta) {
        if (Float.compare(f1, f2) == 0) {
            return false;
        }
        if ((Math.abs(f1 - f2) <= delta)) {
            return false;
        }

        return true;
    }

    public static OptionalInt parse(final Integer... values) {
        return Arrays.asList(values).stream().mapToInt(Integer::intValue).max();
    }

    public static Integer parse(final Predicate<Integer> predicate, final Integer... values) {
        return Arrays.asList(values).stream().filter(predicate).mapToInt(Integer::intValue).sum();
    }

    public static boolean isPerfectSquareDurron(long n) {
        if (n < 0) return false;
        if (n == 0) return true;

        long x = n;
        // This is faster because a number is divisible by 16 only 6% of the time
        // and more than that a vanishingly small percentage.
        while ((x & 0x3) == 0) x >>= 2;
        // This is effectively the same as the switch-case statement used in the original
        // answer.
        if ((x & 0x7) == 1) {

            long sqrt;
            if (x < 410881L) {
                int i;
                float x2, y;

                x2 = x * 0.5F;
                y = x;
                i = Float.floatToRawIntBits(y);
                i = 0x5f3759df - (i >> 1);
                y = Float.intBitsToFloat(i);
                y = y * (1.5F - (x2 * y * y));

                sqrt = (long) (1.0F / y);
            } else {
                sqrt = (long) Math.sqrt(x);
            }
            return sqrt * sqrt == x;
        }
        return false;
    }

    public static boolean isPerfectSquareDurronTwo(long n) {
        if (n < 0) return false;
        // Needed to prevent infinite loop
        if (n == 0) return true;

        long x = n;
        while ((x & 0x3) == 0) x >>= 2;
        if ((x & 0x7) == 1) {
            long sqrt;
            if (x < 41529141369L) {
                int i;
                float x2, y;

                x2 = x * 0.5F;
                y = x;
                i = Float.floatToRawIntBits(y);
                //using the magic number from
                //http://www.lomont.org/Math/Papers/2003/InvSqrt.pdf
                //since it more accurate
                i = 0x5f375a86 - (i >> 1);
                y = Float.intBitsToFloat(i);
                y = y * (1.5F - (x2 * y * y));
                y = y * (1.5F - (x2 * y * y)); //Newton iteration, more accurate
                sqrt = (long) ((1.0F / y) + 0.2);
            } else {
                //Carmack hack gives incorrect answer for n >= 41529141369.
                sqrt = (long) Math.sqrt(x);
            }
            return sqrt * sqrt == x;
        }
        return false;
    }

    /**
     * Returns an enum value for the given id, or the default value if null is passed or there are no enum values with
     * the given id.
     *
     * @return enum value
     */
    public static <T extends Enum<T>> T fromNameOrDefault(final Class<T> e, final String name, final T defaultValue) {
        if (Objects.isNull(name)) {
            return defaultValue;
        }
        return Arrays.stream(e.getEnumConstants())
            .filter(enumConstant -> enumConstant.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(defaultValue);
    }

    public static <T extends Enum<T>> T random(final Class<T> clazz) {
        Objects.requireNonNull(clazz, "Class should not be null");
        return random(clazz.getEnumConstants());
    }

    public static <T> T random(final T[] values) {
        if (ArrayUtils.isNotEmpty(values)) {
            return values[new Random().nextInt(values.length)];
        }
        return null;
    }
}
