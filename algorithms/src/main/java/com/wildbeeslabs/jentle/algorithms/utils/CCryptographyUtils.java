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
package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Custom cryptography utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@Slf4j
@UtilityClass
public class CCryptographyUtils {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    public static final BigInteger LONG_OFFSET = MAX_LONG.add(BigInteger.ONE);

    public static final long MASK = 0xffffffffL;
    public static final int DELTA = 0x9e3779b9;

    /**
     * Encode long value using the given alphabet.
     * <p>
     * If the value is negative, then it is first transformed into BigDecimal that is larger than Long.MAX_VALUE,
     * so we can decode it later in a unambiguous way using default {@link #ALPHABET}.
     *
     * @param value a value to encode
     * @return encoded value
     */
    public static String encode(final long value) {
        return encode(value, ALPHABET);
    }

    /**
     * Encode long value using the given alphabet.
     * <p>
     * If the value is negative, then it is first transformed into BigDecimal that is larger than Long.MAX_VALUE,
     * so we can decode it later in a unambiguous way.
     *
     * @param input a value to encode
     * @param value alphabet. If not specified (empty or null), then default {@link #ALPHABET} value will be used.
     * @return encoded value
     */
    public static String encode(final long input, final String value) {
        final String alphabet = StringUtils.defaultIfBlank(value, ALPHABET);
        int base = alphabet.length();

        BigInteger n = BigInteger.valueOf(input);
        if (n.compareTo(BigInteger.ZERO) < 0) {
            n = n.negate().add(LONG_OFFSET);
        }
        StringBuilder output = new StringBuilder();
        BigInteger b = BigInteger.valueOf(base);
        do {
            output.insert(0, (alphabet.charAt(n.mod(b).intValue())));
            n = n.divide(b);
        }
        while (n.compareTo(BigInteger.ZERO) > 0);

        return output.toString();
    }

    /**
     * Decodes a string encoded with the {@link #encode(long, String)} method using default {@link #ALPHABET}.
     *
     * @param s a string to decode
     * @return decoded long value.
     */
    public static long decode(final String s) {
        return decode(s, ALPHABET);
    }

    /**
     * Decodes a string encoded with the {@link #encode(long, String)} method.
     *
     * @param s     a string to decode
     * @param value alphabet, should be the same as used for decoding.
     *              If not specified (empty or null), then default {@link #ALPHABET} value will be used.
     * @return decoded long value.
     */
    public static long decode(final String s, final String value) {
        if (StringUtils.isBlank(s)) {
            return 0L;
        }

        final String alphabet = StringUtils.defaultIfBlank(value, ALPHABET);
        int base = alphabet.length();

        BigInteger result = BigInteger.ZERO;
        BigInteger b = BigInteger.valueOf(base);
        for (int i = 0; i < s.length(); i++) {
            int v = alphabet.indexOf(s.charAt(i));
            result = result.multiply(b).add(BigInteger.valueOf(v));
        }
        if (result.compareTo(MAX_LONG) > 0) {
            result = result.subtract(LONG_OFFSET).negate();
        }
        return result.longValue();
    }

    /**
     * CJvmUtils implementation of <a href="https://en.wikipedia.org/wiki/XTEA">XTEA</a> block cipher, which is a 64-bit
     * version of a <a href="http://en.wikipedia.org/wiki/Feistel_cipher">Feinstel cipher</a>.
     * <p>
     * This implementation is converted from the plpgsql code: <a href="https://wiki.postgresql.org/wiki/XTEA">
     * https://wiki.postgresql.org/wiki/XTEA</a>. See also basic
     * <a href="https://wiki.postgresql.org/wiki/Pseudo_encrypt">Feistel cipher implementation</a>
     *
     * @param rounds  number of iterations, usually 64
     * @param value   value to encrypt/decrypt
     * @param key     encryption key; this string must have exact length 16 characters
     * @param encrypt true for encrypt, false for decrypt
     * @return encrypted/decrypted value
     */
    public static long xtea(final int rounds, final long value, final String key, final boolean encrypt) {
        assert (key != null && key.length() == 16) : "Crypt key must be 16 bytes long";
        byte[] b = key.getBytes();
        int[] k = new int[4];
        for (int i = 0; i < 4; i++) {
            long bb = 0;
            for (int j = 0; j < 4; j++) {
                bb = (bb << 8) | b[i * 4 + j];
            }
            k[i] = (int) (bb > 0x7fffffff ? bb - 0x100000000L : bb);
        }
        return xtea(rounds, value, k, encrypt);
    }

