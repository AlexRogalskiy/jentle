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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * Custom hash utilities implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public final class CHash {

    private static final String DEFAULT_HASH_MD5 = "MD5";

    private CHash() {
        // PRIVATE EMPTY CONSTRUCTOR
    }

    public static String md5(final String value) throws NoSuchAlgorithmException {
        return md5(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String md5sum(final String filename) throws NoSuchAlgorithmException, IOException {
        return md5(Files.readAllBytes(Paths.get(filename)));
    }

    public static String md5(byte[] bArray) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance(DEFAULT_HASH_MD5);
        md.update(bArray);
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest);
        //DigestUtils.md5Hex(password)
    }
}
