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

import java.util.Objects;

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
        for (T[] row : matrix) {
            row[col] = value;
        }
    }

    private static <T> void updateRow(final T[][] matrix, int row, final T value) {
        for (int j = 0; j < matrix[row].length; j++) {
            matrix[row][j] = value;
        }
    }

    public static <T> void rotate(final T[][] matrix, int size) {
        if (Objects.isNull(matrix) || Objects.isNull(matrix[0]) || matrix.length != matrix[0].length) {
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
        if (Objects.isNull(matrix) || Objects.isNull(matrix[0])) {
            return;
        }
        boolean rowHasZero = false, colHasZero = false;
        for (T row : matrix[0]) {
            if (Objects.equals(row, value)) {
                rowHasZero = true;
                break;
            }
        }
        for (T[] col : matrix) {
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
}