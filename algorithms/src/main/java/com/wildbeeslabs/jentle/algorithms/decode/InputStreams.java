package com.wildbeeslabs.jentle.algorithms.decode;

import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * CFactoryUtils methods for {@link InputStream} instances backed by binary or textual data that attempt
 * to minimize intermediate copying while streaming data.
 */
@UtilityClass
public final class InputStreams {

    public static InputStream create(final byte[] b, int off, int len) {
        if (b == null) {
            throw new IllegalArgumentException("Byte ArrayUtils may not be null");
        }
        return new BinaryInputStream(ByteBuffer.wrap(b, off, len));
    }

    public static InputStream create(final byte[] b) {
        if (b == null) {
            throw new IllegalArgumentException("Byte ArrayUtils may not be null");
        }
        return new BinaryInputStream(ByteBuffer.wrap(b));
    }

    public static InputStream create(final ByteArrayBuffer b) {
        if (b == null) {
            throw new IllegalArgumentException("Byte ArrayUtils may not be null");
        }
        return new BinaryInputStream(ByteBuffer.wrap(b.buffer(), 0, b.length()));
    }

    public static InputStream create(final ByteBuffer b) {
        if (b == null) {
            throw new IllegalArgumentException("Byte ArrayUtils may not be null");
        }
        return new BinaryInputStream(b);
    }

    public static InputStream createAscii(final CharSequence s) {
        if (s == null) {
            throw new IllegalArgumentException("CharSequence may not be null");
        }
        return new TextInputStream(s, Charsets.US_ASCII, 1024);
    }

    public static InputStream create(final CharSequence s, final Charset charset) {
        if (s == null) {
            throw new IllegalArgumentException("CharSequence may not be null");
        }
        return new TextInputStream(s, charset != null ? charset : Charsets.DEFAULT_CHARSET, 1024);
    }

}
