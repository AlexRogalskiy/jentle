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

import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.math3.util.Pair;

/**
 *
 * Custom array utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CArrayUtils {

    private CArrayUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
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
        T temp = array[m];
        array[m] = array[n];
        array[n] = temp;
    }

    public static <T> List<? extends T> getEvenIndexedStrings(final T[] array, final IntPredicate predicate) {
        List<? extends T> result = IntStream
                .range(0, array.length)
                .filter(predicate)
                .mapToObj(i -> array[i])
                .collect(Collectors.toList());
        return result;
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
    @EqualsAndHashCode(callSuper = false)
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
            int k = CNumericUtils.generateRandomInt(0, i);
            swap(array, k, i);
        }
    }

    public static <T> void shuffle(final T[] array, T[] subset, int m) {
        Objects.requireNonNull(array);
        Objects.requireNonNull(subset);
        subset = Arrays.copyOfRange(array, 0, m);
        for (int i = m; i < array.length; i++) {
            int k = CNumericUtils.generateRandomInt(0, i);
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
        for (int i = 0; i < k; i++) {
            smallest[i] = array[i];
        }
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
    @EqualsAndHashCode(callSuper = false)
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
        final T pivot = array[CNumericUtils.generateRandomInt(start, end)];
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
        for (int i = 0; i < nextElements.length; i++) {
            if (nextElements[i][index] == -1) {
                return -1;
            }
            max = Math.max(max, nextElements[i][index]);
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
    @EqualsAndHashCode(callSuper = false)
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
    @EqualsAndHashCode(callSuper = false)
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

    public static String[] join(final String[] first, final String[] second) {
        return Stream.concat(Arrays.stream(first), Arrays.stream(second)).toArray(String[]::new);
    }

    public static <T> Collection<T> join(final Collection<T> first, final Collection<T> second, final Predicate<? super T> filter) {
        return Stream.concat(first.stream(), second.stream()).filter(filter).collect(Collectors.toList());
    }

    public static String join(final String[] array, final String delimiter) {
        return Arrays.stream(array).collect(Collectors.joining(delimiter));
    }

    public static String join(final Collection<String> collection, final String delimiter) {
        return collection.stream().collect(Collectors.joining(delimiter));
    }

    public static <K, V> String join(final Map<K, V> map, final String keyValueDelimiter, final String delimiter) {
        return map.entrySet().stream().map(entry -> entry.getKey() + keyValueDelimiter + entry.getValue()).collect(Collectors.joining(delimiter));
    }

    public static Map<Integer, List<String>> splitByLength(final String[] array) {
        return Arrays.stream(array).filter(Objects::nonNull).collect(Collectors.groupingBy(String::length));
    }

    public static Collection<String> split(final String value, final String delimiter) {
        return Arrays.stream(value.split(delimiter))
                .map(String::trim)
                .filter(next -> !next.isEmpty())
                .collect(Collectors.toList());
    }
}
