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
public class CBitwise {

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
