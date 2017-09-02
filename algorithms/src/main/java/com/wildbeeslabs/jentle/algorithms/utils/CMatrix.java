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
public final class CMatrix<T> {

    private static <T> void updateColumn(T[][] matrix, int col, T value) {
        for (T[] row : matrix) {
            row[col] = value;
        }
    }

    private static <T> void updateRow(T[][] matrix, int row, T value) {
        for (int j = 0; j < matrix[row].length; j++) {
            matrix[row][j] = value;
        }
    }

    private CMatrix() {
        // PRIVATE EMPTY CONSTRUCTOR
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
