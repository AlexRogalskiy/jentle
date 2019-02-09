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
package com.wildbeeslabs.jentle.algorithms.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Custom hash utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@UtilityClass
public final class CDigestUtils {

    // Default md5 hash algorithm
    private static final String DEFAULT_MD5_HASH = "MD5";
    // Default sha-256 hash algorithm
    private static final String DEFAULT_SHA256_HASH = "SHA-256";

    public static byte[] md5(final String value) throws NoSuchAlgorithmException {
        Objects.requireNonNull(value);
        return md5(value.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] md5sum(final String filename) throws NoSuchAlgorithmException, IOException {
        Objects.requireNonNull(filename);
        return md5(Files.readAllBytes(Paths.get(filename)));
    }

    public static byte[] md5(byte[] bArray) throws NoSuchAlgorithmException {
        Objects.requireNonNull(bArray);
        final MessageDigest md = MessageDigest.getInstance(CDigestUtils.DEFAULT_MD5_HASH);
        md.update(bArray);
        return md.digest();
        //DigestUtils.md5Hex(password)
    }

    public static String toHexString(byte[] digest) throws NoSuchAlgorithmException {
        return DatatypeConverter.printHexBinary(digest);
    }

    /**
     * Returns the MessageDigest instance
     *
     * @param algorithm supplied digest algorithm
     * @return The hashing algorithm
     */
    public static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            log.error(String.format("ERROR: cannot create digest instance for algorithm=%s, message=%s", algorithm, ex.getMessage()));
        }
        return null;
    }

    public static String generateSHA256Hash(final String p_value) {
        Objects.requireNonNull(p_value);
        final MessageDigest md = getDigest(DEFAULT_SHA256_HASH);
        if (Objects.nonNull(md)) {
            byte[] bytes = p_value.getBytes(StandardCharsets.UTF_8);
            md.update(bytes, 0, bytes.length);
            return CStringUtils.convertBytesToHexString(md.digest());
        }
        return null;
    }
}
