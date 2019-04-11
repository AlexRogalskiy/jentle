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
package com.wildbeeslabs.jentle.algorithms.matrix;

import com.wildbeeslabs.jentle.algorithms.random.CRandom;
import com.wildbeeslabs.jentle.collections.tree.CTrie3;
import lombok.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Custom matrix utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-09-01
 */
@Slf4j
@UtilityClass
public class CMatrixUtils {

    public static <T> boolean matricesAreEqual(final T[][] m1, final T[][] m2, final Comparator<? super T> cmp) {
        Objects.requireNonNull(m1);
        Objects.requireNonNull(m2);
        if (m1.length != m2.length || m1[0].length != m2[0].length) {
            return false;
        }
        for (int k = 0; k < m1.length; k++) {
            for (int j = 0; j < m1[0].length; j++) {
                if (Objects.compare(m1[k][j], m2[k][j], cmp) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static <T> void updateColumn(final T[][] matrix, int col, final T value) {
        for (final T[] row : matrix) {
            row[col] = value;
        }
    }

    private static <T> void updateRow(final T[][] matrix, int row, final T value) {
        for (int j = 0; j < matrix[row].length; j++) {
            matrix[row][j] = value;
        }
    }

    public static <T> void rotate(final T[][] matrix, int size) {
        Objects.requireNonNull(matrix);
        if (Objects.isNull(matrix[0]) || matrix.length != matrix[0].length) {
            return;
        }
        for (int layer = 0; layer < size / 2; layer++) {
            int first = layer;
            int last = size - layer - 1;
            for (int i = first; i < last; i++) {
                int offset = i - first;
                T top = matrix[first][i];
                matrix[first][i] = matrix[last - offset][first];
                matrix[last - offset][first] = matrix[last][last - offset];
                matrix[last][last - offset] = matrix[i][last];
                matrix[i][last] = top;
            }
        }
    }

    public static <T> void replaceRCByDefault(final T[][] matrix, final T value, final T defaultValue) {
        Objects.requireNonNull(matrix);
        if (Objects.isNull(matrix[0])) {
            return;
        }
        boolean rowHasZero = false, colHasZero = false;
        for (final T row : matrix[0]) {
            if (Objects.equals(row, value)) {
                rowHasZero = true;
                break;
            }
        }
        for (final T[] col : matrix) {
            if (Objects.equals(col[0], value)) {
                colHasZero = true;
                break;
            }
        }
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (Objects.equals(matrix[i][j], value)) {
                    matrix[i][0] = defaultValue;
                    matrix[0][j] = defaultValue;
                }
            }
        }
        for (int i = 1; i < matrix.length; i++) {
            if (Objects.equals(matrix[i][0], defaultValue)) {
                updateRow(matrix, i, defaultValue);
            }
        }
        for (int j = 1; j < matrix[0].length; j++) {
            if (Objects.equals(matrix[0][j], defaultValue)) {
                updateColumn(matrix, j, defaultValue);
            }
        }
        if (rowHasZero) {
            updateRow(matrix, 0, defaultValue);
        }
        if (colHasZero) {
            updateColumn(matrix, 0, defaultValue);
        }
    }

    public static <T> void shuffle(final T[][] matrix) {
        Objects.requireNonNull(matrix);
        if (Objects.isNull(matrix[0])) {
            return;
        }
        int nRows = matrix.length;
        int nColumns = matrix[0].length;
        int num = nRows * nColumns;
        for (int i = 0; i < num; i++) {
            int j = i + CRandom.generateRandomInt(0, num - i);
            if (i != j) {
                int row1 = i / nColumns;
                int column1 = (i - row1 * nColumns) % nColumns;
                T cell1 = matrix[row1][column1];

                int row2 = j / nColumns;
                int column2 = (j - row2 * nColumns) % nColumns;
                T cell2 = matrix[row2][column2];

                matrix[row1][column1] = cell2;
                matrix[row2][column2] = cell1;
            }
        }
    }

    public static <T> boolean findElement(final T[][] matrix, final T elem, final Comparator<? super T> cmp) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);

        int row = 0;
        int col = matrix[0].length - 1;
        while (row < matrix.length && col >= 0) {
            if (Objects.compare(matrix[row][col], elem, cmp) == 0) {
                return true;
            } else if (Objects.compare(matrix[row][col], elem, cmp) > 0) {
                col--;
            } else {
                row++;
            }
        }
        return false;
    }

