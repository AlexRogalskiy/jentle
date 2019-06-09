package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Hexadecimals {

    protected static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String byteToHexString(final Byte b) {
        int v = b & 0xFF;
        return new String(new char[]{HEX_ARRAY[v >>> 4], HEX_ARRAY[v & 0x0F]});
    }
}
