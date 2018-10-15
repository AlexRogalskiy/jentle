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
package com.wildbeeslabs.jentle.algorithms.string;

import com.wildbeeslabs.jentle.algorithms.utils.CNumericUtils;
import com.wildbeeslabs.jentle.algorithms.utils.CStringUtils;
import com.wildbeeslabs.jentle.collections.map.CHashMapList;
import com.wildbeeslabs.jentle.collections.tree.CTrie;
import com.wildbeeslabs.jentle.collections.tree.CTrie3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * Custom string algorithms implementations
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CString {

    private CString() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static List<String> transform(final String start, final String stop, final String[] words) {
        final CHashMapList<String, String> wordList = createWordMap(words);
        final Set<String> visited = new HashSet<>();
        return transform(visited, start, stop, wordList);
    }

    private static LinkedList<String> transform(final Set<String> visited, final String start, final String stop, final CHashMapList<String, String> wordList) {
        if (start.equals(stop)) {
            final LinkedList<String> path = new LinkedList<>();
            path.add(start);
            return path;
        } else if (visited.contains(start)) {
            return null;
        }
        visited.add(start);
        final List<String> words = getValidLinkedWords(start, wordList);
        for (final String word : words) {
            final LinkedList<String> path = transform(visited, word, stop, wordList);
            if (Objects.nonNull(path)) {
                path.addFirst(start);
                return path;
            }
        }
        return null;
    }

    private static CHashMapList<String, String> createWordMap(final String[] words) {
        final CHashMapList<String, String> wordList = new CHashMapList<>();
        for (final String word : words) {
            final List<String> linked = getWildCardRoots(word);
            linked.stream().forEach((linkedWord) -> {
                wordList.put(linkedWord, word);
            });
        }
        return wordList;
    }

    private static List<String> getWildCardRoots(final String word) {
        final List<String> words = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            String w = word.substring(0, i) + "_" + word.substring(i + 1);
            words.add(w);
        }
        return words;
    }

    private static List<String> getValidLinkedWords(final String word, final CHashMapList<String, String> wordList) {
        final List<String> wildCards = getWildCardRoots(word);
        final List<String> linkedWords = new ArrayList<>();
        wildCards.stream().map((wildCard) -> wordList.get(wildCard)).forEach((words) -> {
            words.stream().filter((linkedWord) -> (!linkedWord.equals(word))).forEach((linkedWord) -> {
                linkedWords.add(linkedWord);
            });
        });
        return linkedWords;
    }

    public static List<String> transform2(final String start, final String stop, final String[] words) {
        final CHashMapList<String, String> wordList = createWordMap(words);
        final BFSData<String, PathNode> sourceData = new BFSData<>(new PathNode(start));
        final BFSData<String, PathNode> destData = new BFSData<>(new PathNode(stop));
        while (!sourceData.isFinished() && !destData.isFinished()) {
            String collision = searchLevel(wordList, sourceData, destData);
            if (Objects.nonNull(collision)) {
                return mergePaths(sourceData, destData, collision);
            }
            collision = searchLevel(wordList, destData, sourceData);
            if (Objects.nonNull(collision)) {
                return mergePaths(sourceData, destData, collision);
            }
        }
        return null;
    }

    private static String searchLevel(final CHashMapList<String, String> wordList, final BFSData<String, PathNode> primary, final BFSData<String, PathNode> secondary) {
        int count = primary.toVisit.size();
        for (int i = 0; i < count; i++) {
            final PathNode pathNode = primary.toVisit.poll();
            final String word = pathNode.getValue();
            if (secondary.visited.containsKey(word)) {
                return pathNode.getValue();
            }
            final List<String> words = getValidLinkedWords(word, wordList);
            words.stream().filter((w) -> (!primary.visited.containsKey(w))).map((w) -> {
                final PathNode next = new PathNode(w, pathNode);
                primary.visited.put(w, next);
                return next;
            }).forEach((next) -> {
                primary.toVisit.add(next);
            });
        }
        return null;
    }

    private static List<String> mergePaths(final BFSData<String, PathNode> data1, final BFSData<String, PathNode> data2, final String connection) {
        final PathNode end1 = data1.visited.get(connection);
        final PathNode end2 = data2.visited.get(connection);
        final LinkedList<String> pathOne = end1.collapse(false);
        final LinkedList<String> pathTwo = end2.collapse(true);
        pathTwo.removeFirst();
        pathOne.addAll(pathTwo);
        return pathOne;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class BFSData<T extends CharSequence, E extends APathNode<T, E>> {

        public final Queue<E> toVisit = new LinkedList<>();
        public final Map<T, E> visited = new HashMap<>();

        public BFSData(final E node) {
            toVisit.add(node);
            visited.put(node.getValue(), node);
        }

        public boolean isFinished() {
            return toVisit.isEmpty();
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static abstract class APathNode<T extends CharSequence, E extends APathNode<T, E>> {

        protected T value = null;
        protected E previous = null;

        public APathNode() {
            this(null);
        }

        public APathNode(final T value) {
            this(value, null);
        }

        public APathNode(final T value, final E previous) {
            this.value = value;
            this.previous = previous;
        }

        public LinkedList<T> collapse(boolean startsWithRoot) {
            final LinkedList<T> path = new LinkedList<>();
            APathNode<T, E> node = this;
            while (Objects.nonNull(node)) {
                if (startsWithRoot) {
                    path.addLast(node.value);
                } else {
                    path.addFirst(node.value);
                }
                node = node.previous;
            }
            return path;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class PathNode extends APathNode<String, PathNode> {

        public PathNode() {
            this(null);
        }

        public PathNode(final String value) {
            this(value, null);
        }

        public PathNode(final String value, final PathNode previous) {
            super(value, previous);
        }
    }

    public static CHashMapList<String, Integer> searchAll2(final String value, final String[] smalls) {
        final CHashMapList<String, Integer> lookup = new CHashMapList<>();
        int maxLen = value.length();
        final CTrie3.CTrieNode root = createTreeFromStrings(smalls, maxLen).getRoot();
        for (int i = 0; i < maxLen; i++) {
            final List<String> strings = findStringsAtLocation(root, value, i);
            insertIntoHashMap(strings, lookup, i);
        }
        return lookup;
    }

    private static CTrie3 createTreeFromStrings(final String[] smalls, int maxLen) {
        final CTrie3 tree = new CTrie3();
        tree.setRoot("");
        for (final String small : smalls) {
            if (small.length() <= maxLen) {
                tree.setRoot(small);
            }
        }
        return tree;
    }

    private static List<String> findStringsAtLocation(CTrie3.CTrieNode root, final String value, int start) {
        final List<String> strings = new ArrayList<>();
        int index = start;
        while (index < value.length()) {
            root = root.getChild(value.charAt(index));
            if (Objects.isNull(root)) {
                break;
            }
            if (root.isTerminated()) {
                strings.add(value.substring(start, index + 1));
            }
            index++;
        }
        return strings;
    }

    private static void insertIntoHashMap(final List<String> strings, final CHashMapList<String, Integer> lookup, int index) {
        strings.stream().forEach((string) -> {
            lookup.put(string, index);
        });
    }

    public static CHashMapList<String, Integer> searchAll(final String value, final String[] smalls) {
        final CHashMapList<String, Integer> lookup = new CHashMapList<>();
        final CTrie3 tree = createTrieFromString(value);
        for (final String small : smalls) {
            final List<Integer> locations = tree.search(small);
            subtractValue(locations, small.length());
            lookup.put(small, locations);
        }
        return lookup;
    }

    private static CTrie3 createTrieFromString(final String value) {
        final CTrie3 trie = new CTrie3();
        for (int i = 0; i < value.length(); i++) {
            final String suffix = value.substring(i);
            trie.insert(suffix, i);
        }
        return trie;
    }

    private static void subtractValue(final List<Integer> locations, int delta) {
        if (Objects.isNull(locations)) {
            return;
        }
        for (int i = 0; i < locations.size(); i++) {
            locations.set(i, locations.get(i) - delta);
        }
    }

    public static String getLongestToken(final String[] array) {
        final Map<String, Boolean> map = new HashMap<>();
        for (final String elem : array) {
            map.put(elem, true);
        }
        Arrays.sort(array, new CStringUtils.StringLengthComparator());
        for (final String elem : array) {
            if (canBuildWord(elem, true, map)) {
                return elem;
            }
        }
        return null;
    }

    private static boolean canBuildWord(final String value, boolean isOriginalWord, final Map<String, Boolean> map) {
        if (map.containsKey(value) && !isOriginalWord) {
            return map.get(value);
        }
        for (int i = 1; i < value.length(); i++) {
            final String left = value.substring(0, i);
            final String right = value.substring(i);
            if (map.containsKey(left) && map.get(left) && canBuildWord(right, false, map)) {
                return true;
            }
        }
        map.put(value, false);
        return false;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class ParseResult {

        public int invalid;
        public String parsed;

        public ParseResult() {
            this(Integer.MAX_VALUE, " ");
        }

        public ParseResult(int invalid, final String parsed) {
            this.invalid = invalid;
            this.parsed = parsed;
        }
    }

    public static String bestSplit(final Set<String> dictionary, final String sentence) {
        final ParseResult[] memo = new ParseResult[sentence.length()];
        final ParseResult r = split(dictionary, sentence, 0, memo);
        return Objects.isNull(r) ? null : r.parsed;
    }

    private static ParseResult split(final Set<String> dictionary, final String sentence, int start, final ParseResult[] memo) {
        if (start >= sentence.length()) {
            return new ParseResult(0, "");
        }
        if (Objects.nonNull(memo[start])) {
            return memo[start];
        }
        int bestInvalid = Integer.MAX_VALUE;
        String bestParsing = null;
        String partial = "";
        int index = start;
        while (index < sentence.length()) {
            char c = sentence.charAt(index);
            partial += c;
            int invalid = dictionary.contains(partial) ? 0 : partial.length();
            if (invalid < bestInvalid) {
                final ParseResult result = split(dictionary, sentence, index + 1, memo);
                if (invalid + result.invalid < bestInvalid) {
                    bestInvalid = invalid + result.invalid;
                    bestParsing = partial + " " + result.parsed;
                    if (0 == bestInvalid) {
                        break;
                    }
                }
            }
            index++;
        }
        memo[start] = new ParseResult(bestInvalid, bestParsing);
        return memo[start];
    }

    public static LocationPair findClosest(final String[] words, final String word1, final String word2) {
        final LocationPair best = new LocationPair(-1, -1);
        LocationPair current = new LocationPair(-1, -1);
        for (int i = 0; i < words.length; i++) {
            final String word = words[i];
            if (Objects.equals(word, word1)) {
                current.location1 = i;
                best.updateWithMin(current);
            } else if (Objects.equals(word, word2)) {
                current.location2 = i;
                best.updateWithMin(current);
            }
        }
        return best;
    }

    public static LocationPair findClosest2(final String word1, final String word2, final CHashMapList<String, Integer> locations) {
        final List<Integer> locations1 = locations.get(word1);
        final List<Integer> locations2 = locations.get(word2);
        return findMinDistancePair(locations1, locations2);
    }

    public static int findClosest3(final String word1, final String word2) {
        Objects.requireNonNull(word1);
        Objects.requireNonNull(word2);
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = CNumericUtils.min(dp[i - 1][j - 1] + costOfSubstitution(word1.charAt(i - 1), word2.charAt(j - 1)), dp[i - 1][j] + 1, dp[i][j - 1] + 1);
                }
            }
        }

        return dp[word1.length()][word2.length()];
    }

    public static int findClosest4(final String x, final String y) {
        if (Objects.isNull(x) || x.isEmpty()) {
            return y.length();
        }
        if (Objects.isNull(y) || y.isEmpty()) {
            return x.length();
        }
        int substitution = findClosest4(x.substring(1), y.substring(1)) + costOfSubstitution(x.charAt(0), y.charAt(0));
        int insertion = findClosest4(x, y.substring(1)) + 1;
        int deletion = findClosest4(x.substring(1), y) + 1;
        return CNumericUtils.min(substitution, insertion, deletion);
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private static LocationPair findMinDistancePair(final List<Integer> list1, final List<Integer> list2) {
        if (Objects.isNull(list1) || Objects.isNull(list2) || list1.isEmpty() || list2.isEmpty()) {
            return null;
        }
        int index1 = 0;
        int index2 = 0;
        final LocationPair best = new LocationPair(list1.get(0), list2.get(0));
        final LocationPair current = new LocationPair(list1.get(0), list2.get(0));
        while (index1 < list1.size() && index2 < list2.size()) {
            current.setLocations(list1.get(index1), list2.get(index2));
            best.updateWithMin(current);
            if (current.location1 < current.location2) {
                index1++;
            } else {
                index2++;
            }
        }
        return best;
    }

    private static CHashMapList<String, Integer> getWordLocations(final String[] words) {
        final CHashMapList<String, Integer> locations = new CHashMapList<>();
        for (int i = 0; i < words.length; i++) {
            locations.put(words[i], i);
        }
        return locations;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class LocationPair {

        public int location1;
        public int location2;

        @SuppressWarnings("OverridableMethodCallInConstructor")
        public LocationPair(int first, int second) {
            this.setLocations(first, second);
        }

        public void setLocations(int first, int second) {
            this.location1 = first;
            this.location2 = second;
        }

        public void setLocations(final LocationPair location) {
            this.setLocations(location.location1, location.location2);
        }

        public int distance() {
            return Math.abs(location1 - location2);
        }

        public boolean isValid() {
            return this.location1 >= 0 && this.location2 >= 0;
        }

        public void updateWithMin(final LocationPair location) {
            if (!this.isValid() || location.distance() < distance()) {
                this.setLocations(location);
            }
        }
    }

//    public static Map<String, Integer> concatByNames(final Map<String, Integer> names, final String[][] synonyms) {
//        final Graph graph = constructGraph(names);
//        connectEdges(graph, synonyms);
//        final Map<String, Integer> rootNames = getTrueFrequencies(graph);
//        return rootNames;
//    }
//
//    private static Graph constructGraph(final Map<String, Integer> names) {
//        final Graph graph = new Graph();
//        names.entrySet().stream().forEach((entry) -> {
//            String name = entry.getKey();
//            int frequency = entry.getValue();
//            final NameSet group = new NameSet(name, frequency);
//            graph.put(name, group);
//        });
//        return graph;
//    }
//
//    private static void connectEdges(final Graph graph, final String[][] synonyms) {
//        for (String[] entry : synonyms) {
//            final String name1 = entry[0];
//            final String name2 = entry[1];
//            graph.addEdge(name1, name2);
//        }
//    }
//
//    private static Map<String, Integer> getFrequencies(final Graph graph) {
//        final Map<String, Integer> rootNames = new HashMap<>();
//        graph.getNodes().stream()
//                .filter((node) -> (!Objects.equals(CGraphNode.State.VISITED, node.getState())))
//                .forEach((node) -> {
//                    int frequency = getComponentFrequency(node);
//                    final String name = node.getName();
//                    rootNames.put(name, frequency);
//                });
//        return rootNames;
//    }
//
//    private static int getComponentFrequency(final GraphNode node) {
//        if (Objects.equals(CGraphNode.State.VISITED, node.getState())) {
//            return 0;
//        }
//        node.setState(CGraphNode.State.VISITED);
//        int sum = node.getFrequency();
//        sum = node.getAdjacents().stream()
//                .map((child) -> getComponentFrequency(child))
//                .reduce(sum, Integer::sum);
//        return sum;
//    }
    public static Map<String, Integer> concatByNames(final Map<String, Integer> names, final String[][] synonyms) {
        final Map<String, NameSet> groups = constructGroups(names);
        mergeClasses(groups, synonyms);
        return convertToMap(groups);
    }

    private static void mergeClasses(final Map<String, NameSet> groups, final String[][] synonyms) {
        for (final String[] entry : synonyms) {
            String name1 = entry[0];
            String name2 = entry[1];
            final NameSet set1 = groups.get(name1);
            final NameSet set2 = groups.get(name2);
            if (set1 != set2) {
                final NameSet smaller = set2.size() < set1.size() ? set2 : set1;
                final NameSet bigger = set2.size() < set1.size() ? set1 : set2;
                final Set<String> otherNames = smaller.getNames();
                int frequency = smaller.getFrequency();
                bigger.copyNamesWithFrequency(otherNames, frequency);
                otherNames.stream().forEach((name) -> {
                    groups.put(name, bigger);
                });
            }
        }
    }

    private static Map<String, NameSet> constructGroups(final Map<String, Integer> names) {
        final Map<String, NameSet> groups = new HashMap<>();
        names.entrySet().stream().forEach((entry) -> {
            String name = entry.getKey();
            int frequency = entry.getValue();
            final NameSet group = new NameSet(name, frequency);
            groups.put(name, group);
        });
        return groups;
    }

    private static Map<String, Integer> convertToMap(final Map<String, NameSet> groups) {
        final Map<String, Integer> list = new HashMap<>();
        groups.values().stream().forEach((group) -> {
            list.put(group.getRootName(), group.getFrequency());
        });
        return list;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class NameSet {

        private final Set<String> names = new HashSet<>();
        private String rootName;
        private int frequency;

        public NameSet(final String name, int frequency) {
            this.names.add(name);
            this.rootName = name;
            this.frequency = frequency;
        }

        public void copyNamesWithFrequency(final Set<String> names, int frequency) {
            this.names.addAll(names);
            this.frequency += frequency;
        }

        public int size() {
            return this.names.size();
        }
    }

    @SuppressWarnings("null")
    public static Double compute(final String sequence) {
        final List<Term> terms = Term.parseTermSequence(sequence);
        if (Objects.isNull(terms)) {
            return null;
        }
        double result = 0;
        Term processing = null;
        for (int i = 0; i < terms.size(); i++) {
            Term current = terms.get(i);
            Term next = i + 1 < terms.size() ? terms.get(i + 1) : null;
            processing = collapseTerm(processing, current);
            if (Objects.isNull(next) || Objects.equals(Operator.ADD, next.getOperator()) || Objects.equals(Operator.SUBTRACT, next.getOperator())) {
                result = applyOp(result, processing.getOperator(), processing.getNumber());
                processing = null;
            }
        }
        return result;
    }

    private static Term collapseTerm(final Term primary, final Term secondary) {
        if (Objects.isNull(primary)) {
            return secondary;
        }
        if (Objects.isNull(secondary)) {
            return primary;
        }
        double value = applyOp(primary.getNumber(), secondary.getOperator(), secondary.getNumber());
        primary.setNumber(value);
        return primary;
    }

    private static double applyOp(double left, final Operator op, double right) {
        if (Objects.equals(Operator.ADD, op)) {
            return left + right;
        } else if (Objects.equals(Operator.SUBTRACT, op)) {
            return left - right;
        } else if (Objects.equals(Operator.MULTIPLY, op)) {
            return left * right;
        } else if (Objects.equals(Operator.DIVIDE, op)) {
            return left / right;
        }
        return right;
    }

    public static enum Operator {

        ADD, SUBTRACT, MULTIPLY, DIVIDE, BLANK
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Term {

        private double number;
        private Operator operator;

        public Term() {
            this(0.0, Operator.BLANK);
        }

        public Term(double number, final Operator operator) {
            this.number = number;
            this.operator = operator;
        }

        public static List<Term> parseTermSequence(final String sequence) {
            return null;
        }
    }

    public static Double compute2(final String sequence) {
        final Stack<Double> numberStack = new Stack<>();
        final Stack<CString.Operator> operatorStack = new Stack<>();
        for (int i = 0; i < sequence.length(); i++) {
            try {
                double value = parseNextNumber(sequence, i);
                numberStack.push(value);
                i += Double.toHexString(value).length();
                if (i >= sequence.length()) {
                    break;
                }
                final CString.Operator op = parseNextOperator(sequence, i);
                collapseTop(op, numberStack, operatorStack);
                operatorStack.push(op);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        collapseTop(CString.Operator.BLANK, numberStack, operatorStack);
        if (1 == numberStack.size() && 0 == operatorStack.size()) {
            return numberStack.pop();
        }
        return 0.0;
    }

    private static void collapseTop(final CString.Operator futureTop, final Stack<Double> numberStack, final Stack<CString.Operator> operatorStack) {
        while (operatorStack.size() >= 1 && numberStack.size() >= 2) {
            if (priorityOfOperator(futureTop) <= priorityOfOperator(operatorStack.peek())) {
                double second = numberStack.pop();
                double first = numberStack.pop();
                final CString.Operator op = operatorStack.pop();
                double collapsed = applyOp(first, op, second);
                numberStack.push(collapsed);
            } else {
                break;
            }
        }
    }

    private static int priorityOfOperator(final CString.Operator op) {
        switch (op) {
            case ADD:
                return 1;
            case SUBTRACT:
                return 1;
            case MULTIPLY:
                return 2;
            case DIVIDE:
                return 2;
            case BLANK:
                return 0;
        }
        return 0;
    }

    private static double parseNextNumber(final String sequence, int offset) {
        final StringBuffer sb = new StringBuffer();
        while (offset < sequence.length() && Character.isDigit(sequence.charAt(offset))) {
            sb.append(sequence.charAt(offset));
            offset++;
        }
        return Double.parseDouble(sb.toString());
    }

    private static CString.Operator parseNextOperator(final String sequence, int offset) {
        if (offset < sequence.length()) {
            char op = sequence.charAt(offset);
            switch (op) {
                case '+':
                    return CString.Operator.ADD;
                case '-':
                    return CString.Operator.SUBTRACT;
                case '*':
                    return CString.Operator.MULTIPLY;
                case '/':
                    return CString.Operator.DIVIDE;
            }
        }
        return CString.Operator.BLANK;
    }

    public static List<String> getValidT9Words(final String numbers, final CHashMapList<String, String> dictionary) {
        return dictionary.get(numbers);
    }

    private static CHashMapList<String, String> initializeDictionary(final String[] words) {
        final Map<Integer, Character> letterToNumberMap = createLetterToNumberMap();
        final CHashMapList<String, String> wordsToNumbers = new CHashMapList<>();
        for (final String word : words) {
            String numbers = convertToT9(word, letterToNumberMap);
            wordsToNumbers.put(numbers, word);
        }
        return wordsToNumbers;
    }

    private static Map<Integer, Character> createLetterToNumberMap() {
        final Map<Integer, Character> letterToNumberMap = new HashMap<>();
        for (int i = 0; i < T9.letters.length; i++) {
            int[] letters = T9.letters[i];
            if (Objects.nonNull(letters)) {
                for (int letter : letters) {
                    char c = Character.forDigit(i, 10);
                    letterToNumberMap.put(letter, c);
                }
            }
        }
        return letterToNumberMap;
    }

    private static String convertToT9(final String word, final Map<Integer, Character> letterToNumberMap) {
        final StringBuffer sb = new StringBuffer();
        for (int c : word.codePoints().toArray()) {
            if (letterToNumberMap.containsKey(c)) {
                char digit = letterToNumberMap.get(c);
                sb.append(digit);
            }
        }
        return sb.toString();
    }

    private static String convertToT92(final String word, final Map<Integer, Character> letterToNumberMap) {
        final StringBuffer sb = new StringBuffer();
        for (int c : word.codePoints().toArray()) {
            if (letterToNumberMap.containsKey(c)) {
                char digit = letterToNumberMap.get(c);
                sb.append(digit);
            }
        }
        return sb.toString();
    }

    public static List<String> getValidT9Words(final String number, final CTrie trie) {
        final List<String> results = new ArrayList<>();
        getValidWords(number, 0, StringUtils.EMPTY, trie.getRoot(), results);
        return results;
    }

    private static void getValidWords(final String number, int index, final String prefix, final CTrie.CTrieNode<Integer> node, final List<String> results) {
        if (number.length() == index) {
            if (node.isTerminated()) {
                results.add(prefix);
            }
            return;
        }
        char digit = number.charAt(index);
        int[] letters = getT9Chars(digit);
        if (Objects.nonNull(letters)) {
            for (int letter : letters) {
                final CTrie.CTrieNode<Integer> child = node.getChild(letter);
                if (Objects.nonNull(child)) {
                    getValidWords(number, index + 1, prefix + letter, child, results);
                }
            }
        }
    }

    private static int[] getT9Chars(char digit) {
        if (!Character.isDigit(digit)) {
            return null;
        }
        int dig = Character.getNumericValue(digit) - Character.getNumericValue('0');
        return T9.letters[dig];
    }

    private static class T9 {

        public static final int[][] letters = {
            null,
            null,
            {getCodePointAtZero("a"), getCodePointAtZero("b"), getCodePointAtZero("c")},
            {getCodePointAtZero("d"), getCodePointAtZero("e"), getCodePointAtZero("f")},
            {getCodePointAtZero("g"), getCodePointAtZero("h"), getCodePointAtZero("i")},
            {getCodePointAtZero("j"), getCodePointAtZero("k"), getCodePointAtZero("l")},
            {getCodePointAtZero("m"), getCodePointAtZero("n"), getCodePointAtZero("o")},
            {getCodePointAtZero("p"), getCodePointAtZero("q"), getCodePointAtZero("r"), getCodePointAtZero("s")},
            {getCodePointAtZero("t"), getCodePointAtZero("u"), getCodePointAtZero("v")},
            {getCodePointAtZero("w"), getCodePointAtZero("x"), getCodePointAtZero("y"), getCodePointAtZero("z")}
        };

        public static int getCodePointAtZero(final String str) {
            return getCodePoint(str, 0);
        }

        public static int getCodePoint(final String str, int index) {
            return Character.codePointAt(str, index);
        }
    }

    public static boolean doesMatch(final String pattern, final String value) {
        if (pattern.length() == 0) {
            return value.length() == 0;
        }
        char mainChar = pattern.charAt(0);
        char altChar = mainChar == 'a' ? 'b' : 'a';
        int size = value.length();

        int countOfMain = CStringUtils.countOf(pattern, mainChar);
        int countOfAlt = pattern.length() - countOfMain;
        int firstAlt = pattern.indexOf(altChar);
        int maxMainSize = size / countOfMain;

        for (int i = 0; i <= maxMainSize; i++) {
            int remainLength = size - i * countOfMain;
            if (0 == countOfAlt || remainLength % countOfAlt == 0) {
                int altIndex = firstAlt * i;
                int altSize = 0 == countOfAlt ? 0 : remainLength / countOfAlt;
                if (matches(pattern, value, i, altSize, altIndex)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean matches(final String pattern, final String value, int mainSize, int altSize, int firstAlt) {
        int stringIndex = mainSize;
        for (int i = 1; i < pattern.length(); i++) {
            int size = pattern.charAt(i) == pattern.charAt(0) ? mainSize : altSize;
            int offset = pattern.charAt(i) == pattern.charAt(0) ? 0 : firstAlt;
            if (!CStringUtils.isEqual(value, offset, stringIndex, size)) {
                return false;
            }
            stringIndex += size;
        }
        return true;
    }

    public static int getUTFSize(final String value) {
        int len = (Objects.isNull(value)) ? 0 : value.length();
        int l = 0;
        for (int i = 0; i < len; i++) {
            int c = value.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                l++;
            } else if (c > 0x07FF) {
                l += 3;
            } else {
                l += 2;
            }
        }
        return l;
    }
}
