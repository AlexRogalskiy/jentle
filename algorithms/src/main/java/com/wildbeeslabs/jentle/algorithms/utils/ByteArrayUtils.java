package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;

@UtilityClass
public class ByteArrayUtils {

    // big-endian
    public static void writeInt(byte[] byteArray, int offset, int i) {
        for (int j = 0; j < 4; j++) {
            int shift = 24 - j * 8;
            byteArray[offset + j] = (byte) (i >>> shift);
        }
    }

    public static void writeInt(ByteArrayOutputStream baos, int i) {
        for (int j = 0; j < 4; j++) {
            int shift = 24 - j * 8;
            baos.write((byte) (i >>> shift));
        }
    }

    // big-endian
    public static int readInt(byte[] byteArray, int offset) {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            int shift = 24 - j * 8;
            i += (byteArray[offset + j] & 0xFF) << shift;
        }
        return i;
    }

    public static String toHexString(byte[] ba) {
        StringBuilder sbuf = new StringBuilder();
        for (byte b : ba) {
            String s = Integer.toHexString((int) (b & 0xff));
            if (s.length() == 1) {
                sbuf.append('0');
            }
            sbuf.append(s);
        }
        return sbuf.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] ba = new byte[len / 2];

        for (int i = 0; i < ba.length; i++) {
            int j = i * 2;
            int t = Integer.parseInt(s.substring(j, j + 2), 16);
            byte b = (byte) (t & 0xFF);
            ba[i] = b;
        }
        return ba;
    }
}
