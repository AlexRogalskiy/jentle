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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * Custom miscellaneous algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CMisc {

    /**
     * Default Logger instance
     */
    private static final Logger LOGGER = LogManager.getLogger(CMisc.class);

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
    @EqualsAndHashCode
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
    @EqualsAndHashCode
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

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Grid<E extends Position, T extends Actor<E>> {

        private boolean[][] grid;
        private final T actor;

        public Grid(final T actor) {
            this(actor, 1);
        }

        public Grid(final T actor, int size) {
            assert (size > 0);
            this.grid = new boolean[size][size];
            this.actor = actor;
        }

        private void copyWithShift(boolean[][] oldGrid, boolean[][] newGrid, int shiftRow, int shiftColumn) {
            for (int r = 0; r < oldGrid.length; r++) {
                System.arraycopy(oldGrid[r], 0, newGrid[r + shiftRow], shiftColumn, oldGrid[0].length);
            }
        }

        private void ensureFit(final E position) {
            int shiftRow = 0;
            int shiftColumn = 0;

            int numRows = this.grid.length;
            if (position.row < 0) {
                shiftRow = numRows;
                numRows *= 2;
            } else if (position.row >= numRows) {
                numRows *= 2;
            }

            int numColumns = this.grid[0].length;
            if (position.column < 0) {
                shiftColumn = numColumns;
                numColumns *= 2;
            } else if (position.column >= numColumns) {
                numColumns *= 2;
            }

            if (numRows != this.grid.length || numColumns != this.grid[0].length) {
                boolean[][] newGrid = new boolean[numRows][numColumns];
                copyWithShift(this.grid, newGrid, shiftRow, shiftColumn);
                this.actor.adjustPosition(shiftRow, shiftColumn);
                this.grid = newGrid;
            }
        }

        private void flip(final E position) {
            int row = position.row;
            int column = position.column;
            this.grid[row][column] = !this.grid[row][column];
        }

        public void move() {
            this.actor.turn(this.grid[this.actor.position.row][this.actor.position.column]);
            this.flip(this.actor.position);
            this.actor.move();
            this.ensureFit(this.actor.position);
        }

        public String toFormatString() {
            final StringBuffer sb = new StringBuffer();
            for (int r = 0; r < this.grid.length; r++) {
                for (int c = 0; c < this.grid[0].length; c++) {
                    if (r == this.actor.position.row && c == this.actor.position.column) {
                        sb.append(this.actor.orientation.toFormatString());
                    } else if (this.grid[r][c]) {
                        sb.append("X");
                    } else {
                        sb.append("_");
                    }
                }
                sb.append(StringUtils.LF);
            }
            sb.append(this.actor.getClass().getName()).append(this.actor.orientation.toFormatString()).append(StringUtils.LF);
            return sb.toString();
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Actor<T extends Position> {

        public Orientation orientation;
        public T position;

        public Actor() {
            this((T) new Position(0, 0), Orientation.RIGHT);
        }

        public Actor(final T position, final Orientation orientation) {
            this.position = position;
            this.orientation = orientation;
        }

        public void turn(boolean clockWise) {
            this.orientation = this.orientation.getTurn(clockWise);
        }

        public void move() {
            if (Objects.equals(Orientation.LEFT, this.orientation)) {
                this.position.column--;
            } else if (Objects.equals(Orientation.RIGHT, this.orientation)) {
                this.position.column++;
            } else if (Objects.equals(Orientation.UP, this.orientation)) {
                this.position.row--;
            } else if (Objects.equals(Orientation.DOWN, this.orientation)) {
                this.position.row++;
            }
        }

        public void adjustPosition(int shiftRow, int shiftColumn) {
            this.position.row += shiftRow;
            this.position.column += shiftColumn;
        }
    }

    public static enum Orientation {

        LEFT, UP, RIGHT, DOWN;

        public Orientation getTurn(boolean clockWise) {
            if (Objects.equals(Orientation.LEFT, this)) {
                return clockWise ? Orientation.UP : Orientation.DOWN;
            } else if (Objects.equals(Orientation.UP, this)) {
                return clockWise ? Orientation.RIGHT : Orientation.LEFT;
            } else if (Objects.equals(Orientation.RIGHT, this)) {
                return clockWise ? Orientation.DOWN : Orientation.UP;
            }
            return clockWise ? Orientation.LEFT : Orientation.RIGHT;
        }

        public String toFormatString() {
            if (Objects.equals(Orientation.LEFT, this)) {
                return "\u2190";
            } else if (Objects.equals(Orientation.UP, this)) {
                return "\u2191";
            } else if (Objects.equals(Orientation.RIGHT, this)) {
                return "\u2192";
            }
            return "\u2193";
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Position implements Cloneable {

        public int row;
        public int column;

        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneDeclaresCloneNotSupported"})
        public Position clone() {
            return new Position(this.row, this.column);
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Board<E extends Position, T extends Actor<E>> {

        private final Set<E> whites;
        private final T actor;
        private E topLeft;
        private E bottomRight;

        public Board(final T actor) {
            this(actor, (E) new Position(0, 0), (E) new Position(0, 0));
        }

        public Board(final T actor, final E topLeft, final E bottomRight) {
            this.whites = new HashSet<>();
            this.actor = actor;
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
        }

        public void move() {
            this.actor.turn(this.isWhite(this.actor.position));
            this.flip(this.actor.position);
            this.actor.move();
            this.ensureFit(this.actor.position);
        }

        private void flip(final E position) {
            if (this.whites.contains(position)) {
                this.whites.remove(position);
            } else {
                this.whites.add((E) position.clone());
            }
        }

        private void ensureFit(final E position) {
            int row = position.row;
            int column = position.column;

            this.topLeft.row = Math.min(this.topLeft.row, row);
            this.topLeft.column = Math.min(this.topLeft.column, column);
            this.bottomRight.row = Math.max(this.bottomRight.row, row);
            this.bottomRight.column = Math.max(this.bottomRight.column, column);
        }

        public boolean isWhite(int row, int column) {
            return this.isWhite((E) new Position(row, column));
        }

        public boolean isWhite(final E position) {
            return this.whites.contains(position);
        }

        public String toFormatString() {
            final StringBuffer sb = new StringBuffer();
            int rowMin = this.topLeft.row;
            int rowMax = this.bottomRight.row;
            int colMin = this.topLeft.column;
            int colMax = this.bottomRight.column;
            for (int r = rowMin; r <= rowMax; r++) {
                for (int c = colMin; c <= colMax; c++) {
                    if (r == this.actor.position.row && c == this.actor.position.column) {
                        sb.append(this.actor.orientation.toFormatString());
                    } else if (this.isWhite(r, c)) {
                        sb.append("X");
                    } else {
                        sb.append("_");
                    }
                }
                sb.append(StringUtils.LF);
            }
            sb.append(this.actor.getClass().getName()).append(this.actor.orientation.toFormatString()).append(StringUtils.LF);
            return sb.toString();
        }
    }

    public static List<HtWt> longestIncreasingSequence(final List<HtWt> list) {
        Collections.sort(list);
        final List<List<HtWt>> result = new ArrayList<>();
        List<HtWt> bestSequence = null;
        for (int i = 0; i < list.size(); i++) {
            final List<HtWt> longestAtIndex = bestSequenceAtIndex(list, result, i);
            result.add(i, longestAtIndex);;
            bestSequence = max(bestSequence, longestAtIndex);
        }
        return bestSequence;
    }

    private static List<HtWt> bestSequenceAtIndex(final List<HtWt> list, List<List<HtWt>> resultList, int index) {
        final HtWt value = list.get(index);
        List<HtWt> bestSequence = Collections.EMPTY_LIST;
        for (int i = 0; i < index; i++) {
            final List<HtWt> result = resultList.get(i);
            if (canAppend(result, value)) {
                bestSequence = max(result, bestSequence);
            }
        }
        final List<HtWt> best = new ArrayList<>(bestSequence);
        best.add(value);
        return best;
    }

    private static List<HtWt> max(final List<HtWt> sequence1, final List<HtWt> sequence2) {
        if (Objects.isNull(sequence1)) {
            return sequence2;
        } else if (Objects.isNull(sequence2)) {
            return sequence1;
        }
        return sequence1.size() > sequence2.size() ? sequence1 : sequence2;
    }

    private static boolean canAppend(final List<HtWt> result, final HtWt value) {
        if (Objects.isNull(result)) {
            return false;
        }
        if (result.isEmpty()) {
            return true;
        }
        final HtWt last = result.get(result.size() - 1);
        return last.isBefore(value);
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    @ToString
    public static class HtWt implements Comparable<HtWt> {

        private int height;
        private int weight;

        @Override
        public int compareTo(final HtWt second) {
            if (this.height != second.height) {
                return Integer.valueOf(this.height).compareTo(second.height);
            }
            return Integer.valueOf(this.weight).compareTo(second.weight);
        }

        public boolean isBefore(final HtWt other) {
            if (this.height < other.height && this.weight < other.weight) {
                return true;
            }
            return false;
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class DocPair {

        public int doc1;
        public int doc2;

        public DocPair(int doc1, int doc2) {
            this.doc1 = doc1;
            this.doc2 = doc2;
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Document {

        private List<Integer> words;
        private int id;

        public Document(int id, final List<Integer> words) {
            this.id = id;
            this.words = words;
        }

        public int size() {
            return Objects.isNull(this.words) ? 0 : this.words.size();
        }
    }

    private static Map<DocPair, Double> computeSimilarities(final List<Document> documents) {
        final Map<DocPair, Double> similarities = new HashMap<>();
        for (int i = 0; i < documents.size(); i++) {
            for (int j = i + 1; j < documents.size(); j++) {
                final Document doc1 = documents.get(i);
                final Document doc2 = documents.get(j);
                double sim = computeSimilarity(doc1, doc2);
                if (sim > 0) {
                    final DocPair pair = new DocPair(doc1.getId(), doc2.getId());
                    similarities.put(pair, sim);
                }
            }
        }
        return similarities;
    }

    private static double computeSimilarity(final Document doc1, final Document doc2) {
        int intersection = 0;
        final Set<Integer> set1 = new HashSet<>();
        set1.addAll(doc1.getWords());
        intersection = doc2.getWords().stream().filter((word) -> (set1.contains(word))).map((_item) -> 1).reduce(intersection, Integer::sum);
        double union = doc1.size() + doc2.size() - intersection;
        return intersection / union;
    }

    private static Map<DocPair, Double> computeSimilarities(final Map<Integer, Document> documents) {
        final CHashMapList<Integer, Integer> wordToDocs = groupWords(documents);
        final Map<DocPair, Double> similarities = computeIntersections(wordToDocs);
        adjustToSimilarities(documents, similarities);
        return similarities;
    }

    private static CHashMapList<Integer, Integer> groupWords(final Map<Integer, Document> documents) {
        final CHashMapList<Integer, Integer> wordToDocs = new CHashMapList<>();
        documents.values().stream().forEach((document) -> {
            final List<Integer> words = document.getWords();
            words.stream().forEach((word) -> {
                wordToDocs.put(word, document.getId());
            });
        });
        return wordToDocs;
    }

    private static Map<DocPair, Double> computeIntersections(final CHashMapList<Integer, Integer> wordToDocs) {
        @SuppressWarnings("UnusedAssignment")
        final Map<DocPair, Double> similarities = new HashMap<>();
        final Set<Integer> words = wordToDocs.keySet();
        words.stream().map((word) -> wordToDocs.get(word)).map((docs) -> {
            Collections.sort(docs);
            return docs;
        }).forEach((docs) -> {
            for (int i = 0; i < docs.size(); i++) {
                for (int j = i + 1; j < docs.size(); j++) {
                    increment(similarities, docs.get(i), docs.get(j));
                }
            }
        });
        return similarities;
    }

    private static void increment(final Map<DocPair, Double> similarities, int doc1, int doc2) {
        final DocPair pair = new DocPair(doc1, doc2);
        if (!similarities.containsKey(pair)) {
            similarities.put(pair, 1.0);
        } else {
            similarities.put(pair, similarities.get(pair) + 1);
        }
    }

    private static void adjustToSimilarities(final Map<Integer, Document> documents, final Map<DocPair, Double> similarities) {
        similarities.entrySet().stream().forEach((entry) -> {
            final DocPair pair = entry.getKey();
            final Double intersection = entry.getValue();
            final Document doc1 = documents.get(pair.doc1);
            final Document doc2 = documents.get(pair.doc2);
            double union = (double) doc1.size() + doc2.size() - intersection;
            entry.setValue(intersection / union);
        });
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Element implements Comparable<Element> {

        public int word;
        public int document;

        public Element(int word, int document) {
            this.word = word;
            this.document = document;
        }

        @Override
        public int compareTo(final Element element) {
            if (word == element.word) {
                return document - element.document;
            }
            return word - element.word;
        }
    }

    public static Map<DocPair, Double> computeSimilarities2(final Map<Integer, Document> documents) {
        final List<Element> elements = sortWords(documents);
        final Map<DocPair, Double> similarities = computeIntersections(elements);
        adjustToSimilarities(documents, similarities);
        return similarities;
    }

    private static List<Element> sortWords(final Map<Integer, Document> documents) {
        final List<Element> elements = new ArrayList<>();
        for (final Document document : documents.values()) {
            final List<Integer> words = document.getWords();
            for (int word : words) {
                elements.add(new Element(word, document.getId()));
            }
        }
        Collections.sort(elements);
        return elements;
    }

    private static Map<DocPair, Double> computeIntersections(final List<Element> elements) {
        final Map<DocPair, Double> similarities = new HashMap<>();
        for (int i = 0; i < elements.size(); i++) {
            final Element left = elements.get(i);
            for (int j = i + 1; j < elements.size(); j++) {
                final Element right = elements.get(j);
                if (left.word != right.word) {
                    break;
                }
                increment(similarities, left.document, right.document);
            }
        }
        return similarities;
    }

    public static class CClimbingAlgorithms {

        @Data
        @EqualsAndHashCode
        @ToString
        public static class State {

            private List<Stack<String>> state;
            private int heuristics;

            public State(final List<Stack<String>> state) {
                this.state = state;
            }

            public State(final List<Stack<String>> state, int heuristics) {
                this.state = state;
                this.heuristics = heuristics;
            }

            public State(final State state) {
                if (Objects.nonNull(state)) {
                    this.state = new ArrayList<>();
                    for (final Stack s : state.getState()) {
                        Stack s1;
                        s1 = (Stack) s.clone();
                        this.state.add(s1);
                    }
                    this.heuristics = state.getHeuristics();
                }
            }
        }

        @Data
        @EqualsAndHashCode
        @ToString
        public static class CClimbing {

            private static void printEachStep(final State state) {
                final List<Stack<String>> stackList = state.getState();
                stackList.forEach(stack -> {
                    while (!stack.isEmpty()) {
                        LOGGER.debug(stack.pop());
                    }
                    LOGGER.debug(" ");
                });
            }

            private Stack<String> getStackWithValues(final String[] blocks) {
                Stack<String> stack = new Stack<>();
                for (final String block : blocks) {
                    stack.push(block);
                }
                return stack;
            }

            public List<State> getRouteWithHillClimbing(final Stack<String> initStateStack, final Stack<String> goalStateStack) throws Exception {
                List<Stack<String>> initStateStackList = new ArrayList<>();
                initStateStackList.add(initStateStack);
                int initStateHeuristics = getHeuristicsValue(initStateStackList, goalStateStack);
                final State initState = new State(initStateStackList, initStateHeuristics);

                List<State> resultPath = new ArrayList<>();
                resultPath.add(new State(initState));

                State currentState = initState;
                boolean noStateFound = false;
                while (!currentState.getState()
                        .get(0)
                        .equals(goalStateStack) || noStateFound) {
                    noStateFound = true;
                    State nextState = findNextState(currentState, goalStateStack);
                    if (nextState != null) {
                        noStateFound = false;
                        currentState = nextState;
                        resultPath.add(new State(nextState));
                    }
                }
                return resultPath;
            }

            public State findNextState(final State currentState, final Stack<String> goalStateStack) {
                List<Stack<String>> listOfStacks = currentState.getState();
                int currentStateHeuristics = currentState.getHeuristics();
                return listOfStacks.stream()
                        .map(stack -> {
                            return applyOperationsOnState(listOfStacks, stack, currentStateHeuristics, goalStateStack);
                        })
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);
            }

            public State applyOperationsOnState(final List<Stack<String>> listOfStacks, final Stack<String> stack, int currentStateHeuristics, final Stack<String> goalStateStack) {
                State tempState;
                List<Stack<String>> tempStackList = new ArrayList<>(listOfStacks);
                String block = stack.pop();
                if (stack.size() == 0) {
                    tempStackList.remove(stack);
                }
                tempState = pushElementToNewStack(tempStackList, block, currentStateHeuristics, goalStateStack);
                if (tempState == null) {
                    tempState = pushElementToExistingStacks(stack, tempStackList, block, currentStateHeuristics, goalStateStack);
                }
                if (tempState == null) {
                    stack.push(block);
                }
                return tempState;
            }

            private State pushElementToNewStack(final List<Stack<String>> currentStackList, final String block, int currentStateHeuristics, final Stack<String> goalStateStack) {
                State newState = null;
                final Stack<String> newStack = new Stack<>();
                newStack.push(block);
                currentStackList.add(newStack);
                int newStateHeuristics = getHeuristicsValue(currentStackList, goalStateStack);
                if (newStateHeuristics > currentStateHeuristics) {
                    newState = new State(currentStackList, newStateHeuristics);
                } else {
                    currentStackList.remove(newStack);
                }
                return newState;
            }

            private State pushElementToExistingStacks(final Stack currentStack, final List<Stack<String>> currentStackList, String block, int currentStateHeuristics, Stack<String> goalStateStack) {
                Optional<State> newState = currentStackList.stream()
                        .filter(stack -> stack != currentStack)
                        .map(stack -> {
                            return pushElementToStack(stack, block, currentStackList, currentStateHeuristics, goalStateStack);
                        })
                        .filter(Objects::nonNull)
                        .findFirst();

                return newState.orElse(null);
            }

            /**
             * This method pushes a block to the stack and returns new state if
             * its closer to goal
             */
            private State pushElementToStack(Stack stack, String block, List<Stack<String>> currentStackList, int currentStateHeuristics, final Stack<String> goalStateStack) {
                stack.push(block);
                int newStateHeuristics = getHeuristicsValue(currentStackList, goalStateStack);
                if (newStateHeuristics > currentStateHeuristics) {
                    return new State(currentStackList, newStateHeuristics);
                }
                stack.pop();
                return null;
            }

            public int getHeuristicsValue(final List<Stack<String>> currentState, final Stack<String> goalStateStack) {
                Integer heuristicValue;
                heuristicValue = currentState.stream()
                        .mapToInt(stack -> {
                            return getHeuristicsValueForStack(stack, currentState, goalStateStack);
                        })
                        .sum();
                return heuristicValue;
            }

            public int getHeuristicsValueForStack(final Stack<String> stack, final List<Stack<String>> currentState, final Stack<String> goalStateStack) {
                int stackHeuristics = 0;
                boolean isPositioneCorrect = true;
                int goalStartIndex = 0;
                for (String currentBlock : stack) {
                    if (isPositioneCorrect && currentBlock.equals(goalStateStack.get(goalStartIndex))) {
                        stackHeuristics += goalStartIndex;
                    } else {
                        stackHeuristics -= goalStartIndex;
                        isPositioneCorrect = false;
                    }
                    goalStartIndex++;
                }
                return stackHeuristics;
            }
        }

        public void runTask() {
            final CClimbing сlimbing = new CClimbing();
            String blockArr[] = {"B", "C", "D", "A"};
            final Stack<String> startState = сlimbing.getStackWithValues(blockArr);
            String goalBlockArr[] = {"A", "B", "C", "D"};
            final Stack<String> endState = сlimbing.getStackWithValues(goalBlockArr);
            try {
                final List<State> solutionSequence = сlimbing.getRouteWithHillClimbing(startState, endState);
                solutionSequence.forEach(CClimbing::printEachStep);
            } catch (Exception ex) {
                LOGGER.debug("ERROR: cannot process climbing routes: message=" + ex.getMessage());
            }
        }
    }

    public static int romanToArabic(final String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        final List<RomanNumeral> romanNumerals = RomanNumeral.getSortedValues(Comparator.comparing((RomanNumeral e) -> e.getValue(), Comparator.reverseOrder()));
        int i = 0;
        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            final RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + " cannot be converted to a Roman Numeral");
        }
        return result;
    }

    public static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " is not in range (0,4000]");
        }

        final List<RomanNumeral> romanNumerals = RomanNumeral.getSortedValues(Comparator.comparing(e -> e.getValue(), Comparator.reverseOrder()));

        int i = 0;
        final StringBuilder sb = new StringBuilder();
        while ((number > 0) && (i < romanNumerals.size())) {
            final RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }
        return sb.toString();
    }
}
