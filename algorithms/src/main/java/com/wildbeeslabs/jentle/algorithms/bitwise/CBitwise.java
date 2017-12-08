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

import java.util.ArrayList;
import java.util.List;

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
public class CBitwise {

    private CBitwise() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static boolean getBit(int num, int index) {
        if (index < 0) {
            return false;
        }
        return ((num & (1 << index)) != 0);
    }

    public static int setBit(int num, int index) {
        if (index < 0) {
            return num;
        }
        return num | (1 << index);
    }

    public static int clearBit(int num, int index) {
        if (index < 0) {
            return num;
        }
        int mask = ~(1 << index);
        return num & mask;
    }

    public static int clearBitsMSBthroughI(int num, int index) {
        if (index < 0) {
            return num;
        }
        int mask = (1 << index) - 1;
        return num & mask;
    }

    public static int clearBithsIthrough0(int num, int index) {
        if (index < 0) {
            return num;
        }
        int mask = ~(-1 >>> (31 - index));
        return num & mask;
    }

    public static int updateBit(int num, int i, boolean flag) {
        int value = flag ? 1 : 0;
        int mask = ~(1 << i);
        return (num & mask) | (value << i);
    }

    public static boolean checkOnlyOneBitSet(int num) {
        return (num & (num - 1)) == 0;
    }

    public static int toggle(int num, int index) {
        if (index < 0) {
            return num;
        }
        int mask = 1 << index;
        if ((num & mask) == 0) {
            num |= mask;
        } else {
            num &= ~mask;
        }
        return num;
    }

    public static int createRangeMask(int n, int i, int j) {
        int allOnes = ~0;
        int left = allOnes << (j + 1);
        int right = ((1 << i) - 1);
        return left | right;
    }

    public static String fractionToBinaryFormat(double num, boolean isLimitedBy32Bits) {
        if (num >= 1 || num <= 0) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        while (num > 0) {
            if (isLimitedBy32Bits && buffer.length() >= 32) {
                return null;
            }
            double r = num * 2;
            if (r >= 1) {
                buffer.append(1);
                num = r - 1;
            } else {
                buffer.append(0);
                num = r;
            }
        }
        return buffer.toString();
    }

    public static String fractionToBinaryFormat2(double num, boolean isLimitedBy32Bits) {
        if (num >= 1 || num <= 0) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        double fraction = 0.5;
        while (num > 0) {
            if (isLimitedBy32Bits && buffer.length() >= 32) {
                return null;
            }
            if (num >= fraction) {
                buffer.append(1);
                num -= fraction;
            } else {
                buffer.append(0);
            }
            fraction /= 2;
        }
        return buffer.toString();
    }

    public static int longestSequence(int num) {
        if (-1 == num) {
            return Integer.BYTES * 8;
        }
        final List<Integer> sequences = getAlternatingSequences(num);
        return findLongestSequence(sequences);
    }

    private static List<Integer> getAlternatingSequences(int num) {
        final List<Integer> sequences = new ArrayList<>();
        int searchingFor = 0;
        int counter = 0;
        for (int i = 0; i < Integer.BYTES * 8; i++) {
            if ((num & 1) != searchingFor) {
                sequences.add(counter);
                searchingFor = num & 1;
                counter = 0;
            }
            counter++;
            num >>>= 1;
        }
        sequences.add(counter);
        return sequences;
    }

    private static int findLongestSequence(final List<Integer> sequence) {
        int maxSequence = 1;
        for (int i = 0; i < sequence.size(); i += 2) {
            int zerosSequence = sequence.get(i);
            int onesSequenceRight = i - 1 >= 0 ? sequence.get(i - 1) : 0;
            int onesSequenceLeft = i + 1 < sequence.size() ? sequence.get(i + 1) : 0;
            int currentSequence = 0;
            if (1 == zerosSequence) {
                currentSequence = onesSequenceLeft + 1 + onesSequenceRight;
            } else if (zerosSequence > 1) {
                currentSequence = 1 + Math.max(onesSequenceRight, onesSequenceLeft);
            } else if (0 == zerosSequence) {
                currentSequence = Math.max(onesSequenceRight, onesSequenceLeft);
            }
            maxSequence = Math.max(currentSequence, maxSequence);
        }
        return maxSequence;
    }

    public static int flipBit(int num) {
        if (0 == ~num) {
            return Integer.BYTES * 8;
        }
        int currentLength = 0;
        int previousLength = 0;
        int maxLength = 1;
        while (num != 0) {
            if ((num & 1) == 1) {
                currentLength++;
            } else if ((num & 1) == 0) {
                previousLength = ((num & 2) == 0) ? 0 : currentLength;
                currentLength = 0;
            }
            maxLength = Math.max(previousLength + currentLength + 1, maxLength);
            num >>>= 1;
        }
        return maxLength;
    }
}
