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

    public static final Integer DEFAULT_BITS_PER_BYTE = 8;
    public static final Integer DEFAULT_INT_SIZE = Integer.BYTES * DEFAULT_BITS_PER_BYTE;

    private CBitwise() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static boolean getBit(int num, int index) {
        assert (index >= 0);
        return ((num & (1 << index)) != 0);
    }

    public static int setBit(int num, int index) {
        assert (index >= 0);
        return num | (1 << index);
    }

    public static int clearBit(int num, int index) {
        assert (index >= 0);
        int mask = ~(1 << index);
        return num & mask;
    }

    public static int clearBitsMSBthrough1(int num, int index) {
        assert (index >= 0);
        int mask = (1 << index) - 1;
        return num & mask;
    }

    public static int clearBithsIthrough0(int num, int index) {
        assert (index >= 0);
        int mask = ~(-1 >>> ((DEFAULT_INT_SIZE - 1) - index));
        return num & mask;
    }

    public static int updateBit(int num, int i, boolean flag) {
        int value = flag ? 1 : 0;
        int mask = ~(1 << i);
        return (num & mask) | (value << i);
    }

    public static boolean checkOnlyOneBitSet(int num) {
        return clearLeastBit(num) == 0;
    }

    public static int clearLeastBit(int num) {
        return (num & (num - 1));
    }

    public static int toggle(int num, int index) {
        assert (index >= 0);
        int mask = 1 << index;
        if ((num & mask) == 0) {
            num |= mask;
        } else {
            num &= ~mask;
        }
        return num;
    }

    public static int createRangeMask(int i, int j) {
        assert (i >= 0 && j >= 0 && i <= j);
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
            return DEFAULT_INT_SIZE;
        }
        final List<Integer> sequences = getAlternatingSequences(num);
        return findLongestSequence(sequences);
    }

    private static List<Integer> getAlternatingSequences(int num) {
        final List<Integer> sequences = new ArrayList<>();
        int searchingFor = 0;
        int counter = 0;
        for (int i = 0; i < DEFAULT_INT_SIZE; i++) {
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
            return DEFAULT_INT_SIZE;
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

    public static int getLowerMaxWithSameOnesBits(int num) {
        int c = num;
        int c0 = 0;
        int c1 = 0;
        while (((c & 1) == 0) && (c != 0)) {
            c0++;
            c >>= 1;
        }
        while ((c & 1) == 1) {
            c1++;
            c >>= 1;
        }
        if ((c0 + c1 == (DEFAULT_INT_SIZE - 1)) || (c0 + c1 == 0)) {
            return -1;
        }
        int p = c0 + c1;
        num |= (1 << p);
        num &= ~((1 << p) - 1);
        num |= (1 << (c1 - 1)) - 1;
        return num;
    }

    public static int getUpperMinWithSameOnesBits(int num) {
        int temp = num;
        int c0 = 0;
        int c1 = 0;
        while ((temp & 1) == 1) {
            c1++;
            temp >>= 1;
        }
        if (0 == temp) {
            return -1;
        }
        while ((temp & 1) == 0 && (temp != 0)) {
            c0++;
            temp >>= 1;
        }
        int p = c0 + c1;
        num &= ((~0) << (p + 1));
        int mask = (1 << (c1 + 1)) - 1;
        num |= mask << (c0 - 1);
        return num;
    }

    public static int getLowerMaxWithSameOnesBits2(int num) {
        int c = num;
        int c0 = 0;
        int c1 = 0;
        while (((c & 1) == 0) && (c != 0)) {
            c0++;
            c >>= 1;
        }
        while ((c & 1) == 1) {
            c1++;
            c >>= 1;
        }
        if ((c0 + c1 == (DEFAULT_INT_SIZE - 1)) || (c0 + c1 == 0)) {
            return -1;
        }
        return num + (1 << c0) + (1 << (c1 - 1)) - 1;
    }

    public static int getUpperMinWithSameOnesBits2(int num) {
        int temp = num;
        int c0 = 0;
        int c1 = 0;
        while ((temp & 1) == 1) {
            c1++;
            temp >>= 1;
        }
        if (0 == temp) {
            return -1;
        }
        while ((temp & 1) == 0 && (temp != 0)) {
            c0++;
            temp >>= 1;
        }
        return num - (1 << c1) - (1 << (c0 - 1)) + 1;
    }

    public static int bitSwapRequired(int a, int b) {
        int count = 0;
        for (int c = a ^ b; c != 0; c = clearLeastBit(c)) {//c = c >> 1
            count++;
            //count += c & 1;
        }
        return count;
    }

    public static int swapOddEvenBits(int num) {
        return (((num & 0xaaaaaaaa) >>> 1) | ((num & 0x55555555) << 1));
    }

    public static long swapOddEvenBits(long num) {
        return (((num & 0xaaaaaaaaaaaaaaaal) >>> 1) | ((num & 0x5555555555555555l) << 1));
    }

    public static void drawLine(byte[] screen, int width, int x1, int x2, int y) {
        int start_offset = x1 % DEFAULT_BITS_PER_BYTE;
        int first_full_byte = x1 / DEFAULT_BITS_PER_BYTE;
        if (0 != start_offset) {
            first_full_byte++;
        }
        int end_offset = x2 % DEFAULT_BITS_PER_BYTE;
        int last_full_byte = x2 / DEFAULT_BITS_PER_BYTE;
        if (DEFAULT_BITS_PER_BYTE - 1 != end_offset) {
            last_full_byte--;
        }
        for (int b = first_full_byte; b <= last_full_byte; b++) {
            screen[(width / DEFAULT_BITS_PER_BYTE) * y + b] = (byte) 0xFF;
        }
        byte start_mask = (byte) (0xFF >> start_offset);
        byte end_mask = (byte) ~(0xFF >> (end_offset + 1));
        if ((x1 / DEFAULT_BITS_PER_BYTE) == (x2 / DEFAULT_BITS_PER_BYTE)) {
            byte mask = (byte) (start_mask & end_mask);
            screen[(width / DEFAULT_BITS_PER_BYTE) * y + (x1 / DEFAULT_BITS_PER_BYTE)] |= mask;
        } else {
            if (0 != start_offset) {
                int byte_number = (width / DEFAULT_BITS_PER_BYTE) * y + first_full_byte - 1;
                screen[byte_number] |= start_mask;
            }
            if (DEFAULT_BITS_PER_BYTE - 1 != end_offset) {
                int byte_number = (width / DEFAULT_BITS_PER_BYTE) * y + last_full_byte + 1;
                screen[byte_number] |= end_mask;
            }
        }
    }
}
