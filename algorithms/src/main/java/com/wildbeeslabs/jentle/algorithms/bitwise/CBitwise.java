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

package com.wildbeeslabs.jentle.algorithms.bitwise;

/*
 *
 * Custom bitwise implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @see
 * 
 # x ^ 0s = x
 # x & 0s = 0
 # x | 0s = x
 # x ^ 1s = ~x
 # x & 1s = x
 # x | 1s = 1s
 # x ^ x = 0
 # x & x = x
 # x | x = x
 * 
 */
public final class CBitwise {

    public static boolean getBit(int num, int i) {
        return ((num & (1 << i)) != 0);
    }

    public static int setBit(int num, int i) {
        return num | (1 << i);
    }

    public static int clearBit(int num, int i) {
        int mask = ~(1 << i);
        return num & mask;
    }

    public static int clearBitsMSBthroughI(int num, int i) {
        int mask = (1 << i) - 1;
        return num & mask;
    }

    public static int clearBithsIthrough0(int num, int i) {
        int mask = ~(-1 >>> (31 - i));
        return num & mask;
    }

    public static int updateBit(int num, int i, boolean flag) {
        int value = flag ? 1 : 0;
        int mask = ~(1 << i);
        return (num & mask) | (value << i);
    }
}
