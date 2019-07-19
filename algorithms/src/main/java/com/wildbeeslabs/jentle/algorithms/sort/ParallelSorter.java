package com.wildbeeslabs.jentle.algorithms.sort;

import java.util.Comparator;

public abstract class ParallelSorter extends SorterTemplate {
    protected Object[] a;
    private Comparer comparer;

    protected ParallelSorter() {
    }

    abstract public ParallelSorter newInstance(Object[] arrays);

    private int len() {
        return ((Object[]) a[0]).length;
    }

    /**
     * Sort the arrays using the quicksort algorithm.
     *
     * @param index ArrayUtils (column) to sort by
     */
    public void quickSort(int index) {
        quickSort(index, 0, len(), null);
    }

    /**
     * Sort the arrays using the quicksort algorithm.
     *
     * @param index ArrayUtils (column) to sort by
     * @param lo    starting ArrayUtils index (row), inclusive
     * @param hi    ending ArrayUtils index (row), exclusive
     */
    public void quickSort(int index, int lo, int hi) {
        quickSort(index, lo, hi, null);
    }

    /**
     * Sort the arrays using the quicksort algorithm.
     *
     * @param index ArrayUtils (column) to sort by
     * @param cmp   Comparator to use if the specified column is non-primitive
     */
    public void quickSort(int index, Comparator cmp) {
        quickSort(index, 0, len(), cmp);
    }

    /**
     * Sort the arrays using the quicksort algorithm.
     *
     * @param index ArrayUtils (column) to sort by
     * @param lo    starting ArrayUtils index (row), inclusive
     * @param hi    ending ArrayUtils index (row), exclusive
     * @param cmp   Comparator to use if the specified column is non-primitive
     */
    public void quickSort(int index, int lo, int hi, Comparator cmp) {
        chooseComparer(index, cmp);
        super.quickSort(lo, hi - 1);
    }

    /**
     * @param index ArrayUtils (column) to sort by
     */
    public void mergeSort(int index) {
        mergeSort(index, 0, len(), null);
    }

    /**
     * Sort the arrays using an in-place merge sort.
     *
     * @param index ArrayUtils (column) to sort by
     * @param lo    starting ArrayUtils index (row), inclusive
     * @param hi    ending ArrayUtils index (row), exclusive
     */
    public void mergeSort(int index, int lo, int hi) {
        mergeSort(index, lo, hi, null);
    }

    /**
     * Sort the arrays using an in-place merge sort.
     *
     * @param index ArrayUtils (column) to sort by
     * @param lo    starting ArrayUtils index (row), inclusive
     * @param hi    ending ArrayUtils index (row), exclusive
     */
    public void mergeSort(int index, Comparator cmp) {
        mergeSort(index, 0, len(), cmp);
    }

    /**
     * Sort the arrays using an in-place merge sort.
     *
     * @param index ArrayUtils (column) to sort by
     * @param lo    starting ArrayUtils index (row), inclusive
     * @param hi    ending ArrayUtils index (row), exclusive
     * @param cmp   Comparator to use if the specified column is non-primitive
     */
    public void mergeSort(int index, int lo, int hi, Comparator cmp) {
        chooseComparer(index, cmp);
        super.mergeSort(lo, hi - 1);
    }

    private void chooseComparer(int index, Comparator cmp) {
        Object array = a[index];
        Class type = array.getClass().getComponentType();
        if (type.equals(Integer.TYPE)) {
            comparer = new IntComparer((int[]) array);
        } else if (type.equals(Long.TYPE)) {
            comparer = new LongComparer((long[]) array);
        } else if (type.equals(Double.TYPE)) {
            comparer = new DoubleComparer((double[]) array);
        } else if (type.equals(Float.TYPE)) {
            comparer = new FloatComparer((float[]) array);
        } else if (type.equals(Short.TYPE)) {
            comparer = new ShortComparer((short[]) array);
        } else if (type.equals(Byte.TYPE)) {
            comparer = new ByteComparer((byte[]) array);
        } else if (cmp != null) {
            comparer = new ComparatorComparer((Object[]) array, cmp);
        } else {
            comparer = new ObjectComparer((Object[]) array);
        }
    }

    protected int compare(int i, int j) {
        return comparer.compare(i, j);
    }

    interface Comparer {
        int compare(int i, int j);
    }

    static class ComparatorComparer implements Comparer {
        private Object[] a;
        private Comparator cmp;

        public ComparatorComparer(Object[] a, Comparator cmp) {
            this.a = a;
            this.cmp = cmp;
        }

        public int compare(int i, int j) {
            return cmp.compare(a[i], a[j]);
        }
    }

    static class ObjectComparer implements Comparer {
        private Object[] a;

        public ObjectComparer(Object[] a) {
            this.a = a;
        }

        public int compare(int i, int j) {
            return ((Comparable) a[i]).compareTo(a[j]);
        }
    }

    static class IntComparer implements Comparer {
        private int[] a;

        public IntComparer(int[] a) {
            this.a = a;
        }

        public int compare(int i, int j) {
            return a[i] - a[j];
        }
    }

    static class LongComparer implements Comparer {
        private long[] a;

        public LongComparer(long[] a) {
            this.a = a;
        }

        public int compare(int i, int j) {
            long vi = a[i];
            long vj = a[j];
            return (vi == vj) ? 0 : (vi > vj) ? 1 : -1;
        }
    }

    static class FloatComparer implements Comparer {
        private float[] a;

        public FloatComparer(float[] a) {
            this.a = a;
        }

        public int compare(int i, int j) {
            float vi = a[i];
            float vj = a[j];
            return (vi == vj) ? 0 : (vi > vj) ? 1 : -1;
        }
    }

    static class DoubleComparer implements Comparer {
        private double[] a;

        public DoubleComparer(double[] a) {
            this.a = a;
        }

        public int compare(int i, int j) {
            double vi = a[i];
            double vj = a[j];
            return (vi == vj) ? 0 : (vi > vj) ? 1 : -1;
        }
    }

    static class ShortComparer implements Comparer {
        private short[] a;

        public ShortComparer(short[] a) {
            this.a = a;
        }

        public int compare(int i, int j) {
            return a[i] - a[j];
        }
    }

    static class ByteComparer implements Comparer {
        private byte[] a;

        public ByteComparer(byte[] a) {
            this.a = a;
        }

        public int compare(int i, int j) {
            return a[i] - a[j];
        }
    }
}
