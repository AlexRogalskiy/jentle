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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

        public double slope;
        public double yIntercept;

        public Line(final T start, final T end) {
            double deltaY = end.y - start.y;
            double deltaX = end.x - start.x;
            this.slope = deltaY / deltaX;
            this.yIntercept = (end.y - slope * end.x);
        }

        public void setLocation(final Line<T> line2) {
            this.slope = line2.slope;
            this.yIntercept = line2.yIntercept;
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
}
