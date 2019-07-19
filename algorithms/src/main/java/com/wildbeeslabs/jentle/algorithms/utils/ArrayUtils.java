package com.wildbeeslabs.jentle.algorithms.utils;

import io.jenetics.util.RandomRegistry;

import java.util.Random;

/**
 * Helper class which contains ArrayUtils helper methods.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public final class ArrayUtils {
    private ArrayUtils() {
    }

    public static <T> void swap(final T[] array, final int i, final int j) {
        final T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void swap(final int[] array, final int i, final int j) {
        final int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void swap(final long[] array, final int i, final int j) {
        final long temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void swap(final double[] array, final int i, final int j) {
        final double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static <T> T[] revert(final T[] array) {
        for (int i = 0, j = array.length - 1; i < j; ++i, --j) {
            swap(array, i, j);
        }

        return array;
    }

    public static int[] revert(final int[] array) {
        for (int i = 0, j = array.length - 1; i < j; ++i, --j) {
            swap(array, i, j);
        }

        return array;
    }

    public static long[] revert(final long[] array) {
        for (int i = 0, j = array.length - 1; i < j; ++i, --j) {
            swap(array, i, j);
        }

        return array;
    }

    public static double[] revert(final double[] array) {
        for (int i = 0, j = array.length - 1; i < j; ++i, --j) {
            swap(array, i, j);
        }

        return array;
    }

    /**
     * Randomize the {@code ArrayUtils} using the given {@link Random} object. The used
     * shuffling algorithm is from D. Knuth TAOCP, Seminumerical Algorithms,
     * Third edition, page 142, Algorithm S (Selection sampling technique).
     *
     * @param array  the ArrayUtils to shuffle
     * @param random the PRNG
     */
    public static double[] shuffle(final double[] array, final Random random) {
        for (int j = array.length - 1; j > 0; --j) {
            swap(array, j, random.nextInt(j + 1));
        }
        return array;
    }

    public static double[] shuffle(final double[] array) {
        return shuffle(array, RandomRegistry.getRandom());
    }

    public static int[] shuffle(final int[] array, final Random random) {
        for (int j = array.length - 1; j > 0; --j) {
            swap(array, j, random.nextInt(j + 1));
        }
        return array;
    }

    public static int[] shuffle(final int[] array) {
        return shuffle(array, RandomRegistry.getRandom());
    }

}
