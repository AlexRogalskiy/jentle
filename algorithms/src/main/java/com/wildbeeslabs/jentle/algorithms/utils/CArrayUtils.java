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

import com.wildbeeslabs.jentle.algorithms.random.CRandom;
import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.IntStream;

import org.apache.commons.math3.util.Pair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * Custom array utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CArrayUtils {

    /**
     * Default logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CArrayUtils.class);

    private CArrayUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static <T> List<T> union(final T[] array1, final T[] array2, final Comparator<? super T> cmp) {
        final List<T> result = new ArrayList<>();
        int length1 = array1.length;
        int length2 = array2.length;

        int index1 = 0, index2 = 0;
        while (index1 < length1 && index2 < length2) {
            if (Objects.compare(array1[index1], array2[index2], cmp) < 0) {
                result.add(array1[index1]);
                index1++;

            } else if (Objects.compare(array1[index1], array2[index2], cmp) > 0) {
                result.add(array2[index2]);
                index2++;
            } else {
                result.add(array1[index1]);
                index1++;
                index2++;
            }
        }
        while (index1 < length1) {
            result.add(array1[index1++]);
        }
        while (index2 < length2) {
            result.add(array2[index2++]);
        }
        return result;
    }

    public static <T> void merge(final T[] first, final T[] second, int lastFirst, int lastSecond, final Comparator<? super T> cmp) {
        Objects.requireNonNull(first);
        Objects.requireNonNull(second);
        assert (lastFirst >= 0 && lastSecond >= 0 && lastFirst < first.length && lastSecond < second.length);
        int indexF = lastFirst - 1;
        int indexS = lastSecond - 1;
        int indexMerged = lastSecond + lastFirst - 1;

        while (indexS >= 0) {
            if (indexF >= 0 && Objects.compare(first[indexF], second[indexS], cmp) > 0) {
                first[indexMerged] = first[indexF];
                indexF--;
            } else {
                first[indexMerged] = second[indexS];
                indexS--;
            }
            indexMerged--;
        }
    }

    public static <T> List<T> intersect(final T[] array1, final T[] array2, final Comparator<? super T> cmp) {
        final List<T> result = new ArrayList<>();
        int length1 = array1.length;
        int length2 = array2.length;
        int index1 = 0, index2 = 0;
        while (index1 < length1 && index2 < length2) {
            if (Objects.compare(array1[index1], array2[index2], cmp) < 0) {
                index1++;
            } else if (Objects.compare(array1[index1], array2[index2], cmp) > 0) {
                index2++;
            } else {
                result.add(array1[index1]);
                index1++;
                index2++;
            }
        }
        return result;
    }

    public static <T extends Comparable<? super T>> int getMinIndex(final T[] array) {
        return getMinIndex(array, CUtils.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> int getMinIndex(final T[] array, final Comparator<? super T> cmp) {
        int minIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (Objects.compare(array[i], array[minIndex], cmp) < 0) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static <T extends Comparable<? super T>> int getMaxIndex(final T[] array) {
        return getMaxIndex(array, CUtils.<T>getDefaultSortComparator());
    }

    public static <T extends Comparable<? super T>> int getMaxIndex(final T[] array, final Comparator<? super T> cmp) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (Objects.compare(array[i], array[maxIndex], cmp) > 0) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static <T> void swap(final T[] array, int m, int n) {
        final T temp = array[m];
        array[m] = array[n];
        array[n] = temp;
    }

    public static <T> void swap2(final int[] array, int m, int n) {
        array[m] = array[m] + array[n];
        array[n] = array[m] - array[n];
        array[m] = array[m] - array[n];
    }

    @FunctionalInterface
    public static interface ShortToByteFunction {

        byte applyAsByte(short s);
    }

    //byte[] transformedArray = transformArray(array, s -> (byte) (s * 2));
    public byte[] transformArray(final short[] array, final ShortToByteFunction function) {
        byte[] transformedArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            transformedArray[i] = function.applyAsByte(array[i]);
        }
        return transformedArray;
    }

    public static int[] squaresOf(final int[] array) {
        return IntStream.range(0, array.length).map((i) -> i * i).toArray();
    }

    public static int[] squaresOf(final int num) {
        assert (num > 0);
        final int[] res = new int[num];
        int k = 0;
        res[k] = 0;
        while (k++ <= num) {
            res[k] = res[k - 1] + k + k - 1;
        }
        return res;
    }

    public static <T> boolean isArray2SubArrayOfArray1(final T[] array1, final T[] array2, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array1);
        Objects.requireNonNull(array2);
        int len1 = array1.length - 1, len2 = array2.length - 1;
        while (len1 >= 0 && len2 >= 0) {
            if (Objects.compare(array1[len1], array2[len2], cmp) == 0) {
                len1--;
                len2--;
            } else {
                len1--;
            }
        }
        if (len2 == 0) {
            return true;
        } else if (len2 != 0 && len1 == 0) {
            return false;
        }
        return false;
    }

    public static <T> T getNthMax(int n, final List<T> list, final Comparator<? super T> cmp) {
        assert (n > 0);
        return list.stream().sorted(cmp).limit(n).skip(n - 1).findFirst().get();
    }

    public static <T> void split(final T[] array, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        split(array, value, 0, array.length, cmp);
    }

    public static <T> void split(final T[] array, final T value, int low, int high, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        assert (low >= 0 && low < array.length);
        assert (high >= 0 && high < array.length);
        assert (low <= high);
        int l = low, h = high;
        while (l <= h) {
            if (Objects.compare(array[l], value, cmp) <= 0) {
                l++;
            } else if (Objects.compare(array[h], value, cmp) > 0) {
                h--;
            } else {
                swap(array, l, h);
            }
        }
    }

    public static <T> void swapBy(final T[] array, int m) {
        Objects.requireNonNull(array);
        assert (m > 0 && m < array.length);
        int p = 0, q = m, r = array.length - 1;
        while (p < q && q < r) {
            if ((q - p + 1) <= (r - q)) {
                for (int i = p; i < q - p + 1; i++) {
                    swap(array, i, i + q - p + 1);
                }
                int pnew = q;
                int qnew = q - p + 1;
                p = pnew;
                q = qnew;
            } else {
                for (int i = q - (r - q) + 1; i < r - (r - q) + 1; i++) {
                    swap(array, i, i + r - q);
                }
                int qnew = q - (r - q);
                int rnew = q;
                q = qnew;
                r = rnew;
            }
        }
    }

    public static <T> void reverse(final T[] array) {
        Objects.requireNonNull(array);
        int len = array.length;
        for (int i = 0; i < len / 2; i++) {
            swap(array, i, len - 1 - i);
        }
    }

    public static int getMagic(int[] array) {
        Objects.requireNonNull(array);
        return getMagic(array, 0, array.length - 1);
    }

    public static int getMagic(int[] array, int start, int end) {
        if (end < start) {
            return -1;
        }
        int midIndex = (start + end) / 2;
        int midValue = array[midIndex];
        if (midValue == midIndex) {
            return midIndex;
        }

        int leftIndex = Math.min(midIndex - 1, midValue);
        int left = getMagic(array, start, leftIndex);
        if (left >= 0) {
            return left;
        }
        int rightIndex = Math.max(midIndex + 1, midValue);
        int right = getMagic(array, rightIndex, end);
        return right;
    }

    public static int findMinDiff(final int[] array1, final int[] array2) {
        Objects.requireNonNull(array1);
        Objects.requireNonNull(array2);
        Arrays.sort(array1);
        Arrays.sort(array2);
        int a = 0;
        int b = 0;
        int difference = Integer.MAX_VALUE;
        while (a < array1.length && b < array2.length) {
            if (Math.abs(array1[a] - array2[b]) < difference) {
                difference = Math.abs(array1[a] - array2[b]);
            }
            if (array1[a] < array2[b]) {
                a++;
            } else {
                b++;
            }
        }
        return difference;
    }

    public static <T> Result findUnsortedSequence(final T[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int endLeft = findEndOfLeftSubsequence(array, cmp);
        if (endLeft >= array.length - 1) {
            return null;
        }
        int startRight = findStartOfRightSubsequence(array, cmp);
        int maxIndex = endLeft;
        int minIndex = startRight;
        for (int i = endLeft + 1; i < startRight; i++) {
            if (Objects.compare(array[i], array[minIndex], cmp) < 0) {
                minIndex = i;
            }
            if (Objects.compare(array[i], array[maxIndex], cmp) > 0) {
                maxIndex = i;
            }
        }
        int leftIndex = shrinkLeft(array, minIndex, endLeft, cmp);
        int rightIndex = shrinkRight(array, maxIndex, startRight, cmp);
        return new Result(leftIndex, rightIndex);
    }

    private static <T> int findEndOfLeftSubsequence(final T[] array, final Comparator<? super T> cmp) {
        for (int i = 1; i < array.length; i++) {
            if (Objects.compare(array[i], array[i - 1], cmp) < 0) {
                return i - 1;
            }
        }
        return array.length - 1;
    }

    private static <T> int findStartOfRightSubsequence(final T[] array, final Comparator<? super T> cmp) {
        for (int i = array.length - 2; i >= 0; i--) {
            if (Objects.compare(array[i], array[i + 1], cmp) > 0) {
                return i + 1;
            }
        }
        return 0;
    }

    private static <T> int shrinkLeft(final T[] array, int minIndex, int start, final Comparator<? super T> cmp) {
        final T comp = array[minIndex];
        for (int i = start - 1; i >= 0; i--) {
            if (Objects.compare(array[i], comp, cmp) <= 0) {
                return i + 1;
            }
        }
        return 0;
    }

    private static <T> int shrinkRight(final T[] array, int maxIndex, int start, final Comparator<? super T> cmp) {
        final T comp = array[maxIndex];
        for (int i = start; i < array.length; i++) {
            if (Objects.compare(array[i], comp, cmp) >= 0) {
                return i - 1;
            }
        }
        return array.length - 1;
    }

    @Data
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Result {

        public int leftIndex = 0;
        public int rightIndex = 0;

        public String toFormatString() {
            return "( " + this.leftIndex + ", " + this.rightIndex + " )";
        }
    }

    public static int getMaxSum(int[] array) {
        int maxSum = 0;
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
            if (maxSum < sum) {
                maxSum = sum;
            } else if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }

    public static int[] findSwapValues(int[] array1, int[] array2) {
        Objects.requireNonNull(array1);
        Objects.requireNonNull(array2);
        final Integer target = getTarget(array1, array2);
        if (Objects.isNull(target)) {
            return null;
        }
        return findDifference(array1, array2, target);
    }

    private static Integer getTarget(int[] array1, int[] array2) {
        int sum1 = Arrays.stream(array1).sum();
        int sum2 = Arrays.stream(array2).sum();
        if ((sum1 - sum2) % 2 != 0) {
            return null;
        }
        return (sum1 - sum2) / 2;
    }

    private static int[] findDifference(int[] array1, int[] array2, int target) {
        final Set<Integer> content = getContents(array2);
        for (int one : array1) {
            int two = one - target;
            if (content.contains(two)) {
                return new int[]{one, two};
            }
        }
        return null;
    }

    private static int[] findDifference2(int[] array1, int[] array2, int target) {
        int a = 0;
        int b = 0;
        while (a < array1.length && b < array2.length) {
            int diff = array1[a] - array2[b];
            if (diff == target) {
                return new int[]{array1[a], array2[b]};
            } else if (diff < target) {
                a++;
            } else {
                b++;
            }
        }
        return null;
    }

    private static Set<Integer> getContents(int[] array) {
        final Set<Integer> set = new HashSet<>();
        for (int a : array) {
            set.add(a);
        }
        return set;
    }

    public static List<Pair<Integer, Integer>> getPairSum(int[] array, int sum) {
        Objects.requireNonNull(array);
        final List<Pair<Integer, Integer>> result = new ArrayList<>();
        final Set<Integer> elements = new HashSet<>();
        for (int x : array) {
            int complement = sum - x;
            if (elements.contains(complement) && !elements.contains(x)) {
                result.add(new Pair<>(x, complement));
            }
            elements.add(x);
        }
        return result;
    }

    public static List<Pair<Integer, Integer>> getPairSum2(int[] array, int sum) {
        Objects.requireNonNull(array);
        Arrays.sort(array);
        int first = 0;
        int last = array.length - 1;
        final List<Pair<Integer, Integer>> result = new ArrayList<>();
        while (first < last) {
            int s = array[first] + array[last];
            if (s == sum) {
                result.add(new Pair<>(array[first], array[last]));
                first++;
                last--;
            } else {
                if (s < sum) {
                    first++;
                } else {
                    last--;
                }
            }
        }
        return result;
    }

    public static <T> void shuffle(final T[] array) {
        Objects.requireNonNull(array);
        for (int i = 0; i < array.length; i++) {
            int k = CRandom.generateRandomInt(0, i);
            swap(array, k, i);
        }
    }

    public static <T> void shuffle(final T[] array, T[] subset, int m) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(subset);
        subset = Arrays.copyOfRange(array, 0, m);
        for (int i = m; i < array.length; i++) {
            int k = CRandom.generateRandomInt(0, i);
            if (k < m) {
                subset[k] = array[i];
            }
        }
    }

    public static char[] findLongestSubarray(char[] array) {
        Objects.requireNonNull(array);
        int[] deltas = computeDeltaArray(array);
        int[] match = findLongestMatch(deltas);
        return extract(array, match[0] + 1, match[1]);
    }

    private static int[] computeDeltaArray(char[] array) {
        int[] deltas = new int[array.length];
        int delta = 0;
        for (int i = 0; i < array.length; i++) {
            if (Character.isLetter(array[i])) {
                delta++;
            } else if (Character.isDigit(array[i])) {
                delta--;
            }
            deltas[i] = delta;
        }
        return deltas;
    }

    private static int[] findLongestMatch(int[] deltas) {
        final Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int[] max = new int[2];
        for (int i = 0; i < deltas.length; i++) {
            if (!map.containsKey(deltas[i])) {
                map.put(deltas[1], i);
            } else {
                int match = map.get(deltas[i]);
                int distance = i - match;
                int longest = max[1] - max[0];
                if (distance > longest) {
                    max[1] = i;
                    max[0] = match;
                }
            }
        }
        return max;
    }

    private static char[] extract(char[] array, int start, int end) {
        return Arrays.copyOfRange(array, start, end);
    }

    public static <T> T findMajorityElement(final T[] array, final Comparator<? super T> cmp) {
        final T candidate = getCandidate(array, cmp);
        return validate(array, candidate, cmp) ? candidate : null;
    }

    private static <T> T getCandidate(final T[] array, final Comparator<? super T> cmp) {
        T majority = null;
        int count = 0;
        for (final T elem : array) {
            if (0 == count) {
                majority = elem;
            }
            if (Objects.compare(elem, majority, cmp) == 0) {
                count++;
            } else {
                count--;
            }
        }
        return majority;
    }

    private static <T> boolean validate(final T[] array, final T majority, final Comparator<? super T> cmp) {
        int count = 0;
        for (final T elem : array) {
            if (Objects.compare(elem, majority, cmp) == 0) {
                count++;
            }
        }
        return count > array.length / 2;
    }

    public static <T> T[] getElems(final T[] array, int k, final Class<? extends T[]> clazz, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        assert (k > 0 && k <= array.length);
        Arrays.sort(array, cmp);
        final T[] smallest = CUtils.newArray(clazz, k);
        System.arraycopy(array, 0, smallest, 0, k);
        return smallest;
    }

    public static <T> T[] getElems2(final T[] array, int k, final Class<? extends T[]> clazz, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        assert (k > 0 && k <= array.length);
        final PriorityQueue<T> heap = getFromHeap(array, k, cmp);
        return heapToArray(heap, clazz);
    }

    private static <T> PriorityQueue<T> getFromHeap(final T[] array, int k, final Comparator<? super T> cmp) {
        final PriorityQueue<T> heap = new PriorityQueue<>(k, cmp);
        for (final T elem : array) {
            if (heap.size() < k) {
                heap.add(elem);
            } else if (Objects.compare(elem, heap.peek(), cmp) < 0) {
                heap.poll();
                heap.add(elem);
            }
        }
        return heap;
    }

    private static <T> T[] heapToArray(final PriorityQueue<T> heap, final Class<? extends T[]> clazz) {
        final T[] array = CUtils.newArray(clazz, heap.size());
        while (!heap.isEmpty()) {
            array[heap.size() - 1] = heap.poll();
        }
        return array;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class PartitionResult {

        public int leftSize;
        public int middleSize;

        public PartitionResult(int leftSize, int middleSize) {
            this.leftSize = leftSize;
            this.middleSize = middleSize;
        }
    }

    public static <T> T[] getElems3(final T[] array, int k, final Class<? extends T[]> clazz, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        assert (k > 0 && k <= array.length);
        final T threshold = rank(array, k - 1, cmp);
        final T[] smallest = CUtils.newArray(clazz, k);
        int count = 0;
        for (final T elem : array) {
            if (Objects.compare(elem, threshold, cmp) < 0) {
                smallest[count++] = elem;
            }
        }
        while (count < k) {
            smallest[count++] = threshold;
        }
        return smallest;
    }

    private static <T> T rank(final T[] array, int k, final Comparator<? super T> cmp) {
        assert (k > 0 && k <= array.length);
        return rank(array, k, 0, array.length - 1, cmp);
    }

    private static <T> T rank(final T[] array, int k, int start, int end, final Comparator<? super T> cmp) {
        final T pivot = array[CRandom.generateRandomInt(start, end)];
        final PartitionResult partition = partition(array, start, end, pivot, cmp);
        int leftSize = partition.leftSize;
        int middleSize = partition.middleSize;

        if (k < leftSize) {
            return rank(array, k, start, start + leftSize - 1, cmp);
        } else if (k < leftSize + middleSize) {
            return pivot;
        }
        return rank(array, k - leftSize - middleSize, start + leftSize + middleSize, end, cmp);
    }

    private static <T> PartitionResult partition(final T[] array, int start, int end, final T pivot, final Comparator<? super T> cmp) {
        int left = start;
        int right = end;
        int middle = start;
        while (middle <= right) {
            if (Objects.compare(array[middle], pivot, cmp) < 0) {
                swap(array, middle, left);
                middle++;
                left++;
            } else if (Objects.compare(array[middle], pivot, cmp) > 0) {
                swap(array, middle, right);
                right--;
            } else if (Objects.compare(array[middle], pivot, cmp) == 0) {
                middle++;
            }
        }
        return new PartitionResult(left - start, right - left + 1);
    }

    public static int maxMin(int[] seances) {
        Objects.requireNonNull(seances);
        int oneStep = 0;
        int twoStep = 0;
        for (int i = seances.length - 1; i >= 0; i--) {
            int bestWith = seances[i] + twoStep;
            int bestWithout = oneStep;
            int current = Math.max(bestWith, bestWithout);
            twoStep = oneStep;
            oneStep = current;
        }
        return oneStep;
    }

    public static Range shortestSuperSequence(int[] big, int[] small) {
        final int[][] nextElements = getNextElementsMulti(big, small);
        final int[] closures = getClosures(nextElements);
        return getShortestClosure(closures);
    }

    private static int[][] getNextElementsMulti(final int[] big, final int[] small) {
        int[][] nextElements = new int[small.length][big.length];
        for (int i = 0; i < small.length; i++) {
            nextElements[i] = getNextElement(big, small[i]);
        }
        return nextElements;
    }

    private static int[] getNextElement(final int[] big, int value) {
        int next = -1;
        int[] nexts = new int[big.length];
        for (int i = big.length - 1; i >= 0; i--) {
            if (big[i] == value) {
                next = i;
            }
            nexts[i] = next;
        }
        return nexts;
    }

    private static int[] getClosures(int[][] nextElements) {
        int[] maxNextElement = new int[nextElements[0].length];
        for (int i = 0; i < nextElements[0].length; i++) {
            maxNextElement[i] = getClosureForIndex(nextElements, i);
        }
        return maxNextElement;
    }

    private static int getClosureForIndex(int[][] nextElements, int index) {
        int max = -1;
        for (int[] nextElement : nextElements) {
            if (nextElement[index] == -1) {
                return -1;
            }
            max = Math.max(max, nextElement[index]);
        }
        return max;
    }

    private static Range getShortestClosure(int[] closures) {
        int bestStart = -1;
        int bestEnd = -1;
        for (int i = 0; i < closures.length; i++) {
            if (closures[i] == -1) {
                break;
            }
            int current = closures[i] - i;
            if (bestStart == -1 || current < bestEnd - bestStart) {
                bestStart = i;
                bestEnd = closures[i];
            }
        }
        return new Range(bestStart, bestEnd);
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Range {

        private int start;
        private int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int length() {
            return end - start + 1;
        }

        public boolean isShorterThan(final Range other) {
            return this.length() < other.length();
        }
    }

    public static Range shortestSuperSequence2(int[] big, int[] small) {
        final int[] closures = getClosures2(big, small);
        return getShortestClosure2(closures);
    }

    private static int[] getClosures2(int[] big, int[] small) {
        int[] closure = new int[big.length];
        for (int i = 0; i < small.length; i++) {
            sweepForClosure(big, closure, small[i]);
        }
        return closure;
    }

    private static void sweepForClosure(int[] big, int[] closures, int value) {
        int next = -1;
        for (int i = big.length - 1; i >= 0; i--) {
            if (big[i] == value) {
                next = i;
            }
            if ((next == -1 || closures[i] < next) && (closures[i] != -1)) {
                closures[i] = next;
            }
        }
    }

    private static Range getShortestClosure2(int[] closures) {
        Range shortest = new Range(0, closures[0]);
        for (int i = 1; i < closures.length; i++) {
            if (closures[i] == -1) {
                break;
            }
            Range range = new Range(i, closures[i]);
            if (!shortest.isShorterThan(range)) {
                shortest = range;
            }
        }
        return shortest;
    }

    public static Range shortestSuperSequence3(int[] big, int[] small) {
        final List<Queue<Integer>> locations = getLocationsForElements(big, small);
        if (Objects.isNull(locations)) {
            return null;
        }
        return getShortestClosure3(locations);
    }

    private static List<Queue<Integer>> getLocationsForElements(int[] big, int[] small) {
        final List<Queue<Integer>> allLocations = new ArrayList<>();
        for (int e : small) {
            final Queue<Integer> locations = getLocations(big, e);
            if (locations.size() == 0) {
                return null;
            }
            allLocations.add(locations);
        }
        return allLocations;
    }

    private static Queue<Integer> getLocations(int[] big, int small) {
        final Queue<Integer> locations = new LinkedList<>();
        for (int i = 0; i < big.length; i++) {
            if (big[i] == small) {
                locations.add(i);
            }
        }
        return locations;
    }

    private static Range getShortestClosure3(final List<Queue<Integer>> lists) {
        final PriorityQueue<HeapNode> minHeap = new PriorityQueue<>();
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < lists.size(); i++) {
            int head = lists.get(i).remove();
            minHeap.add(new HeapNode(head, i));
            max = Math.max(max, head);
        }
        int min = minHeap.peek().locationWithinList;
        int bestRangeMin = min;
        int bestRangeMax = max;
        while (true) {
            final HeapNode n = minHeap.poll();
            final Queue<Integer> list = lists.get(n.listId);
            min = n.locationWithinList;
            if (max - min < bestRangeMax - bestRangeMin) {
                bestRangeMax = max;
                bestRangeMin = min;
            }
            if (list.size() == 0) {
                break;
            }
            n.locationWithinList = list.remove();
            minHeap.add(n);
            max = Math.max(max, n.locationWithinList);
        }
        return new Range(bestRangeMin, bestRangeMax);
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class HeapNode {

        public int listId;
        public int locationWithinList;

        private int start;
        private int end;

        public HeapNode(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static int findSplitPoint(int arr[], int n) {
        int leftSum = 0;
        for (int i = 0; i < n; i++) {
            leftSum += arr[i];
        }
        int rightSum = 0;
        for (int i = n - 1; i >= 0; i--) {
            rightSum += arr[i];
            leftSum -= arr[i];
            if (rightSum == leftSum) {
                return i;
            }
        }
        return -1;
    }

    public static int[] missingTwo(int[] array) {
        int max_value = array.length + 2;
        int rem_square = squareSumToN(max_value, 2);
        int rem_one = max_value * (max_value + 1) / 2;
        for (int i = 0; i < array.length; i++) {
            rem_square -= array[i] * array[i];
            rem_one -= array[i];
        }
        return solveEquation(rem_one, rem_square);
    }

    private static int squareSumToN(int n, int power) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += (int) Math.pow(i, power);
        }
        return sum;
    }

    private static int[] solveEquation(int r1, int r2) {
        int a = 2;
        int b = -2 * r1;
        int c = r1 * r1 - r2;
        double part1 = -1 * b;
        double part2 = Math.sqrt(b * b - 4 * a * c);
        double part3 = 2 * a;
        int solutionX = (int) ((part1 + part2) / part3);
        int solutionY = r1 - solutionX;
        return new int[]{solutionX, solutionY};
    }

    public static void addNewNumber(double number, final PriorityQueue<Double> maxHeap, final PriorityQueue<Double> minHeap) {
        if (maxHeap.size() == minHeap.size()) {
            if (Objects.nonNull(minHeap.peek()) && number > minHeap.peek()) {
                maxHeap.offer(minHeap.poll());
                minHeap.offer(number);
            } else {
                maxHeap.offer(number);
            }
        } else {
            if (number < maxHeap.peek()) {
                minHeap.offer(maxHeap.poll());
                maxHeap.offer(number);
            } else {
                minHeap.offer(number);
            }
        }
    }

    public static double getMedian(final PriorityQueue<Double> maxHeap, final PriorityQueue<Double> minHeap) {
        if (maxHeap.isEmpty()) {
            return 0;
        }
        if (maxHeap.size() == minHeap.size()) {
            return (maxHeap.peek() + minHeap.peek()) / 2;
        } else {
            return maxHeap.peek();
        }
    }

    public static int computeHistogramVolume(int[] histogram) {
        int start = 0;
        int end = histogram.length - 1;

        final HistogramData[] data = createHistogramData(histogram);
        int max = data[0].getRightMaxIndex();
        int leftVolume = subgraphVolume(data, start, max, true);
        int rightVolume = subgraphVolume(data, max, end, false);
        return leftVolume + rightVolume;
    }

    private static HistogramData[] createHistogramData(int[] histogram) {
        final HistogramData[] result = new HistogramData[histogram.length];
        for (int i = 0; i < histogram.length; i++) {
            result[i] = new HistogramData(histogram[i]);
        }
        int maxIndex = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[maxIndex] < histogram[i]) {
                maxIndex = i;
            }
            result[i].setLeftMaxIndex(maxIndex);
        }
        maxIndex = histogram.length - 1;
        for (int i = histogram.length - 1; i >= 0; i--) {
            if (histogram[maxIndex] < histogram[i]) {
                maxIndex = i;
            }
            result[i].setRightMaxIndex(maxIndex);
        }
        return result;
    }

    private static int subgraphVolume(final HistogramData[] histogram, int start, int end, boolean isLeft) {
        if (start >= end) {
            return 0;
        }
        int sum = 0;
        if (isLeft) {
            int max = histogram[end - 1].getLeftMaxIndex();
            sum += borderedVolume(histogram, max, end);
            sum += subgraphVolume(histogram, start, max, isLeft);
        } else {
            int max = histogram[start + 1].getRightMaxIndex();
            sum += borderedVolume(histogram, start, max);
            sum += subgraphVolume(histogram, max, end, isLeft);
        }
        return sum;
    }

    private static int borderedVolume(final HistogramData[] data, int start, int end) {
        if (start >= end) {
            return 0;
        }
        int min = Math.min(data[start].getHeight(), data[end].getHeight());
        int sum = 0;
        for (int i = start + 1; i < end; i++) {
            sum += min - data[i].getHeight();
        }
        return sum;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class HistogramData {

        private int height;
        private int leftMaxIndex = -1;
        private int rightMaxIndex = -1;

        public HistogramData(int height) {
            this.height = height;
        }
    }

    public static int computeHistogramVolume2(int[] histogram) {
        Objects.requireNonNull(histogram);
        int[] leftMaxes = new int[histogram.length];
        int leftMax = histogram[0];
        for (int i = 0; i < histogram.length; i++) {
            leftMax = Math.max(leftMax, histogram[i]);
            leftMaxes[i] = leftMax;
        }
        int sum = 0;
        int rightMax = histogram[histogram.length - 1];
        for (int i = histogram.length - 1; i >= 0; i--) {
            rightMax = Math.max(rightMax, histogram[i]);
            int secondTallest = Math.min(rightMax, leftMaxes[i]);
            if (secondTallest > histogram[i]) {
                sum += secondTallest - histogram[i];
            }
        }
        return sum;
    }

    public static int missingNumber(int[] array1, int[] array2) {
        Objects.requireNonNull(array1);
        Objects.requireNonNull(array2);
        int missingNumber = array1[0];
        for (int index = 1; index < array1.length; index++) {
            missingNumber ^= array1[index];
        }
        for (int index = 0; index < array2.length; index++) {
            missingNumber ^= array2[index];
        }
        return missingNumber;
    }

    public static int missingNumber(int[] elements) {
        int n = elements.length + 1;
        int missingNumber = elements[0];
        for (int index = 1; index <= n; index++) {
            if (index < elements.length) {
                missingNumber ^= elements[index];
            }
            missingNumber ^= index;
        }
        return missingNumber;
    }

    public static int missingNumber2(int[] elements) {
        int arraySum = 0;
        for (int element : elements) {
            arraySum += element;
        }
        int n = elements.length + 1;
        int sumOfNumbers = (n * (n + 1)) / 2;
        return sumOfNumbers - arraySum;
    }

    public static int getOddNumber(int[] elements) {
        int oddNumber = elements[0];
        int length = elements.length;
        for (int index = 1; index < length; index++) {
            oddNumber ^= elements[index];
        }
        return oddNumber;
    }

    public static <T> void moveZeroToEnd(final T[] array, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        int index = 0;
        int nonZeros = 0;
        int length = array.length;
        while (index < length) {
            if (Objects.compare(array[index], value, cmp) != 0) {
                array[nonZeros++] = array[index];
            }
            index++;
        }
        while (nonZeros < length) {
            array[nonZeros++] = value;
        }
    }

    //Kadane’s algorithm
    public static int maxSubArray(int[] array) {
        Objects.requireNonNull(array);
        int localMax = 0;
        int maxSum = Integer.MIN_VALUE;
        for (int index = 0; index < array.length; index++) {
            localMax = localMax + array[index];
            localMax = Math.max(0, localMax);
            maxSum = Math.max(maxSum, localMax);
        }
        return maxSum;
    }

    public static <T> void reverseArrayIterative(final T[] array) {
        Objects.requireNonNull(array);
        int length = array.length;
        int half = length / 2;
        for (int index = 0; index < half; index++) {
            T temp = array[index];
            array[index] = array[length - 1 - index];
            array[length - 1 - index] = temp;
        }
    }

    public static <T> void reverseArrayRecursive(final T[] array, int low, int high) {
        Objects.requireNonNull(array);
        if (high < low) {
            return;
        }
        T temp = array[low];
        array[low] = array[high];
        array[high] = temp;
        reverseArrayRecursive(array, low + 1, high - 1);
    }

    public static <T> void replaceByGreatestRight(final T[] array, final T value, final Comparator<? super T> cmp) {
        int lastIndex = array.length - 1;
        T maxElement = array[lastIndex];
        array[lastIndex--] = value;
        while (lastIndex >= 0) {
            T current = array[lastIndex];
            array[lastIndex] = maxElement;
            if (Objects.compare(current, maxElement, cmp) > 0) {
                maxElement = current;
            }
            lastIndex--;
        }
    }

    public static <T> List<T> getLeaders(final T[] array, final T value, final Comparator<? super T> cmp) {
        final Stack<T> stack = new Stack<>();
        final List<T> result = new ArrayList<>();

        int lastIndex = array.length - 1;
        T maxElement = array[lastIndex];
        array[lastIndex--] = value;
        stack.push(maxElement);

        while (lastIndex >= 0) {
            T current = array[lastIndex];
            if (Objects.compare(current, maxElement, cmp) > 0) {
                maxElement = current;
                stack.push(maxElement);
            }
            lastIndex--;
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    //Boyer–Moore vote algorithm
    public static <T> T majorityElement(final T[] array, final Comparator<? super T> cmp) {
        T element = null;
        int counter = 0;
        int length = array.length;
        int index = 0;
        while (index < length) {
            if (counter == 0) {
                element = array[index];
                counter++;
            } else if (Objects.compare(element, array[index], cmp) == 0) {
                counter++;
            } else {
                counter--;
            }
            index++;
        }
        if (counter == 0) {
            return null;
        }
        index = -1;
        counter = 0;
        while (++index < length) {
            if (Objects.compare(element, array[index], cmp) == 0) {
                counter++;
            }
        }
        if (counter > length / 2) {
            return element;
        }
        return null;
    }

    public static Integer getDuplicate(int[] numbers) {
        for (int index = 0; index < numbers.length; index++) {
            int absIndex = Math.abs(numbers[index]);
            if (numbers[absIndex] < 0) {
                return index;
            } else {
                numbers[absIndex] = -numbers[absIndex];
            }
        }
        return null;
    }

    public static void getDistinctSortedArray(int[] array) {
        Objects.requireNonNull(array);
        Arrays.sort(array);
        for (int i = 1; i < array.length; i++) {
            if (array[i] == array[i - 1]) {
                array[i]++;
            } else if (array[i] < array[i - 1]) {
                array[i] = array[i - 1] + 1;
            }
        }
    }

    public static int[] randomArray(int size, int min, int max) {
        assert (size > 0);
        int[] array = new int[size];
        for (int j = 0; j < size; j++) {
            array[j] = CRandom.generateRandomInt(min, max);
        }
        return array;
    }

    public static <T, V extends T> boolean contains(final T value, final V[] array, final Comparator<? super T> cmp) {
        Objects.requireNonNull(array);
        for (final V current : array) {
            if (Objects.compare(value, current, cmp) == 0) {
                return true;
            }
        }
        return false;
    }

    public static double average(final List<? extends Number> list) {
        Objects.requireNonNull(list);
        return list.stream().mapToDouble((number) -> number.doubleValue()).average().getAsDouble();
    }

    public static double sum(final List<? extends Number> list, double initialValue) {
        Objects.requireNonNull(list);
        return list.stream().map((element) -> element.doubleValue()).reduce(initialValue, (accumulator, _item) -> accumulator + _item);
    }
}
