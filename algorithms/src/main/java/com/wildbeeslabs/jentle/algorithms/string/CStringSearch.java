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
package com.wildbeeslabs.jentle.algorithms.string;

import com.wildbeeslabs.jentle.algorithms.utils.CNumericUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom string search implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
public final class CStringSearch {

    private CStringSearch() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static int simpleTextSearch(char[] pattern, char[] text) {
        int patternSize = pattern.length;
        int textSize = text.length;
        int i = 0;
        while ((i + patternSize) <= textSize) {
            int j = 0;
            while (text[i + j] == pattern[j]) {
                j += 1;
                if (j >= patternSize) {
                    return i;
                }
            }
            i += 1;
        }
        return -1;
    }

    public static int RabinKarpMethod(char[] pattern, char[] text) {
        int patternSize = pattern.length; // m
        int textSize = text.length; // n
        long prime = CNumericUtils.getBiggerPrime(patternSize);
        long r = 1;
        for (int i = 0; i < patternSize - 1; i++) {
            r *= 2;
            r = r % prime;
        }
        long[] t = new long[textSize];
        t[0] = 0;
        long pfinger = 0;
        for (int j = 0; j < patternSize; j++) {
            t[0] = (2 * t[0] + text[j]) % prime;
            pfinger = (2 * pfinger + pattern[j]) % prime;
        }
        int i = 0;
        boolean passed = false;
        int diff = textSize - patternSize;
        for (i = 0; i <= diff; i++) {
            if (t[i] == pfinger) {
                passed = true;
                for (int k = 0; k < patternSize; k++) {
                    if (text[i + k] != pattern[k]) {
                        passed = false;
                        break;
                    }
                }
                if (passed) {
                    return i;
                }
            }
            if (i < diff) {
                long value = 2 * (t[i] - r * text[i]) + text[i + patternSize];
                t[i + 1] = ((value % prime) + prime) % prime;
            }
        }
        return -1;
    }

    public static int KnuthMorrisPrattSearch(char[] pattern, char[] text) {
        int patternSize = pattern.length;
        int textSize = text.length;
        int i = 0, j = 0;
        int[] shift = KnuthMorrisPrattShift(pattern);
        while ((i + patternSize) <= textSize) {
            while (text[i + j] == pattern[j]) {
                j += 1;
                if (j >= patternSize) {
                    return i;
                }
            }
            if (j > 0) {
                i += shift[j - 1];
                j = Math.max(j - shift[j - 1], 0);
            } else {
                i++;
                j = 0;
            }
        }
        return -1;
    }

    public static int[] KnuthMorrisPrattShift(char[] pattern) {
        int patternSize = pattern.length;
        int[] shift = new int[patternSize];
        shift[0] = 1;
        int i = 1, j = 0;
        while ((i + j) < patternSize) {
            if (pattern[i + j] == pattern[j]) {
                shift[i + j] = i;
                j++;
            } else {
                if (j == 0) {
                    shift[i] = i + 1;
                }
                if (j > 0) {
                    i = i + shift[j - 1];
                    j = Math.max(j - shift[j - 1], 0);
                } else {
                    i = i + 1;
                    j = 0;
                }
            }
        }
        return shift;
    }

    public static int BoyerMooreHorspoolSimpleSearch(char[] pattern, char[] text) {
        int patternSize = pattern.length;
        int textSize = text.length;
        int i = 0, j = 0;
        while ((i + patternSize) <= textSize) {
            j = patternSize - 1;
            while (text[i + j] == pattern[j]) {
                j--;
                if (j < 0) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }

    public static int BoyerMooreHorspoolSearch(char[] pattern, char[] text) {
        int shift[] = new int[256];
        for (int k = 0; k < 256; k++) {
            shift[k] = pattern.length;
        }
        for (int k = 0; k < pattern.length - 1; k++) {
            shift[pattern[k]] = pattern.length - 1 - k;
        }
        int i = 0, j = 0;
        while ((i + pattern.length) <= text.length) {
            j = pattern.length - 1;
            while (text[i + j] == pattern[j]) {
                j -= 1;
                if (j < 0) {
                    return i;
                }
            }
            i = i + shift[text[i + pattern.length - 1]];
        }
        return -1;
    }
}
