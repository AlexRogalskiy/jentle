/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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
package com.wildbeeslabs.jentle.algorithms.math.utils;

import com.wildbeeslabs.jentle.collections.map.CHashMapList;
import lombok.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Stream;

/**
 * Custom math algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public class CMathUtils {

    @FunctionalInterface
    public interface MathOperation {

        int operation(int a, int b);
    }

    final MathOperation addition = (a, b) -> a + b;

    final MathOperation subtraction = (a, b) -> a - b;

    final MathOperation multiplication = (a, b) -> a * b;

    final MathOperation division = (a, b) -> a / b;

    public static int fibonacci_1(int n) {
        return fibonacci_2(n, new int[n + 1]);
    }

    public static int fibonacci_2(int i, int[] memo) {
        assert (i >= 0);
        if (0 == i || 1 == i) {
            return i;
        }
        if (0 == memo[i]) {
            memo[i] = fibonacci_2(i - 1, memo) + fibonacci_2(i - 2, memo);
        }
        return memo[i];
    }

    //upward dynamic
    public static int fibonacci_3(int n) {
        assert (n >= 0);
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

    public static int fibonacci_4(int n) {
        assert (n >= 0);
        if (0 == n) {
            return 1;
        }
        int a = 0, b = 1;
        for (int i = 2; i < n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return (a + b);
    }

    public static boolean isArmstrong(int x) {
        int n = x;
        int d = digits(x);
        int y = 0, z = x;
        while (z > 0) {
            x = z % 10;
            y += pow(x, d);
            z /= 10;
        }
        if (y == n) {
            return true;
        }
        return false;
    }

    private static int pow(int n, int power) {
        assert power > 0;
        if (power == 1) {
            return n;
        }
        return n * pow(n, power - 1);
    }

    private static int digits(long n) {
        if (n < 10) {
            return 1;
        }
        return 1 + digits(n / 10);
    }

    public static boolean isPalindrome(int n) {
        int original = n;
        int reverse = 0;
        for (int i = 0; i <= n; i++) {
            int r = n % 10;
            n = n / 10;
            reverse = reverse * 10 + r;
        }
        return reverse == original;
    }

    public static int secondLargest(int[] array) {
        Objects.nonNull(array);
        int largest = 0, secondLargest = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > largest) {
                secondLargest = largest;
                largest = array[i];
            }
        }
        return secondLargest;
    }

    public static int smallestUnsignedNumberDividedBy1To20(int n) {
        assert n > 0;
        boolean flag = false;
        long calculations = 0;
        while (true) {
            flag = true;
            if (n % 19 != 0 || n % 17 != 0) {
                n++;
                continue;
            }
            for (int i = 11; i <= 20; i++) {
                calculations++;
                if (n % i != 0) {
                    flag = false;
                }
            }
            if (flag) {
                break;
            } else {
                n++;
            }
        }
        return n;
    }

    public static boolean isPrime(int n) {
        int i = (int) Math.sqrt(n);
        while (i > 0) {
            if (i == 1) {
                return true;
            }
            if (n % i == 0) {
                return false;
            }
            i--;
        }
        return true;
    }

    public static int gcd(int m, int n) {
        return gcd(m, n, -1);
    }

    private static int gcd(int m, int n, int d) {
        if (d == -1) {
            d = m > n ? n : m;
        }
        if (m % d == 0 && n % d == 0) {
            return d;
        }
        return gcd(m, n, d - 1);
    }

    public static int finonacci5(int n) {
        assert (n >= 0);
        final int[] fibs = {0, 1};
        final Stream<Integer> fibonacci = Stream.generate(() -> {
            int result = fibs[1];
            int fib3 = fibs[0] + fibs[1];
            fibs[0] = fibs[1];
            fibs[1] = fib3;
            return result;
        });
        return fibonacci.skip(n - 1).findFirst().get();
        //return fibonacci.limit(n).collect(Collectors.toList());
    }

    public static int factorial(int n) {
        assert (n >= 0);
        if (0 == n) {
            return 1;
        }
        int facNumber = 1;
        for (int i = 1; i < n; i++) {
            facNumber *= (i + 1);
        }
        return facNumber;
    }

    public static double factorialLog(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input argument n=%i (n > 0 for n!)", n));
        }
        double logSum = 0;
        for (int i = 2; i <= n; i++) {
            logSum += Math.log((double) i);
        }
        return logSum;
    }

    public static int gorner(final int[] array, int x) {
        Objects.requireNonNull(array);
        int k = 0, n = array.length - 1, y = array[n];
        while (k++ <= n) {
            y = y * x + array[n - k];
        }
        return y;
    }

    /**
     * Returns the <a href="http://mathworld.wolfram.com/HyperbolicCosine.html">
     * hyperbolic cosine</a> of x.
     *
     * @param x double value for which to find the hyperbolic cosine
     * @return hyperbolic cosine of x
     */
    public static double cosh(double x) {
        return (Math.exp(x) + Math.exp(-x)) / 2.0;
    }

    /**
     * Returns the <a href="http://mathworld.wolfram.com/HyperbolicSine.html">
     * hyperbolic sine</a> of x.
     *
     * @param x double value for which to find the hyperbolic sine
     * @return hyperbolic sine of x
     */
    public static double sinh(double x) {
        return (Math.exp(x) - Math.exp(-x)) / 2.0;
    }

    /**
     * Returns the natural <code>log</code> of the <a
     * href="http://mathworld.wolfram.com/BinomialCoefficient.html"> Binomial
     * Coefficient</a>, "<code>n choose k</code>", the number of
     * <code>k</code>-element subsets that can be selected from an
     * <code>n</code>-element set.
     *
     * <Strong>Preconditions</strong>:
     * <ul>
     * <li> <code>0 <= k <= n </code> (otherwise
     * <code>IllegalArgumentException</code> is thrown)</li>
     * </ul>
     *
     * @param n the size of the set
     * @param k the size of the subsets to be counted
     * @return <code>n choose k</code>
     * @throws IllegalArgumentException if preconditions are not met.
     */
    public static double binomialCoefficientLog(final int n, final int k) {
        if (n < k) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input arguments n=%i, k=%i (n >= k for binomial coefficient (n,k))", n, k));
        }
        if (n < 0) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input arguments n=%i, k=%i (n >= 0 for binomial coefficient (n,k))", n, k));
        }
        if ((n == k) || (k == 0)) {
            return 0;
        }
        if ((k == 1) || (k == n - 1)) {
            return Math.log((double) n);
        }

        double logSum = 0;
        // n!/k!
        for (int i = k + 1; i <= n; i++) {
            logSum += Math.log((double) i);
        }
        // divide by (n-k)!
        for (int i = 2; i <= n - k; i++) {
            logSum -= Math.log((double) i);
        }
        return logSum;
    }

    public static double hypot(double a, double b) {
        double r = 0.0;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        }
        return r;
    }

    public static int log2(int x) {
        int y, v;
        if (x <= 0) {
            throw new IllegalArgumentException(String.format("ERROR: invalid input arguments n=%i (n > 0)", x));
        }
        v = x;
        y = -1;
        while (v > 0) {
            v >>= 1;
            y++;
        }
        return y;
    }

    public double percentage(double obtained, double total) {
        return obtained * 100 / total;
    }

    public static long roundByToNearestHundred(double input) {
        long i = (long) Math.ceil(input);
        return ((i + 99) / 100) * 100;
    }

    public static Optional<Point> calculateIntersectionPoint(double m1, double b1, double m2, double b2) {
        if (Double.compare(m1, m2) == 0) {
            return Optional.empty();
        }
        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;

        return Optional.of(Point.of(x, y));
    }

    public static int levenshteinDistance(final CharSequence x, final CharSequence y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];
        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = LevenshteinDistance.min(dp[i - 1][j - 1] + LevenshteinDistance.costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    public double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
        //return Point2D.distance(x1, y1, x2, y2);
        //double ac = Math.abs(y2 - y1);
        //double cb = Math.abs(x2 - x1);
        //return Math.hypot(ac, cb);
    }

    public static <T extends Point> T intersection(final T start1, final T end1, final T start2, final T end2) {
        if (start1.getX() > end1.getX()) {
            swap(start1, end1);
        }
        if (start2.getX() > end2.getX()) {
            swap(start2, end2);
        }
        if (start1.getX() > start2.getX()) {
            swap(start1, start2);
            swap(end1, end2);
        }
        final Line<T> line1 = new Line<>(start1, end1);
        final Line<T> line2 = new Line<>(start2, end2);
        if (line1.getSlope() == line2.getSlope()) {
            if (line1.getYIntercept() == line2.getYIntercept() && isBetween(start1, start2, end2)) {
                return start2;
            }
            return null;
        }
        double x = (line2.getYIntercept() - line1.getYIntercept()) / (line1.getSlope() - line2.getSlope());
        double y = x * line1.getSlope() + line1.getYIntercept();
        final Point intersection = Point.of(x, y);
        if (isBetween(start1, intersection, end1) && isBetween(start2, intersection, end2)) {
            return (T) intersection;
        }
        return null;
    }

    private static boolean isBetween(double start, double middle, double end) {
        if (start > end) {
            return (end <= middle && middle <= start);
        }
        return (start <= middle && middle <= end);
    }

    private static <T extends Point> boolean isBetween(final T start, final T middle, final T end) {
        return (isBetween(start.getX(), middle.getX(), end.getX()) && isBetween(start.getY(), middle.getY(), end.getY()));
    }

    private static <T extends Point> void swap(final T first, final T last) {
        double tempX = first.getX();
        double tempY = first.getY();
        first.setLocation(last.getX(), last.getY());
        last.setLocation(tempX, tempY);
    }

    public static <T extends Point> Line<T> findBestLine(final T[] points) {
        final CHashMapList<Double, Line<T>> linesBySlope = getListOfLines(points);
        return getBestLine(linesBySlope);
    }

    private static <T extends Point> CHashMapList<Double, Line<T>> getListOfLines(final T[] points) {
        final CHashMapList<Double, Line<T>> linesBySlope = new CHashMapList<>();
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                final Line<T> line = new Line<>(points[i], points[j]);
                double key = Line.floorToNearestEpsilon(line.slope);
                linesBySlope.put(key, line);
            }
        }
        return linesBySlope;
    }

    private static <T extends Point> Line<T> getBestLine(final CHashMapList<Double, Line<T>> linesBySlope) {
        Line<T> bestLine = null;
        int bestCount = 0;
        final Set<Double> slopes = linesBySlope.keySet();
        for (final Double slope : slopes) {
            final List<Line<T>> lines = linesBySlope.get(slope);
            for (final Line<T> line : lines) {
                int count = countEquivalentLines(linesBySlope, line);
                if (count > bestCount) {
                    bestLine = line;
                    bestCount = count;
                }
            }
        }
        return bestLine;
    }

    private static <T extends Point> int countEquivalentLines(final CHashMapList<Double, Line<T>> linesBySlope, final Line<T> line) {
        double key = Line.floorToNearestEpsilon(line.slope);
        int count = countEquivalentLines(linesBySlope.get(key), line);
        count += countEquivalentLines(linesBySlope.get(key - Line.epsilon), line);
        count += countEquivalentLines(linesBySlope.get(key + Line.epsilon), line);
        return count;
    }

    private static <T extends Point> int countEquivalentLines(final List<Line<T>> lines, final Line<T> line) {
        if (Objects.isNull(lines)) {
            return 0;
        }
        int count = 0;
        count = lines.stream().filter((parallelLine) -> (parallelLine.isEquivalent(line))).map((item) -> 1).reduce(count, Integer::sum);
        return count;
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    @EqualsAndHashCode
    @ToString
    public static class Point {

        private double x;
        private double y;

        public void setLocation(final Point point2) {
            this.setLocation(point2.x, point2.y);
        }

        public void setLocation(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class Line<T extends Point> {

        private static final double epsilon = 0.0001;
        private double slope;
        private double yIntercept;
        private boolean infiniteSlope = false;

        public Line(final T start, final T end) {
            if (Math.abs(start.getX() - end.getY()) > Line.epsilon) {
                double deltaY = start.getY() - end.getY();
                double deltaX = start.getX() - end.getX();
                this.slope = deltaY / deltaX;
                this.yIntercept = (start.getY() - this.slope * start.getX());
            } else {
                this.infiniteSlope = true;
                this.yIntercept = start.getX();
            }
        }

        public void setLocation(final Line<T> line2) {
            this.slope = line2.slope;
            this.yIntercept = line2.yIntercept;
        }

        public static double floorToNearestEpsilon(double value) {
            int res = (int) (value / Line.epsilon);
            return ((double) res) * Line.epsilon;
        }

        public boolean isEquivalent(final Line<T> line) {
            return isEquivalent(line.slope, this.slope)
                && isEquivalent(line.yIntercept, this.yIntercept)
                && (line.infiniteSlope = this.infiniteSlope);
        }

        private boolean isEquivalent(double a, double b) {
            return (Math.abs(a - b) < Line.epsilon);
        }
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class Square<T extends Point> {

        private double left;
        private double right;
        private double top;
        private double bottom;
        private double size;

        public Square(double left, double top, double right, double bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public T middle() {
            return (T) Point.of((this.left + this.right) / 2.0, (this.top + this.bottom) / 2.0);
        }

        @SuppressWarnings("UnusedAssignment")
        public T extend(final T mid1, T mid2, double size) {
            double xdir = mid1.getX() < mid2.getX() ? -1 : 1;
            double ydir = mid1.getY() < mid2.getY() ? -1 : 1;
            if (mid1.getX() == mid2.getX()) {
                return (T) new Point(mid1.getX(), mid1.getY() + ydir * size / 2.0);
            }

            double slope = (mid1.getY() - mid2.getY()) / (mid1.getX() - mid2.getX());
            double x1 = 0;
            double y1 = 0;
            if (Math.abs(slope) == 1) {
                x1 = mid1.getX() + xdir * size / 2.0;
                y1 = mid1.getY() + ydir * size / 2.0;
            } else if (Math.abs(slope) < 1) {
                x1 = mid1.getX() + xdir * size / 2.0;
                y1 = slope * (x1 - mid1.getX()) + mid1.getY();
            } else {
                y1 = mid1.getY() + ydir * size / 2.0;
                x1 = (y1 - mid1.getY()) / slope + mid1.getX();
            }
            return (T) new Point(x1, y1);
        }

        public Line<T> cut(final Square<T> other) {
            final T p1 = extend(this.middle(), other.middle(), this.size);
            final T p2 = extend(this.middle(), other.middle(), -1 * this.size);
            final T p3 = extend(other.middle(), this.middle(), other.size);
            final T p4 = extend(other.middle(), this.middle(), -1 * other.size);

            T start = p1;
            T end = p1;
            final List<T> points = Arrays.asList(p2, p3, p4);
            for (final T point : points) {
                if (point.getX() < start.getX() || (point.getX() == start.getX() && point.getY() < start.getY())) {
                    start = point;
                } else if (point.getX() > end.getX() || (point.getX() == end.getX() && point.getY() > end.getY())) {
                    end = point;
                }
            }
            return new Line<>(start, end);
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Rectangle<T extends Point> {

        private final T topRight;
        private final T bottomLeft;

        public Rectangle(final T topRight, final T bottomLeft) {
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
        }

        public boolean isOverlapping(final Rectangle<T> other) {
            if (this.topRight.getY() < other.bottomLeft.getY() || this.bottomLeft.getY() > other.topRight.getY()) {
                return false;
            }
            if (this.topRight.getX() < other.bottomLeft.getX() || this.bottomLeft.getX() > other.topRight.getX()) {
                return false;
            }
            return true;
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class LevenshteinDistance {

        /**
         * time complexity: O(3^n)
         *
         * @param x
         * @param y
         * @return
         */
        public static int calculate(final String x, final String y) {
            if (x.isEmpty()) {
                return y.length();
            }

            if (y.isEmpty()) {
                return x.length();
            }

            int substitution = calculate(x.substring(1), y.substring(1))
                + costOfSubstitution(x.charAt(0), y.charAt(0));
            int insertion = calculate(x, y.substring(1)) + 1;
            int deletion = calculate(x.substring(1), y) + 1;

            return min(substitution, insertion, deletion);
        }

        public static int costOfSubstitution(char a, char b) {
            return a == b ? 0 : 1;
        }

        public static int min(int... numbers) {
            return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
        }
    }
}
