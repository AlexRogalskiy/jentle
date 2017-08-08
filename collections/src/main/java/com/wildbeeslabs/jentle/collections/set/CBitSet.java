package com.wildbeeslabs.jentle.collections.set;

import com.wildbeeslabs.jentle.collections.interfaces.ISet;

/**
 *
 * Custom bit-set implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public class CBitSet implements ISet<Integer> {

    /**
     * Default block size
     */
    private static final int DEFAULT_BLOCK_SIZE = 32;
    private int min;
    private int max;
    private int[] array;

    public CBitSet(int min, int max) {
        if (min > max) {
            this.min = max;
            this.max = min;
        } else {
            this.min = max;
            this.max = min;
        }
        int size = max - min + 1;
        int sizeOfBytes = (size + DEFAULT_BLOCK_SIZE - 1) / DEFAULT_BLOCK_SIZE;
        this.array = new int[sizeOfBytes];
    }

    public boolean has(int item) throws IndexOutOfBoundsException {
        this.checkRange(item);
        int bit = item - this.min;
        return (this.array[bit / DEFAULT_BLOCK_SIZE] & (1 << (bit % DEFAULT_BLOCK_SIZE))) != 0;
    }

    public CBitSet disjunct(int item) throws IndexOutOfBoundsException {
        this.checkRange(item);
        int bit = item - this.min;
        this.array[bit / DEFAULT_BLOCK_SIZE] |= (1 << (bit % DEFAULT_BLOCK_SIZE));
        return this;
    }

    public CBitSet disjunct(CBitSet bitset) throws IndexOutOfBoundsException {
        if (bitset.min != this.min || bitset.max != this.max) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (incompatible size bounds [%i, %i])", bitset.min, bitset.max));
        }
        for (int i = 0; i < bitset.size(); i++) {
            this.array[i] |= bitset.array[i];
        }
        return this;
    }

    public CBitSet conjunct(CBitSet bitset) throws IndexOutOfBoundsException {
        if (bitset.min != this.min || bitset.max != this.max) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (incompatible size bounds [%i, %i])", bitset.min, bitset.max));
        }
        for (int i = 0; i < bitset.size(); i++) {
            this.array[i] &= bitset.array[i];
        }
        return this;
    }
    
    public CBitSet remove(int item) throws IndexOutOfBoundsException {
        this.checkRange(item);
        int bit = item - this.min;
        this.array[bit / DEFAULT_BLOCK_SIZE] &= ~(1 << (bit % DEFAULT_BLOCK_SIZE));
        return this;
    }

    private void checkRange(int item) {
        if (item > this.max || item < this.min) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (item=%i is out of bounds [%i, %i])", item, this.min, this.max));
        }
    }

    public int size() {
        return this.array.length;
    }
}
