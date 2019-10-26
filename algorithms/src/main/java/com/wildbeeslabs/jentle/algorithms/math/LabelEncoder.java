package com.wildbeeslabs.jentle.algorithms.math;

import java.util.Arrays;
import java.util.List;

/**
 * Encode labels of type T to an ArrayList with optional sorting
 * @author Michael Brzustowicz
 * @param <T>
 */
public class LabelEncoder<T> {

    private final List<T> classes;
    
    public LabelEncoder(T[] labels) {
        // Arrays.sort(labels); // can sort first but not required
        classes = Arrays.asList(labels);
    }

    public List<T> getClasses() {
        return classes;
    }

    public int encode(T label) {
        return classes.indexOf(label);
    }
    
    public T decode(int index) {
        return classes.get(index);
    }
    
    public int[] encodeOneHot(T label) {
        int[] oneHot = new int[classes.size()];
        oneHot[encode(label)] = 1;
        return oneHot;
    }
    
    public T decodeOneHot(int[] oneHot) {
        return decode(Arrays.binarySearch(oneHot, 1));
    }
}
