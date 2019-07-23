package com.wildbeeslabs.jentle.collections.array.impl;

public class BitArray {
    int[] implArray;
    int size;
    final static int INT_SIZE = 32;

    /**
     * Creates a new instance of BitArray
     */
    public BitArray(int size) {
        int implSize = (int) (size / INT_SIZE) + 1;
        implArray = new int[implSize];
    }

    public void set(int pos, boolean value) {
        int implPos = (int) (pos / INT_SIZE);
        int bitMask = 1 << (pos & (INT_SIZE - 1));
        if (value) {
            implArray[implPos] |= bitMask; // set true if true
        } else {
            implArray[implPos] &= ~bitMask;
        }
    }

    public boolean get(int pos) {
        int implPos = (int) (pos / INT_SIZE);
        int bitMask = 1 << (pos & (INT_SIZE - 1));
        return (implArray[implPos] & bitMask) != 0;
    }
}
