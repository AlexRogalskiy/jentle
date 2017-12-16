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
package com.wildbeeslabs.jentle.algorithms.misc;

import com.wildbeeslabs.jentle.collections.map.CHashMapList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Custom miscellaneous implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CMisc {

    private CMisc() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static int findOpenNumber(final String fileName) throws FileNotFoundException {
        int rangeSize = (1 << 20);
        int[] blocks = getCountPerBlock(fileName, rangeSize);
        int blockIndex = findBlockWithMissing(blocks, rangeSize);
        if (blockIndex < 0) {
            return -1;
        }

        byte[] bitVector = getBitVectorForRange(fileName, blockIndex, rangeSize);
        int offset = findZero(bitVector);
        if (offset < 0) {
            return -1;
        }

        return blockIndex * rangeSize + offset;
    }

    private static int[] getCountPerBlock(final String fileName, int rangeSize) throws FileNotFoundException {
        int arraySize = Integer.MAX_VALUE / rangeSize + 1;
        int[] blocks = new int[arraySize];
        try (final Scanner in = new Scanner(new FileReader(fileName))) {
            while (in.hasNextInt()) {
                int value = in.nextInt();
                blocks[value / rangeSize]++;
            }
        }
        return blocks;
    }

    private static int findBlockWithMissing(int[] blocks, int rangeSize) {
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] < rangeSize) {
                return i;
            }
        }
        return -1;
    }

    private static byte[] getBitVectorForRange(final String fileName, int blockIndex, int rangeSize) throws FileNotFoundException {
        int startRange = blockIndex * rangeSize;
        int endRange = startRange + rangeSize;
        byte[] bitVector = new byte[rangeSize / Byte.SIZE];

        try (final Scanner in = new Scanner(new FileReader(fileName))) {
            while (in.hasNextInt()) {
                int value = in.nextInt();
                if (startRange <= value && value < endRange) {
                    int offset = value - startRange;
                    int mask = (1 << (offset % Byte.SIZE));
                    bitVector[offset / Byte.SIZE] |= mask;
                }
            }
        }
        return bitVector;
    }

    private static int findZero(byte b) {
        for (int i = 0; i < Byte.SIZE; i++) {
            int mask = 1 << i;
            if ((b & mask) == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int findZero(byte[] bitVector) {
        for (int i = 0; i < bitVector.length; i++) {
            if (bitVector[i] != ~0) {
                int bitIndex = findZero(bitVector[i]);
                return i * Byte.SIZE + bitIndex;
            }
        }
        return -1;
    }

    public static <T extends Point> T intersection(final T start1, final T end1, final T start2, final T end2) {
        if (start1.x > end1.x) {
            swap(start1, end1);
        }
        if (start2.x > end2.x) {
            swap(start2, end2);
        }
        if (start1.x > start2.x) {
            swap(start1, start2);
            swap(end1, end2);
        }
        final Line<T> line1 = new Line<>(start1, end1);
        final Line<T> line2 = new Line<>(start2, end2);
        if (line1.slope == line2.slope) {
            if (line1.yIntercept == line2.yIntercept && isBetween(start1, start2, end2)) {
                return start2;
            }
            return null;
        }
        double x = (line2.yIntercept - line1.yIntercept) / (line1.slope - line2.slope);
        double y = x * line1.slope + line1.yIntercept;
        final Point intersection = new Point(x, y);
        if (isBetween(start1, intersection, end1) && isBetween(start2, intersection, end2)) {
            return (T) intersection;
        }
        return null;
    }

    private static boolean isBetween(double start, double middle, double end) {
        if (start > end) {
            return end <= middle && middle <= start;
        } else {
            return start <= middle && middle <= end;
        }
    }

    private static <T extends Point> boolean isBetween(final T start, final T middle, final T end) {
        return isBetween(start.x, middle.x, end.x) && isBetween(start.y, middle.y, end.y);
    }

    private static <T extends Point> void swap(final T first, final T last) {
        double tempX = first.x;
        double tempY = first.y;
        first.setLocation(last.x, last.y);
        last.setLocation(tempX, tempY);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class Line<T extends Point> {

        public static double epsilon = 0.0001;
        public double slope;
        public double yIntercept;
        private boolean infinite_slope = false;

        public Line(final T start, final T end) {
            if (Math.abs(start.x - end.x) > Line.epsilon) {
                double deltaY = start.y - end.y;
                double deltaX = start.x - end.x;
                this.slope = deltaY / deltaX;
                this.yIntercept = (start.y - slope * start.x);
            } else {
                this.infinite_slope = true;
                this.yIntercept = start.x;
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
            if (isEquivalent(line.slope, this.slope) && isEquivalent(line.yIntercept, this.yIntercept) && (line.infinite_slope = this.infinite_slope)) {
                return true;
            }
            return false;
        }

        public boolean isEquivalent(double a, double b) {
            return (Math.abs(a - b) < Line.epsilon);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class Point {

        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void setLocation(final Point point2) {
            this.setLocation(point2.x, point2.y);
        }

        public void setLocation(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class LexNumbers {

        public static final String[] smalls = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        public static final String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        public static final String[] bigs = {"", "Thousand", "Million", "Billion"};
        public static final String hundred = "Hundred";
        public static final String negative = "Negative";
    }

    public static String convert(int num) {
        if (0 == num) {
            return LexNumbers.smalls[0];
        } else if (num < 0) {
            return LexNumbers.negative + StringUtils.EMPTY + convert(-1 * num);
        }
        final LinkedList<String> parts = new LinkedList<>();
        int chunkCount = 0;
        while (num > 0) {
            if (num % 1000 != 0) {
                StringBuilder chunk = new StringBuilder();
                chunk.append(convertChunk(num % 1000)).append(StringUtils.EMPTY).append(LexNumbers.bigs[chunkCount]);
                parts.addFirst(chunk.toString());
            }
            num /= 1000;
            chunkCount++;
        }
        return listToString(parts);
    }

    private static String convertChunk(int num) {
        final LinkedList<String> parts = new LinkedList<>();
        if (num >= 100) {
            parts.addLast(LexNumbers.smalls[num / 100]);
            parts.addLast(LexNumbers.hundred);
            num %= 100;
        }
        if (num >= 10 && num <= 19) {
            parts.addLast(LexNumbers.smalls[num]);
        } else if (num >= 20) {
            parts.addLast(LexNumbers.tens[num / 10]);
            num %= 10;
        }
        if (num >= 1 && num <= 9) {
            parts.addLast(LexNumbers.smalls[num]);
        }
        return listToString(parts);
    }

    private static String listToString(final LinkedList<String> parts) {
        final StringBuffer sb = new StringBuffer();
        while (parts.size() > 1) {
            sb.append(parts.pop());
            sb.append(StringUtils.EMPTY);
        }
        sb.append(parts.pop());
        return sb.toString();
    }

    public static <T extends Person> int maxAliveYear(final T[] persons, int min, int max) {
        int[] populationDeltas = getPopulationDeltas(persons, min, max);
        int maxAliveYear = getMaxAliveYear(populationDeltas);
        return maxAliveYear + min;
    }

    private static <T extends Person> int[] getPopulationDeltas(final T[] persons, int min, int max) {
        int[] populationDeltas = new int[max - min + 2];
        for (final T person : persons) {
            int birth = person.birth - min;
            populationDeltas[birth]++;
            int death = person.death - min;
            populationDeltas[death + 1]--;
        }
        return populationDeltas;
    }

    private static int getMaxAliveYear(int[] deltas) {
        int maxAliveYear = 0;
        int maxAlive = 0;
        int currentAlive = 0;
        for (int year = 0; year < deltas.length; year++) {
            currentAlive += deltas[year];
            if (currentAlive > maxAlive) {
                maxAliveYear = year;
                maxAlive = currentAlive;
            }
        }
        return maxAliveYear;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Person {

        public int birth;
        public int death;
    }

    public static Set<Integer> allLengths(int k, int shorter, int longer) {
        final Set<Integer> lengths = new HashSet<>();
        for (int i = 0; i <= k; i++) {
            int j = k - i;
            int length = i * shorter + j * longer;
            lengths.add(length);
        }
        return lengths;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
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
            return (T) new Point((this.left + this.right) / 2.0, (this.top + this.bottom) / 2.0);
        }

        public T extend(final T mid1, T mid2, double size) {
            double xdir = mid1.x < mid2.x ? -1 : 1;
            double ydir = mid1.y < mid2.x ? -1 : 1;
            if (mid1.x == mid2.x) {
                return (T) new Point(mid1.x, mid1.y + ydir * size / 2.0);
            }

            double slope = (mid1.y - mid2.y) / (mid1.x - mid2.x);
            @SuppressWarnings("UnusedAssignment")
            double x1 = 0;
            @SuppressWarnings("UnusedAssignment")
            double y1 = 0;
            if (Math.abs(slope) == 1) {
                x1 = mid1.x + xdir * size / 2.0;
                y1 = mid1.y + ydir * size / 2.0;
            } else if (Math.abs(slope) < 1) {
                x1 = mid1.x + xdir * size / 2.0;
                y1 = slope * (x1 - mid1.x) + mid1.y;
            } else {
                y1 = mid1.y + ydir * size / 2.0;
                x1 = (y1 - mid1.y) / slope + mid1.x;
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
                if (point.x < start.x || (point.x == start.x && point.y < start.y)) {
                    start = point;
                } else if (point.x > end.x || (point.x == end.x && point.y > end.y)) {
                    end = point;
                }
            }
            return new Line<>(start, end);
        }
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
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @ToString
    public static class Result {

        public int hits = 0;
        public int pseudoHits = 0;

        public String toFormatString() {
            return "( " + this.hits + ", " + this.pseudoHits + " )";
        }
    }

    private static int code(char c) {
        switch (c) {
            case 'B':
                return 0;
            case 'G':
                return 1;
            case 'R':
                return 2;
            case 'Y':
                return 3;
            default:
                return -1;
        }
    }

    public static Result estimate(final String guess, final String solution) {
        Objects.requireNonNull(guess);
        Objects.requireNonNull(solution);
        if (guess.length() != solution.length()) {
            return null;
        }
        final Result result = new Result();
        int[] frequencies = new int[4];
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == solution.charAt(i)) {
                result.hits++;
            } else {
                int code = code(solution.charAt(i));
                frequencies[code]++;
            }
        }
        for (int i = 0; i < guess.length(); i++) {
            int code = code(guess.charAt(i));
            if (code >= 0 && frequencies[code] > 0 && guess.charAt(i) != solution.charAt(i)) {
                result.pseudoHits++;
                frequencies[code]--;
            }
        }
        return result;
    }
}