    /**
     * CJvmUtils implementation of <a href="https://en.wikipedia.org/wiki/XTEA">XTEA</a> block cipher, which is a 64-bit
     * version of a <a href="http://en.wikipedia.org/wiki/Feistel_cipher">Feinstel cipher</a>.
     * <p>
     * This implementation is converted from the plpgsql code: <a href="https://wiki.postgresql.org/wiki/XTEA">
     * https://wiki.postgresql.org/wiki/XTEA</a>. See also basic
     * <a href="https://wiki.postgresql.org/wiki/Pseudo_encrypt">Feistel cipher implementation</a>
     *
     * @param rounds  number of iterations, usually 64
     * @param value   value to encrypt/decrypt
     * @param key     encryption key; this string must have exact size 4 elements
     * @param encrypt true for encrypt, false for decrypt
     * @return encrypted/decrypted value
     */
    public static long xtea(final int rounds, final long value, final int[] key, final boolean encrypt) {
        assert (key != null && key.length == 4) : "Crypt key array must have be 4 elements long";

        long[] k = new long[key.length];
        for (int i = 0; i < key.length; i++) {
            k[i] = key[i] >= 0 ? key[i] : (key[i] + MASK + 1);
        }

        long v1 = ((value >> 32) & MASK);
        long v0 = (value & MASK);
        if (encrypt) {
            long sum = 0;
            for (int i = 0; i < rounds; i++) {
                v0 = (v0 + ((((v1 << 4) & MASK ^ (v1 >> 5)) + v1) & MASK ^ (sum + k[(int) ((sum & 3))]) & MASK)) & MASK;
                sum = (sum + DELTA) & MASK;
                v1 = (v1 + ((((v0 << 4) & MASK ^ (v0 >> 5)) + v0) & MASK ^ (sum + k[(int) (((sum >> 11) & 3))]) & MASK)) & MASK;
            }
        } else {
            long sum = ((DELTA * rounds) & MASK);

            for (int i = 0; i < rounds; i++) {
                v1 = (v1 - ((((v0 << 4) & MASK ^ (v0 >> 5)) + v0) & MASK ^ (sum + k[(int) (((sum >> 11) & 3))]) & MASK)) & MASK;
                sum = (sum - DELTA) & MASK;
                v0 = (v0 - ((((v1 << 4) & MASK ^ (v1 >> 5)) + v1) & MASK ^ (sum + k[(int) ((sum & 3))]) & MASK)) & MASK;

            }
        }
        return (v1 << 32) | v0;
    }

    /**
     * Implementation of simple <a href="https://en.wikipedia.org/wiki/Feistel_cipher">Feistel cipher</a>. This cipher
     * can be applied for 32-bit integers only in contrast to {@link #xtea(int, long, int[], boolean)} which can be
     * applied to 64-bit long values as well.
     * <p>
     * This implementation is converted from the plpgsql code: <a href="https://wiki.postgresql.org/wiki/Pseudo_encrypt">
     * https://wiki.postgresql.org/wiki/Pseudo_encrypt</a>.
     *
     * @param value  value to encrypt/decrypt
     * @param rounds number of iterations, usually 3
     * @return encrypted/decrypted value
     */
    public static int feistel(final int value, final int rounds, final int[] key) {
        assert (key != null && key.length == 3) : "Crypt key array must have be 3 elements long";

        long l1 = (value >> 16) & 0xffff;
        long r1 = value & 0xffff;
        long l2, r2;

        for (int i = 0; i < rounds; i++) {
            l2 = r1;
            // as described at the https://wiki.postgresql.org/wiki/Pseudo_encrypt, the following part of code:
            // (((key[0] * r1 + key[1]) % key[2]) / (double) key[2])
            // could be any function that return values in the [0; 1] range
            r2 = l1 ^ Math.round((((key[0] * r1 + key[1]) % key[2]) / (double) key[2]) * 32767);
            l1 = l2;
            r1 = r2;
        }
        return (int) ((r1 << 16) + l1);
    }

    //SHA1PRNG
    public static String generateRandomPassword(final String algorithm) {
        Objects.requireNonNull(algorithm, "Algorithm should be null!");
        try {
            final SecureRandom random = SecureRandom.getInstance(algorithm);
            byte[] passwordBytes = new byte[16];
            random.nextBytes(passwordBytes);
            return new String(Base64.encodeBase64(passwordBytes), StandardCharsets.UTF_8).replace("=", "");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to load SHA1PRNG", e);
        }
    }
}
