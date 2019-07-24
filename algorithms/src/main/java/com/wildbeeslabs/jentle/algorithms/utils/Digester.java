package com.wildbeeslabs.jentle.algorithms.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for generating hashes for values using several algorithms. It uses the {@link MessageDigest} as
 * underlying mechanism.
 *
 * @author Allard Buijze
 * @since 2.0
 */
public final class Digester {

    private static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

    private final MessageDigest messageDigest;

    /**
     * Creates a new Digester instance for the given {@code algorithm}.
     *
     * @param algorithm The algorithm to use, e.g. "MD5"
     * @return a fully initialized Digester instance
     */
    public static Digester newInstance(String algorithm) {
        try {
            return new Digester(MessageDigest.getInstance(algorithm));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("This environment doesn't support the MD5 hashing algorithm", e);
        }
    }

    /**
     * Creates a new Digester instance for the MD5 Algorithm
     *
     * @return a Digester instance to create MD5 hashes
     */
    public static Digester newMD5Instance() {
        return newInstance("MD5");
    }

    /**
     * Utility method that creates a hex string of the MD5 hash of the given {@code input}
     *
     * @param input The value to create a MD5 hash for
     * @return The hex representation of the MD5 hash of given {@code input}
     */
    public static String md5Hex(String input) {
        try {
            return newMD5Instance().update(input.getBytes("UTF-8")).digestHex();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("The UTF-8 encoding is not available on this environment", e);
        }
    }

    private Digester(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }

    /**
     * Update the Digester with given {@code additionalData}.
     *
     * @param additionalData The data to add to the digest source
     * @return {@code this} for method chaining
     */
    public Digester update(byte[] additionalData) {
        messageDigest.update(additionalData);
        return this;
    }

    /**
     * Returns the hex representation of the digest of all data that has been provided so far.
     *
     * @return the hex representation of the digest of all data that has been provided so far
     * @see #update(byte[])
     */
    public String digestHex() {
        return hex(messageDigest.digest());
    }

    private static String hex(byte[] hash) {
        return pad(new BigInteger(1, hash).toString(16));
    }

    private static String pad(String md5) {
        if (md5.length() == 32) {
            return md5;
        }
        StringBuilder sb = new StringBuilder(32);
        for (int t = 0; t < 32 - md5.length(); t++) {
            sb.append("0");
        }
        sb.append(md5);
        return sb.toString();
    }

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Returns the SHA1 of the provided data
     *
     * @param data The data to calculate, such as the contents of a file
     * @return The human-readable SHA1
     */
    public static String sha1DigestAsHex(String data) {
        byte[] dataBytes = getDigest("SHA").digest(data.getBytes(UTF8_CHARSET));
        return new String(encodeHex(dataBytes));
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = HEX_CHARS[(0xF0 & data[i]) >>> 4];
            out[j++] = HEX_CHARS[0x0F & data[i]];
        }
        return out;
    }

    /**
     * Creates a new {@link MessageDigest} with the given algorithm. Necessary because {@code MessageDigest} is not
     * thread-safe.
     */
    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
        }
    }
}
