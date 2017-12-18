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

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import com.wildbeeslabs.jentle.collections.exception.OverflowStackException;
//import com.wildbeeslabs.jentle.collections.graph.CGraph.CGraphNode;
import com.wildbeeslabs.jentle.collections.interfaces.IStack;
import com.wildbeeslabs.jentle.collections.map.CHashMapList;
import com.wildbeeslabs.jentle.collections.stack.CBoundStack;
import com.wildbeeslabs.jentle.collections.tree.CTrie;
import com.wildbeeslabs.jentle.collections.tree.CTrie3;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Custom string utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CStringUtils {

    private CStringUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    /**
     * Check whether string consists of unique set of characters
     *
     * @param value - input string
     * @return true - if string is unique, false - otherwise
     */
    public static boolean isUnique(final String value) {
        if (Objects.isNull(value) || value.isEmpty()) {
            return false;
        }
        return value.codePoints().allMatch(new HashSet<>()::add);
    }

    public static boolean permutation(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last) || first.length() != last.length()) {
            return false;
        }
        Map<Character, Long> firstMap = first.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Character, Long> secondMap = last.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return firstMap.equals(secondMap);
    }

    public static boolean isPermutationOfPalindrome(final String value) {
        if (Objects.isNull(value)) {
            return false;
        }
        final AtomicInteger count = new AtomicInteger();
        value.codePoints().mapToObj(ch -> (char) ch).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).forEach((k, v) -> {
            if (v % 2 == 1) {
                if (count.get() > 1) {
                    return;
                }
                count.incrementAndGet();
            }
        });
        return count.get() <= 1;
    }

    public static boolean isSimilarByOneEdit(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last) || Math.abs(first.length() - last.length()) > 1) {
            return false;
        }
        int index1 = 0, index2 = 0;
        String s1 = first.length() < last.length() ? first : last;
        String s2 = first.length() < last.length() ? last : first;
        boolean flag = false;
        while (index2 < s2.length() && index1 < s1.length()) {
            if (s1.codePointAt(index1) != s2.codePointAt(index2)) {
                if (flag) {
                    return false;
                }
                flag = true;
                if (s1.length() == s2.length()) {
                    index1++;
                }
            } else {
                index1++;
            }
            index2++;
        }
        return true;
    }

    public static String compress(final String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        StringBuilder compStr = new StringBuilder(value.length());
        int count = 0, len = value.length();
        for (int i = 0; i < len; i++) {
            count++;
            if (i + 1 >= value.length() || value.codePointAt(i) != value.codePointAt(i + 1)) {
                compStr.append(Character.toChars(value.codePointAt(i)));
                compStr.append(count);
                count = 0;
            }
        }
        return compStr.length() < len ? compStr.toString() : value;
    }

    public static boolean isRotation(final String first, final String last) {
        if (Objects.isNull(first) || Objects.isNull(last)) {
            return false;
        }
        int len = first.length();
        if (len == last.length() && len > 0) {
            return (first + first).contains(last);
        }
        return false;
    }

    public static long countOccurences(final String value, final String searchValue) {
        if (Objects.isNull(value) || Objects.isNull(searchValue)) {
            return -1;
        }
        return value.codePoints().filter(ch -> Objects.equals(ch, searchValue)).count();
        //return StringUtils.countMatches(value, searchValue);
    }

    public static String[] splitByRegex(final String value, final String regex) {
        return split(value, Pattern.quote(regex));
    }

    public static String[] split(final String value, final String delimeter) {
        if (Objects.isNull(value) || Objects.isNull(delimeter)) {
            return null;
        }
        return Optional.ofNullable(value).filter(str -> str.length() != 0).map(str -> str.split(delimeter)).orElse(null);//"\\s+|,\\s*|\\.\\s*"
        //StringUtils.split(value, delimeter);
    }

    public static String removeLastCharacter(final String value) {
        return Optional.ofNullable(value).filter(str -> str.length() != 0).map(str -> str.substring(0, str.length() - 1)).orElse(value);
        //return StringUtils.chop(value);
    }

    public static String generateRandom(int length, boolean useLetters, boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String generateAlphabeticRandom(int length) {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String generateAlphaNumericRandom(int length) {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static <T> List<? extends T> getTokens(final String value, final String delimeter, boolean returnDelims) {
        return Collections.list(new StringTokenizer(value, delimeter, returnDelims)).stream().map(token -> (T) token).collect(Collectors.toList());
    }

    public static Stream<String> streamOfStrings(final String value) {
        return value.codePoints().mapToObj(c -> String.valueOf((char) c));
    }

    public static Stream<Character> streamOfChars(final String value) {
        return value.codePoints().mapToObj(c -> (char) c);
    }

    public static String toString(final InputStream inputStream, final String encoding) {
        final StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(inputStream, writer, encoding);
        } catch (IOException ex) {
            return null;
        }
        return writer.toString();
    }

    public static String toString(final InputStream inputStream) {
        return toString(inputStream, StandardCharsets.UTF_8.name());
    }

    public static List<String> generateParenthesis(int count) {
        assert (count > 0);
        char[] str = new char[count * 2];
        final List<String> list = new ArrayList<>();
        addParanthesis(list, count, count, str, 0);
        return list;
    }

    private static void addParanthesis(final List<String> list, int left, int right, char[] str, int count) {
        if (left < 0 || right < left) {
            return;
        }
        if (0 == left && 0 == right) {
            String s = String.copyValueOf(str);
            list.add(s);
        } else {
            if (left > 0) {
                str[count] = '(';
                addParanthesis(list, left - 1, right, str, count + 1);
            }

            if (right > left) {
                str[count] = ')';
                addParanthesis(list, left, right - 1, str, count + 1);
            }
        }
    }

    public static int countEval(final String s, boolean result, final Map<String, Integer> memo) {
        Objects.requireNonNull(s);
        if (0 == s.length()) {
            return 0;
        }
        if (1 == s.length()) {
            return stringToBool(s) == result ? 1 : 0;
        }
        if (memo.containsKey(result + s)) {
            return memo.get(result + s);
        }
        int ways = 0;
        for (int i = 1; i < s.length(); i += 2) {
            char c = s.charAt(i);
            String left = s.substring(0, i);
            String right = s.substring(i + 1, s.length());

            int leftTrue = countEval(left, true, memo);
            int leftFalse = countEval(left, false, memo);
            int rightTrue = countEval(right, true, memo);
            int rightFalse = countEval(right, false, memo);

            int total = (leftTrue + leftFalse) * (rightTrue + rightFalse);

            int totalTrue = 0;
            if ('^' == c) {
                totalTrue = leftTrue * rightFalse + leftFalse * rightTrue;
            } else if ('&' == c) {
                totalTrue = leftTrue * rightTrue;
            } else if ('|' == c) {
                totalTrue = leftTrue * rightTrue + leftFalse * rightTrue + leftTrue * rightFalse;
            }
            int subWays = result ? totalTrue : total - totalTrue;
            ways += subWays;
        }
        memo.put(result + s, ways);
        return ways;
    }

    private static boolean stringToBool(final String value) {
        return "1".equals(value);
    }

    public static boolean doesMatch(final String pattern, final String value) {
        if (pattern.length() == 0) {
            return value.length() == 0;
        }
        char mainChar = pattern.charAt(0);
        char altChar = mainChar == 'a' ? 'b' : 'a';
        int size = value.length();

        int countOfMain = countOf(pattern, mainChar);
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
            if (!isEqual(value, offset, stringIndex, size)) {
                return false;
            }
            stringIndex += size;
        }
        return true;
    }

    private static boolean isEqual(final String s1, int offset1, int offset2, int size) {
        for (int i = 0; i < size; i++) {
            if (s1.charAt(offset1 + i) != s1.charAt(offset2 + i)) {
                return false;
            }
        }
        return true;
    }

    private static int countOf(final String pattern, char c) {
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == c) {
                count++;
            }
        }
        return count;
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

    private static String converToT9(final String word, final Map<Integer, Character> letterToNumberMap) {
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
    @EqualsAndHashCode(callSuper = false)
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
        final Stack<Operator> operatorStack = new Stack<>();
        for (int i = 0; i < sequence.length(); i++) {
            try {
                double value = parseNextNumber(sequence, i);
                numberStack.push(value);
                i += Double.toHexString(value).length();
                if (i >= sequence.length()) {
                    break;
                }
                Operator op = parseNextOperator(sequence, i);
                collapseTop(op, numberStack, operatorStack);
                operatorStack.push(op);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        collapseTop(Operator.BLANK, numberStack, operatorStack);
        if (1 == numberStack.size() && 0 == operatorStack.size()) {
            return numberStack.pop();
        }
        return 0.0;
    }

    private static void collapseTop(final Operator futureTop, final Stack<Double> numberStack, final Stack<Operator> operatorStack) {
        while (operatorStack.size() >= 1 && numberStack.size() >= 2) {
            if (priorityOfOperator(futureTop) <= priorityOfOperator(operatorStack.peek())) {
                double second = numberStack.pop();
                double first = numberStack.pop();
                final Operator op = operatorStack.pop();
                double collapsed = applyOp(first, op, second);
                numberStack.push(collapsed);
            } else {
                break;
            }
        }
    }

    private static int priorityOfOperator(final Operator op) {
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

    private static Operator parseNextOperator(final String sequence, int offset) {
        if (offset < sequence.length()) {
            char op = sequence.charAt(offset);
            switch (op) {
                case '+':
                    return Operator.ADD;
                case '-':
                    return Operator.SUBTRACT;
                case '*':
                    return Operator.MULTIPLY;
                case '/':
                    return Operator.DIVIDE;
            }
        }
        return Operator.BLANK;
    }

    public static boolean brackets(final String value, int maxdeep) {
        try {
            final IStack<Character> stack = new CBoundStack<>(maxdeep, Character[].class);
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                switch (c) {
                    case '(':
                    case '[':
                    case '{':
                        stack.push(c);
                        break;
                    case ')':
                        if (stack.pop() != '(') {
                            return false;
                        }
                        break;
                    case ']':
                        if (stack.pop() != '[') {
                            return false;
                        }
                        break;
                    case '}':
                        if (stack.pop() != '{') {
                            return false;
                        }
                        break;
                }
            }
            return stack.isEmpty();
        } catch (OverflowStackException | EmptyStackException ex) {
            return false;
        }
    }

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
    @EqualsAndHashCode(callSuper = false)
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
    @EqualsAndHashCode(callSuper = false)
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

    @Data
    @EqualsAndHashCode(callSuper = false)
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

    public static String getLongestToken(final String[] array) {
        final Map<String, Boolean> map = new HashMap<>();
        for (final String elem : array) {
            map.put(elem, true);
        }
        Arrays.sort(array, new StringLengthComparator());
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

    private static class StringLengthComparator implements Comparator<String> {

        public int compare(final String first, final String second) {
            return first.length() - second.length();
        }
    }

    public static CHashMapList<String, Integer> searchAll(final String value, final String[] smalls) {
        final CHashMapList<String, Integer> lookup = new CHashMapList<>();
        final CTrie3 tree = createTrieFromString(value);
        for (String small : smalls) {
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
        for (final String string : strings) {
            lookup.put(string, index);
        }
    }
}
