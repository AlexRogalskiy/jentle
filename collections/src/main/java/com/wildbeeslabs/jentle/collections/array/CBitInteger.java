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
package com.wildbeeslabs.jentle.collections.array;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom bit integer array implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CBitInteger {

    /**
     * Default number size (in bits)
     */
    private static final int DEFAULT_NUMBER_SIZE = Integer.BYTES * Byte.SIZE;
    private boolean[] bits;

    public CBitInteger() {
        this.bits = new boolean[CBitInteger.DEFAULT_NUMBER_SIZE];
    }

    public CBitInteger(int value) {
        this.bits = new boolean[CBitInteger.DEFAULT_NUMBER_SIZE];
        for (int j = 0; j < CBitInteger.DEFAULT_NUMBER_SIZE; j++) {
            if (((value >> j) & 1) == 1) {
                this.bits[CBitInteger.DEFAULT_NUMBER_SIZE - 1 - j] = true;
            } else {
                this.bits[CBitInteger.DEFAULT_NUMBER_SIZE - 1 - j] = false;
            }
        }
    }

    /**
     * Returns k-th most-significant bit
     *
     * @param k - index value
     * @return k-th most-significant bit
     */
    public int fetch(int k) {
        this.checkRange(k);
        if (this.bits[k]) {
            return 1;
        }
        return 0;
    }

    /**
     * Sets k-th most-significant bit
     *
     * @param k - index value
     * @param bitValue - value to set
     */
    public void set(int k, int bitValue) {
        this.checkRange(k);
        if (bitValue == 0) {
            this.bits[k] = false;
        }
        this.bits[k] = true;
    }

    /**
     * Sets k-th most-significant bit.
     *
     * @param k - index value
     * @param bitValue - value to set (char)
     */
    public void set(int k, char bitValue) {
        this.checkRange(k);
        if (bitValue == '0') {
            this.bits[k] = false;
        }
        this.bits[k] = true;
    }

    /**
     * Sets k-th most-significant bit.
     *
     * @param k - index value
     * @param bitValue - value to set
     */
    public void set(int k, boolean bitValue) {
        this.checkRange(k);
        this.bits[k] = bitValue;
    }

    /**
     * Swaps bit values of the current bit array
     *
     * @param number - number to swap values with
     */
    public void swapValues(final CBitInteger number) {
        for (int i = 0; i < CBitInteger.DEFAULT_NUMBER_SIZE; i++) {
            int temp = number.fetch(i);
            number.set(i, this.fetch(i));
            this.set(i, temp);
        }
    }

    /**
     * Returns integer representation of the current array of bits
     *
     * @return
     */
    public int toInt() {
        int number = 0;
        for (int j = CBitInteger.DEFAULT_NUMBER_SIZE - 1; j >= 0; j--) {
            number = number | this.fetch(j);
            if (j > 0) {
                number = number << 1;
            }
        }
        return number;
    }

    /**
     * Returns size of the current array
     *
     * @return size of the current array
     */
    public int size() {
        return this.bits.length;
    }

    /**
     * Checks range of input index value
     *
     * @param index - index value
     * @throws IndexOutOfBoundsException
     */
    protected void checkRange(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException(String.format("ERROR: %s (index=%d is out of bounds [0, %d])", this.getClass().getName(), index, this.size() - 1));
        }
    }
}
