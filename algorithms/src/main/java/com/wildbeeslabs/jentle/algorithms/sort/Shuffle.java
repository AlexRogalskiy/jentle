package com.wildbeeslabs.jentle.algorithms.sort;

import java.util.Random;

import static com.wildbeeslabs.jentle.algorithms.sort.Util.shuffle;

public class Shuffle implements Sorter {

    @Override
    public void sort(final int[] A, final int left, final int right) {
        shuffle(A, left, right, new Random());
    }

    @Override
    public String toString() {
        return "random-shuffle";
    }

}
