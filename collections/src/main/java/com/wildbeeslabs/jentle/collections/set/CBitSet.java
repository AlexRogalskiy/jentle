package com.wildbeeslabs.jentle.collections.set;

import com.wildbeeslabs.jentle.collections.interfaces.ISet;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    /**
     * Default block size (in bits)
     */
    private static final int DEFAULT_BLOCK_SIZE = 32;
    /**
     * Default number size (in bits)
     */
    private static final int DEFAULT_NUMBER_SIZE = 32;
    private int min;
    private int max;
    private int[] array;

    public CBitSet(int min, int max) {
        this(min, max, null);
    }

    public CBitSet(int min, int max, final int[] bitset) {
        if (min > max) {
            this.min = max;
            this.max = min;
        } else {
            this.min = max;
            this.max = min;
        }
        int size = (max - min + 1);
        int numOfBlocks = (size * DEFAULT_NUMBER_SIZE + DEFAULT_BLOCK_SIZE - 1) / DEFAULT_BLOCK_SIZE;
        this.array = new int[numOfBlocks];
        if (null != bitset) {
            System.arraycopy(bitset, 0, this.array, 0, bitset.length);
        }
    }

    public CBitSet(final CBitSet bitset) {
        this(bitset.min, bitset.max, bitset.array);
    }

    @Override
    public boolean has(Integer item) throws IndexOutOfBoundsException {
        this.checkRange(item);
        int bit = item - this.min;
        return (this.array[bit / DEFAULT_BLOCK_SIZE] & (1 << (bit % DEFAULT_BLOCK_SIZE))) != 0;
    }

    @Override
    public ISet<Integer> disjunct(Integer item) throws IndexOutOfBoundsException {
        this.checkRange(item);
        int bit = item - this.min;
        this.array[bit / DEFAULT_BLOCK_SIZE] |= (1 << (bit % DEFAULT_BLOCK_SIZE));
        return this;
    }

    public CBitSet disjunct(final CBitSet bitset) throws IndexOutOfBoundsException {
        if (bitset.min != this.min || bitset.max != this.max) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (incompatible size bounds [%i, %i])", bitset.min, bitset.max));
        }
        for (int i = 0; i < bitset.capacity(); i++) {
            this.array[i] |= bitset.array[i];
        }
        return this;
    }

    public CBitSet conjunct(final CBitSet bitset) throws IndexOutOfBoundsException {
        if (bitset.min != this.min || bitset.max != this.max) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (incompatible size bounds [%i, %i])", bitset.min, bitset.max));
        }
        for (int i = 0; i < bitset.capacity(); i++) {
            this.array[i] &= bitset.array[i];
        }
        return this;
    }

    @Override
    public ISet<Integer> remove(Integer item) throws IndexOutOfBoundsException {
        this.checkRange(item);
        int bit = item - this.min;
        this.array[bit / DEFAULT_BLOCK_SIZE] &= ~(1 << (bit % DEFAULT_BLOCK_SIZE));
        return this;
    }

    public ISet<Integer> remove(final CBitSet bitset) throws IndexOutOfBoundsException {
        if (this.min != bitset.min || this.max != bitset.max) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (incompatible size bounds [%i, %i])", bitset.min, bitset.max));
        }
        for (int i = 0; i < this.capacity(); i++) {
            this.array[i] &= ~bitset.array[i];
        }
        return this;
    }

    public CBitSet inverse() {
        for (int i = 0; i < this.capacity(); i++) {
            this.array[i] = ~this.array[i];
        }
        return this;
    }

    public CBitSet or(final CBitSet bitset) throws IndexOutOfBoundsException {
        if (this.min != bitset.min || this.max != bitset.max) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (incompatible size bounds [%i, %i])", bitset.min, bitset.max));
        }
        return this.disjunct(bitset);
    }

    public CBitSet and(final CBitSet bitset) throws IndexOutOfBoundsException {
        if (this.min != bitset.min || this.max != bitset.max) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (incompatible size bounds [%i, %i])", bitset.min, bitset.max));
        }
        return this.conjunct(bitset);
    }

    public ISet<Integer> diff(final CBitSet bitset) throws IndexOutOfBoundsException {
        if (this.min != bitset.min || this.max != bitset.max) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (incompatible size bounds [%i, %i])", bitset.min, bitset.max));
        }
        return this.remove(bitset);
    }

    public CBitSet not(final CBitSet bitset) {
        return this.inverse();
    }

    private void checkRange(int item) {
        if (item > this.max || item < this.min) {
            throw new IndexOutOfBoundsException(String.format("ERROR: CBitSet (item=%i is out of bounds [%i, %i])", item, this.min, this.max));
        }
    }

    public int capacity() {
        return this.array.length;
    }
    
    @Override
    public int size() {
        int sum = 0;
        for(int i=0; i<this.capacity(); i++) {
            int b = this.array[i];
            b = b - ((b >>> 1) & 0x55555555);
            b = (b & 0x33333333) + ((b >>> 2) & 0x33333333);
            sum += (((b + (b >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
        }
        return sum;
    }

    @Override
    public String toString() {
        return String.format("CBitSet {data: %s, min: %i, max: %i}", this.array, this.min, this.max);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        final CBitSet other = (CBitSet) obj;
        if (this.min != other.min) {
            return false;
        }
        if (this.max != other.max) {
            return false;
        }
        if (!Arrays.equals(this.array, other.array)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + this.min;
        hash = 61 * hash + this.max;
        hash = 61 * hash + Arrays.hashCode(this.array);
        return hash;
    }

    @Override
    public Iterator<Integer> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
