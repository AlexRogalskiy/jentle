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
package com.wildbeeslabs.jentle.collections.set.impl;

import com.wildbeeslabs.jentle.collections.set.iface.ISet;

import java.util.Iterator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom bit-set2 implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CBitSet2 extends ACSet<Integer> implements ISet<Integer> {

    /**
     * Default max power rate
     */
    protected static final int DEFAULT_MAX_POWER_RATE = 5;
    /**
     * Default max chunk size
     */
    protected static final int DEFAULT_MAX_CHUNK_SIZE = Integer.BYTES * Byte.SIZE;//0x1F;

    protected int[] bitSet;

    public CBitSet2(int size) {
        this.bitSet = new int[(size >> CBitSet2.DEFAULT_MAX_POWER_RATE) + 1];
    }

    public boolean get(final Integer pos) {
        int wordNumber = (pos >> CBitSet2.DEFAULT_MAX_POWER_RATE);
        int bitNumber = (pos & CBitSet2.DEFAULT_MAX_CHUNK_SIZE);
        return (this.bitSet[wordNumber] & (1 << bitNumber)) != 0;
    }

    public void set(final Integer pos) {
        int wordNumber = (pos >> CBitSet2.DEFAULT_MAX_POWER_RATE);
        int bitNumber = (pos & CBitSet2.DEFAULT_MAX_CHUNK_SIZE);
        this.bitSet[wordNumber] |= 1 << bitNumber;
    }

    @Override
    public boolean has(final Integer item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ISet<Integer> disjunct(final Integer item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Integer> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
