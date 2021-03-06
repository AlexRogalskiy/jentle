package com.wildbeeslabs.jentle.algorithms.utils;

import opennlp.tools.ml.model.Context;

import java.util.List;

/**
 * Utility class for simple vector arithmetic.
 */
public class ArrayMath {

    public static double innerProduct(double[] vecA, double[] vecB) {
        if (vecA == null || vecB == null || vecA.length != vecB.length)
            return Double.NaN;

        double product = 0.0;
        for (int i = 0; i < vecA.length; i++) {
            product += vecA[i] * vecB[i];
        }
        return product;
    }

    /**
     * L1-norm
     */
    public static double l1norm(double[] v) {
        double norm = 0;
        for (int i = 0; i < v.length; i++)
            norm += Math.abs(v[i]);
        return norm;
    }

    /**
     * L2-norm
     */
    public static double l2norm(double[] v) {
        return Math.sqrt(innerProduct(v, v));
    }

    /**
     * Inverse L2-norm
     */
    public static double invL2norm(double[] v) {
        return 1 / l2norm(v);
    }

    /**
     * Computes \log(\sum_{i=1}^n e^{x_i}) using a maximum-element trick
     * to avoid arithmetic overflow.
     *
     * @param x input vector
     * @return log-sum of exponentials of vector elements
     */
    public static double logSumOfExps(double[] x) {
        double max = max(x);
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] != Double.NEGATIVE_INFINITY)
                sum += Math.exp(x[i] - max);
        }
        return max + Math.log(sum);
    }

    public static double max(double[] x) {
        int maxIdx = argmax(x);
        return x[maxIdx];
    }

    /**
     * Find index of maximum element in the vector x
     *
     * @param x input vector
     * @return index of the maximum element. Index of the first
     * maximum element is returned if multiple maximums are found.
     */
    public static int argmax(double[] x) {
        if (x == null || x.length == 0) {
            throw new IllegalArgumentException("Vector x is null or empty");
        }

        int maxIdx = 0;
        for (int i = 1; i < x.length; i++) {
            if (x[maxIdx] < x[i])
                maxIdx = i;
        }
        return maxIdx;
    }

    public static void sumFeatures(Context[] context, float[] values, double[] prior) {
        for (int ci = 0; ci < context.length; ci++) {
            if (context[ci] != null) {
                Context predParams = context[ci];
                int[] activeOutcomes = predParams.getOutcomes();
                double[] activeParameters = predParams.getParameters();
                double value = 1;
                if (values != null) {
                    value = values[ci];
                }
                for (int ai = 0; ai < activeOutcomes.length; ai++) {
                    int oid = activeOutcomes[ai];
                    prior[oid] += activeParameters[ai] * value;
                }
            }
        }
    }

    // === Not really related to math ===

    /**
     * Convert a list of Double objects into an ArrayUtils of primitive doubles
     */
    public static double[] toDoubleArray(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    /**
     * Convert a list of Integer objects into an ArrayUtils of primitive integers
     */
    public static int[] toIntArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}
