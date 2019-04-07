/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
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
package com.wildbeeslabs.jentle.algorithms.format;

import lombok.extern.slf4j.Slf4j;

/**
 * Helper class to handle base64 format operations
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
public final class CBase64Formatter {

    /**
     * Default byte masks
     */
    public static final int MASK = 0x3F;
    public static final int FIRST_MASK = MASK << 18;
    public static final int SECOND_MASK = MASK << 12;
    public static final int THIRD_MASK = MASK << 6;
    public static final int FORTH_MASK = MASK;
    /**
     * Default base64 character set
     */
    public static final byte[] ENCODING = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', '+', '/'};

    private CBase64Formatter() {
    }

    @SuppressWarnings("UnusedAssignment")
    public static byte[] toBase64(byte[] in) {
        int inputLength = in.length;
        int outputLength = (int) Math.floor((4 * inputLength) / 3f) + 3;
        outputLength = outputLength + 2 * (int) Math.floor(outputLength / 76f);
        byte[] results = new byte[outputLength];
        int inputIndex = 0;
        int outputIndex = 0;
        while (inputLength - inputIndex > 2) {
            int one = (toInt(in[inputIndex++]) << 16);
            int two = (toInt(in[inputIndex++]) << 8);
            int three = toInt(in[inputIndex++]);
            int quantum = one | two | three;
            int index = (quantum & FIRST_MASK) >> 18;
            outputIndex = setResult(results, outputIndex, ENCODING[index]);
            index = (quantum & SECOND_MASK) >> 12;
            outputIndex = setResult(results, outputIndex, ENCODING[index]);
            index = (quantum & THIRD_MASK) >> 6;
            outputIndex = setResult(results, outputIndex, ENCODING[index]);
            index = (quantum & FORTH_MASK);
            outputIndex = setResult(results, outputIndex, ENCODING[index]);
        }
        switch (inputLength - inputIndex) {
            case 1:
                int quantum = in[inputIndex++] << 16;
                int index = (quantum & FIRST_MASK) >> 18;
                outputIndex = setResult(results, outputIndex, ENCODING[index]);
                index = (quantum & SECOND_MASK) >> 12;
                outputIndex = setResult(results, outputIndex, ENCODING[index]);
                outputIndex = setResult(results, outputIndex, (byte) '=');
                outputIndex = setResult(results, outputIndex, (byte) '=');
                break;
            case 2:
                quantum = (in[inputIndex++] << 16) + (in[inputIndex++] << 8);
                index = (quantum & FIRST_MASK) >> 18;
                outputIndex = setResult(results, outputIndex, ENCODING[index]);
                index = (quantum & SECOND_MASK) >> 12;
                outputIndex = setResult(results, outputIndex, ENCODING[index]);
                index = (quantum & THIRD_MASK) >> 6;
                outputIndex = setResult(results, outputIndex, ENCODING[index]);
                outputIndex = setResult(results, outputIndex, (byte) '=');
                break;
        }
        return results;
    }

    private static int toInt(byte b) {
        return 0xFF & b;
    }

    private static int setResult(byte[] results, int outputIndex, byte value) {
        results[outputIndex++] = value;
        outputIndex = checkLineLength(results, outputIndex);
        return outputIndex;
    }

    private static int checkLineLength(byte[] results, int outputIndex) {
        if (outputIndex == 76 || outputIndex > 76 && (outputIndex - 2 * Math.floor(outputIndex / 76f - 1)) % 76 == 0) {
            results[outputIndex++] = '\r';
            results[outputIndex++] = '\n';
        }
        return outputIndex;
    }
}
