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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * Custom matrix utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-09-01
 * @param <T>
 *
 */
public final class CMatrixUtils<T> {

    private CMatrixUtils() {
        // PRIVATE EMPTY CONSTRUCTOR
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
            int j = i + CNumericUtils.generateRandomInt(0, num - i);
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

        final Coordinate<T> start = (Coordinate<T>) origin.clone();
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
    @EqualsAndHashCode(callSuper = false)
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

    public static <T> boolean checkDiagonal(final T[][] matrix, boolean isMainDiagonal, final Comparator<? super T> cmp) {
        Objects.requireNonNull(matrix);
        Objects.requireNonNull(matrix[0]);
        int row = 0;
        int column = isMainDiagonal ? 0 : matrix[0].length - 1;
        int direction = isMainDiagonal ? 1 : -1;
        final T first = matrix[0][column];
        for (int i = 0; i < matrix.length; i++) {
            if (Objects.compare(matrix[row][column], first, cmp) != 0) {
                return false;
            }
            row += 1;
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
}