    public static <T> Coordinate<T> findElement2(final T[][] matrix, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);

        final Coordinate<T> origin = new Coordinate(0, 0);
        final Coordinate<T> dest = new Coordinate(matrix.length - 1, matrix[0].length - 1);
        return findElement(matrix, origin, dest, value, cmp);
    }

    private static <T> Coordinate<T> findElement(final T[][] matrix, final Coordinate<T> origin, final Coordinate<T> dest, final T value, final Comparator<? super T> cmp) {
        if (!origin.inBounds(matrix) || !dest.inBounds(matrix)) {
            return null;
        }
        if (Objects.compare(matrix[origin.row][origin.column], value, cmp) == 0) {
            return origin;
        } else if (!origin.isBefore(dest)) {
            return null;
        }

        final Coordinate<T> start = origin.clone();
        int diagDist = Math.min(dest.row - origin.row, dest.column - origin.column);
        final Coordinate<T> end = new Coordinate<>(start.row + diagDist, start.column + diagDist);
        final Coordinate<T> p = new Coordinate<>(0, 0);

        while (start.isBefore(end)) {
            p.setToAverage(start, end);
            if (Objects.compare(value, matrix[p.row][p.column], cmp) > 0) {
                start.row = p.row + 1;
                start.column = p.column + 1;
            } else {
                end.row = p.row - 1;
                end.column = p.column - 1;
            }
        }
        return partitionAndSearch(matrix, origin, dest, start, value, cmp);
    }

    private static <T> Coordinate<T> partitionAndSearch(final T[][] matrix, final Coordinate<T> origin, final Coordinate<T> dest, final Coordinate<T> pivot, final T value, final Comparator<? super T> cmp) {
        final Coordinate<T> lowerLeftOrigin = new Coordinate<>(pivot.row, origin.column);
        final Coordinate<T> lowerLeftDest = new Coordinate<>(dest.row, pivot.column - 1);
        final Coordinate<T> upperRightOrigin = new Coordinate<>(origin.row, pivot.column);
        final Coordinate<T> upperRightDest = new Coordinate<>(pivot.row - 1, dest.column);

        final Coordinate<T> lowerLeft = findElement(matrix, lowerLeftOrigin, lowerLeftDest, value, cmp);
        if (Objects.isNull(lowerLeft)) {
            return findElement(matrix, upperRightOrigin, upperRightDest, value, cmp);
        }
        return lowerLeft;
    }

    @Data
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Coordinate<T> implements Cloneable {

        public int row;
        public int column;

        public boolean inBounds(final T[][] matrix) {
            if (Objects.isNull(matrix) || Objects.isNull(matrix[0])) {
                return false;
            }
            return (this.row >= 0 && this.column >= 0 && this.row < matrix.length && this.column < matrix[0].length);
        }

        public boolean isBefore(final Coordinate<T> coordinate) {
            if (Objects.isNull(coordinate)) {
                return false;
            }
            return (this.row <= coordinate.row && this.column <= coordinate.column);
        }

        @Override
        @SuppressWarnings({"CloneDeclaresCloneNotSupported", "CloneDoesntCallSuperClone"})
        public Coordinate<T> clone() {
            return new Coordinate<>(this.row, this.column);
        }

        public void setToAverage(final Coordinate<T> min, final Coordinate<T> max) {
            if (Objects.isNull(min) || Objects.isNull(max)) {
                return;
            }
            this.row = (min.row + max.row) / 2;
            this.column = (min.column + max.column) / 2;
        }
    }

    public static <T> Coordinate<T> findElement3(final T[][] matrix, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);

        int l = matrix.length - 1;
        int k = 0;
        while (l >= 0 && k <= matrix[0].length - 1) {
            if (Objects.compare(matrix[l][k], value, cmp) < 0) {
                k++;
            } else if (Objects.compare(matrix[l][k], value, cmp) > 0) {
                l--;
            } else {
                break;
            }
        }
        return (Objects.compare(matrix[l][k], value, cmp) == 0) ? new Coordinate<>(l, k) : null;
    }

    public static <T> boolean checkDiagonal(final T[][] matrix, boolean isMainDiagonal, final Comparator<? super T> cmp) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);

        int row = 0;
        int column = isMainDiagonal ? 0 : matrix[0].length - 1;
        int direction = isMainDiagonal ? 1 : -1;
        final T first = matrix[0][column];
        for (final T[] ignored : matrix) {
            if (Objects.compare(matrix[row][column], first, cmp) != 0) {
                return false;
            }
            row++;
            column += direction;
        }
        return true;
    }

    public static <T> List<Integer> computeAreaSize(final T[][] matrix, final T emptyValue, final Comparator<? super T> cmp) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);

        boolean[][] visited = new boolean[matrix.length][matrix[0].length];
        final List<Integer> areaSize = new ArrayList<>();
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                int size = computeArea(matrix, visited, r, c, emptyValue, cmp);
                if (size > 0) {
                    areaSize.add(size);
                }
            }
        }
        return areaSize;
    }

    private static <T> int computeArea(final T[][] matrix, boolean[][] visited, int row, int col, final T emptyValue, final Comparator<? super T> cmp) {
        if (row < 0 || col < 0 || row >= matrix.length || col >= matrix[0].length || visited[row][col] || Objects.compare(matrix[row][col], emptyValue, cmp) == 0) {
            return 0;
        }
        int size = 1;
        visited[row][col] = true;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                size += computeArea(matrix, visited, row + dr, col + dc, emptyValue, cmp);
            }
        }
        return size;
    }

    public static <T> boolean paintFill(final T[][] screen, int row, int column, final T value) {
        Objects.requireNonNull(screen);
        if (Objects.equals(value, screen[row][column])) {
            return false;
        }
        return paintFill(screen, row, column, screen[row][column], value);
    }

    private static <T> boolean paintFill(final T[][] screen, int row, int column, final T oldValue, final T newValue) {
        if (row < 0 || row >= screen.length || column < 0 || column >= screen[0].length) {
            return false;
        }
        if (Objects.equals(oldValue, screen[row][column])) {
            screen[row][column] = newValue;
            paintFill(screen, row - 1, column, oldValue, newValue);
            paintFill(screen, row + 1, column, oldValue, newValue);
            paintFill(screen, row, column - 1, oldValue, newValue);
            paintFill(screen, row, column + 1, oldValue, newValue);
        }
        return true;
    }

    public static <T> SubSquare findSquare(final T[][] matrix, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);
        for (int i = matrix.length; i >= 1; i--) {
            final SubSquare square = findSquareWithSize(matrix, value, i, cmp);
            if (Objects.nonNull(square)) {
                return square;
            }
        }
        return null;
    }

    private static <T> SubSquare findSquareWithSize(final T[][] matrix, final T value, int squareSize, final Comparator<? super T> cmp) {
        int count = matrix.length - squareSize + 1;
        for (int row = 0; row < count; row++) {
            for (int col = 0; col < count; col++) {
                if (isSquare(matrix, value, row, col, squareSize, cmp)) {
                    return new SubSquare(row, col, squareSize);
                }
            }
        }
        return null;
    }

    private static <T> boolean isSquare(final T[][] matrix, final T value, int row, int col, int size, final Comparator<? super T> cmp) {
        for (int j = 0; j < size; j++) {
            if (Objects.compare(matrix[row][col + j], value, cmp) == 0) {
                return false;
            }
            if (Objects.compare(matrix[row + size - 1][col + j], value, cmp) == 0) {
                return false;
            }
        }
        for (int i = 1; i < size - 1; i++) {
            if (Objects.compare(matrix[row + i][col], value, cmp) == 0) {
                return false;
            }
            if (Objects.compare(matrix[row + i][col + size - 1], value, cmp) == 0) {
                return false;
            }
        }
        return true;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class SubSquare {

        private int row;
        private int col;
        private int size;

        public SubSquare(int row, int col, int size) {
            this.row = row;
            this.col = col;
            this.size = size;
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class SquareCell {

        public int zerosRight;
        public int zerosBelow;

        public SquareCell(int zerosRight, int zerosBelow) {
            this.zerosRight = zerosRight;
            this.zerosBelow = zerosBelow;
        }
    }

    public static <T> SubSquare findSquare2(final T[][] matrix, final T value, final Comparator<? super T> cmp) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);

        final SquareCell[][] processed = processSquare(matrix, value, cmp);
        for (int i = matrix.length; i >= 1; i--) {
            final SubSquare square = findSquareWithSize2(processed, 0, i);
            if (Objects.nonNull(square)) {
                return square;
            }
        }
        return null;
    }

    private static SubSquare findSquareWithSize2(final SquareCell[][] matrix, int value, int squareSize) {
        int count = matrix.length - squareSize + 1;
        for (int row = 0; row < count; row++) {
            for (int col = 0; col < count; col++) {
                if (isSquare2(matrix, value, row, col, squareSize)) {
                    return new SubSquare(row, col, squareSize);
                }
            }
        }
        return null;
    }

    private static boolean isSquare2(final SquareCell[][] matrix, int value, int row, int col, int size) {
        final SquareCell topLeft = matrix[row][col];
        final SquareCell topRight = matrix[row][col + size - 1];
        final SquareCell bottomLeft = matrix[row + size - 1][col];
        if (topLeft.zerosRight < size || topLeft.zerosBelow < size || topRight.zerosBelow < size || bottomLeft.zerosRight < size) {
            return false;
        }
        return true;
    }

    private static <T> SquareCell[][] processSquare(final T[][] matrix, final T value, final Comparator<? super T> cmp) {
        final SquareCell[][] processed = new SquareCell[matrix.length][matrix[0].length];
        for (int r = matrix.length - 1; r >= 0; r--) {
            for (int c = matrix[0].length - 1; c >= 0; c--) {
                int rightZeros = 0;
                int belowZeros = 0;
                if (Objects.compare(matrix[r][c], value, cmp) == 0) {
                    rightZeros++;
                    belowZeros++;
                    if (c + 1 < matrix[0].length) {
                        final SquareCell previous = processed[r][c + 1];
                        rightZeros += previous.zerosRight;
                    }
                    if (r + 1 < matrix.length) {
                        final SquareCell previous = processed[r + 1][c];
                        belowZeros += previous.zerosBelow;
                    }
                }
                processed[r][c] = new SquareCell(rightZeros, belowZeros);
            }
        }
        return processed;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class SubMatrix {

        private int row1;
        private int row2;
        private int col1;
        private int col2;
        private int sum;

        public SubMatrix(int row1, int col1, int row2, int col2, int sum) {
            {
                this.row1 = row1;
                this.col1 = col1;
                this.row2 = row2;
                this.col2 = col2;
                this.sum = sum;
            }

        }
    }

    public static SubMatrix getMaxMatrix(int[][] matrix) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);

        SubMatrix best = null;
        int rowCount = matrix.length;
        int colCount = matrix[0].length;
        int[][] sumThrough = precomputeSums(matrix);
        for (int row1 = 0; row1 < rowCount; row1++) {
            for (int row2 = row1; row2 < colCount; row2++) {
                for (int col1 = 0; col1 < colCount; col1++) {
                    for (int col2 = col1; col2 < colCount; col2++) {
                        int sum = sum(sumThrough, row1, col1, row2, col2);
                        if (Objects.isNull(best) || best.getSum() < sum) {
                            best = new SubMatrix(row1, col1, row2, col2, sum);
                        }
                    }
                }
            }
        }
        return best;
    }

    private static int[][] precomputeSums(int[][] matrix) {
        int[][] sumThrough = new int[matrix.length][matrix[0].length];
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                int left = c > 0 ? sumThrough[r][c - 1] : 0;
                int top = r > 0 ? sumThrough[r - 1][c] : 0;
                int overLap = r > 0 && c > 0 ? sumThrough[r - 1][c - 1] : 0;
                sumThrough[r][c] = left + top - overLap + matrix[r][c];
            }
        }
        return sumThrough;
    }

    private static int sum(int[][] sumThrough, int r1, int c1, int r2, int c2) {
        int topAndLeft = r1 > 0 && c1 > 0 ? sumThrough[r1 - 1][c1 - 1] : 0;
        int left = c1 > 0 ? sumThrough[r2][c1 - 1] : 0;
        int top = r1 > 0 ? sumThrough[r1 - 1][c2] : 0;
        int full = sumThrough[r2][c2];
        return full - left - top + topAndLeft;
    }

    public static SubMatrix getMaxMatrix2(int[][] matrix) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);

        SubMatrix best = null;
        int rowCount = matrix.length;
        int colCount = matrix[0].length;
        for (int rowStart = 0; rowStart < rowCount; rowStart++) {
            int[] partialSum = new int[colCount];
            for (int rowEnd = rowStart; rowEnd < rowCount; rowEnd++) {
                for (int i = 0; i < colCount; i++) {
                    partialSum[i] += matrix[rowEnd][i];
                }
                final Range bestRange = maxSubArray(partialSum, colCount);
                if (Objects.isNull(best) || best.getSum() < bestRange.sum) {
                    best = new SubMatrix(rowStart, bestRange.start, rowEnd, bestRange.end, bestRange.sum);
                }
            }
        }
        return best;
    }

    private static Range maxSubArray(int[] array, int n) {
        Range best = null;
        int start = 0;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += array[i];
            if (Objects.isNull(best) || sum > best.sum) {
                best = new Range(start, i, sum);
            }
            if (sum < 0) {
                start = i + 1;
                sum = 0;
            }
        }
        return best;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Range {

        public int start;
        public int end;
        public int sum;

        public Range(int start, int end, int sum) {
            this.start = start;
            this.end = end;
            this.sum = sum;
        }
    }

    public static void createCrossWord(final String[] list) {
        final WordGroup[] groupList = WordGroup.createWordGroups(list);
        int maxWordLength = groupList.length;
        final CTrie3[] trieList = new CTrie3[maxWordLength];
    }

    private static Rectangle maxRectangle(int maxWordLength, final WordGroup[] groupList, final CTrie3[] trieList) {
        int maxSize = maxWordLength * maxWordLength;
        for (int z = maxSize; z > 0; z--) {
            for (int i = 1; i <= maxWordLength; i++) {
                if (z % i == 0) {
                    int j = z / i;
                    if (j <= maxWordLength) {
                        final Rectangle rectangle = makeRectangle(i, j, groupList, trieList);
                        if (Objects.nonNull(rectangle)) {
                            return rectangle;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static Rectangle makeRectangle(int length, int height, final WordGroup[] groupList, final CTrie3[] trieList) {
        if (Objects.isNull(groupList[length - 1]) || Objects.isNull(groupList[height - 1])) {
            return null;
        }
        if (Objects.isNull(trieList[height - 1])) {
            final List<String> words = groupList[height - 1].getWords();
            trieList[height - 1] = new CTrie3(words);
        }
        return makePartialRectangle(length, height, new Rectangle(length), groupList, trieList);
    }

    private static Rectangle makePartialRectangle(int l, int h, final Rectangle rectangle, final WordGroup[] groupList, final CTrie3[] trieList) {
        if (rectangle.height == h) {
            if (rectangle.isComplete(l, h, groupList[h - 1])) {
                return rectangle;
            }
            return null;
        }
        if (!rectangle.isPartial(l, trieList[h - 1])) {
            return null;
        }
        for (int i = 0; i < groupList[l - 1].length(); i++) {
            final Rectangle orgPlus = rectangle.append(groupList[l - 1].getWord(i));
            final Rectangle rect = makePartialRectangle(l, h, orgPlus, groupList, trieList);
            if (Objects.nonNull(rect)) {
                return rect;
            }
        }
        return null;
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class Rectangle {

        public int height;
        public int length;
        public char[][] matrix;

        public Rectangle(int length) {
            this.height = 0;
            this.length = length;
        }

        public Rectangle(char[][] matrix) {
            this.height = matrix.length;
            this.length = matrix[0].length;
            this.matrix = matrix;
        }

        public char getLetter(int i, int j) {
            return this.matrix[i][j];
        }

        public String getColumn(int i) {
            final StringBuffer sb = new StringBuffer();
            for (int r = 0; r < this.height; r++) {
                sb.append(this.matrix[r][i]);
            }
            return sb.toString();
        }

        public boolean isComplete(int length, int height, final WordGroup groupList) {
            if (this.height == height) {
                for (int i = 0; i < length; i++) {
                    final String col = this.getColumn(i);
                    if (!groupList.containsWord(col)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        public boolean isPartial(int length, final CTrie3 trie) {
            if (this.height == 0) {
                return true;
            }
            for (int i = 0; i < length; i++) {
                final String col = this.getColumn(i);
                if (!trie.contains(col)) {
                    return false;
                }
            }
            return true;
        }

        public Rectangle append(final String s) {
            if (s.length() == this.length) {
                char temp[][] = new char[this.height + 1][this.length];
                for (int i = 0; i < this.height; i++) {
                    System.arraycopy(this.matrix[i], 0, temp[i], 0, this.length);
                }
                s.getChars(0, this.length, temp[this.height], 0);
                return new Rectangle(temp);
            }
            return null;
        }
    }

    @Data
    @EqualsAndHashCode
    @ToString
    public static class WordGroup {

        private final Map<String, Boolean> lookup = new HashMap<>();
        private final List<String> group = new ArrayList<>();

        public boolean containsWord(final String value) {
            return this.lookup.containsKey(value);
        }

        public int length() {
            return this.group.size();
        }

        public String getWord(int i) {
            return this.group.get(i);
        }

        public List<String> getWords() {
            return this.group;
        }

        public void addWord(final String word) {
            if (Objects.nonNull(word)) {
                this.group.add(word);
                this.lookup.put(word, Boolean.TRUE);
            }
        }

        public void setWords(final List<String> words) {
            this.group.clear();
            this.lookup.clear();
            if (Objects.nonNull(words)) {
                this.group.addAll(words);
                words.stream().collect(Collectors.toMap((word) -> word, (word) -> Boolean.TRUE));
            }
        }

        public void removeWord(final String word) {
            if (Objects.nonNull(word)) {
                this.group.remove(word);
                this.lookup.remove(word);
            }
        }

        public static WordGroup[] createWordGroups(final String[] list) {
            int maxWordLength = 0;
            for (final String item : list) {
                if (item.length() > maxWordLength) {
                    maxWordLength = item.length();
                }
            }
            final WordGroup[] groupList = new WordGroup[maxWordLength];
            for (final String item : list) {
                int wordLength = item.length() - 1;
                if (Objects.isNull(groupList[wordLength])) {
                    groupList[wordLength] = new WordGroup();
                }
                groupList[wordLength].addWord(item);
            }
            return groupList;
        }
    }

    public static int[][] randomMatrix(int rows, int columns, int min, int max) {
        assert rows > 0 : "Should be greater than zero";
        assert columns > 0 : "Should be greater than zero";

        final int[][] matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = CRandom.generateRandomInt(min, max);
            }
        }
        return matrix;
    }

    public static <T extends Serializable> T[][] fillMatrix(int rows, int columns, final Class<? extends T> clazz, final T defaultValue) {
        assert rows > 0 : "Should be greater than zero";
        assert columns > 0 : "Should be greater than zero";

        final T[][] matrix = com.wildbeeslabs.jentle.collections.utils.CUtils.newMatrix(clazz, rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = SerializationUtils.clone(defaultValue);
            }
        }
        return matrix;
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

    public static double[][] generateRandomDoubleMatrix(int n, int lowerBound, int upperBound) {
        assert n > 0 : "Should be greater than zero";
        double[][] randomMatrix = new double[n][n];
        IntStream.range(0, n).forEach(i -> IntStream.range(0, n).forEach(j -> randomMatrix[i][j] = CRandom.generateRandomDouble(lowerBound, upperBound)));
        return randomMatrix;
    }

    /**
     * Checks index bounds by lower / upper bounds
     *
     * @param index      - initial input index to check by
     * @param lowerBound - initial input lower bound
     * @param upperBound - initial input upper bound
     */
    private void checkBound(int index, int lowerBound, int upperBound) {
        assert index >= lowerBound && index <= upperBound : String.format("Should be in range [{%s},{%s}]", lowerBound, upperBound);
    }
}
