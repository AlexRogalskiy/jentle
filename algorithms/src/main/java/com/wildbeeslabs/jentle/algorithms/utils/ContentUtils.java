package com.wildbeeslabs.jentle.algorithms.utils;

import com.wildbeeslabs.jentle.algorithms.decode.ByteArrayBuffer;
import com.wildbeeslabs.jentle.algorithms.decode.ByteSequence;
import com.wildbeeslabs.jentle.algorithms.decode.Charsets;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Utility methods for converting textual content of a message.
 */
@UtilityClass
public class ContentUtils {

    public static final int DEFAULT_COPY_BUFFER_SIZE = 1024;

    /**
     * Copies the contents of one stream to the other.
     *
     * @param in  not null
     * @param out not null
     * @throws IOException
     */
    public static void copy(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[DEFAULT_COPY_BUFFER_SIZE];
        int inputLength;
        while (-1 != (inputLength = in.read(buffer))) {
            out.write(buffer, 0, inputLength);
        }
    }

    /**
     * Copies the contents of one stream to the other.
     *
     * @param in  not null
     * @param out not null
     * @throws IOException
     */
    public static void copy(final Reader in, final Writer out) throws IOException {
        final char[] buffer = new char[DEFAULT_COPY_BUFFER_SIZE];
        int inputLength;
        while (-1 != (inputLength = in.read(buffer))) {
            out.write(buffer, 0, inputLength);
        }
    }

    public static byte[] buffer(final InputStream in) throws IOException {
        Objects.requireNonNull(in, "Input stream may not be null");
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        copy(in, buf);
        return buf.toByteArray();
    }

    public static String buffer(final Reader in) throws IOException {
        Objects.requireNonNull(in, "Reader may not be null");
        final StringWriter buf = new StringWriter();
        copy(in, buf);
        return buf.toString();
    }

    /**
     * Encodes the specified string into an immutable sequence of bytes using
     * the US-ASCII charset.
     *
     * @param string string to encode.
     * @return encoded string as an immutable sequence of bytes.
     */
    public static ByteSequence encode(CharSequence string) {
        if (Objects.isNull(string)) {
            return null;
        }
        final ByteArrayBuffer buf = new ByteArrayBuffer(string.length());
        for (int i = 0; i < string.length(); i++) {
            buf.append((byte) string.charAt(i));
        }
        return buf;
    }

    /**
     * Encodes the specified string into an immutable sequence of bytes using
     * the specified charset.
     *
     * @param charset Java charset to be used for the conversion.
     * @param string  string to encode.
     * @return encoded string as an immutable sequence of bytes.
     */
    public static ByteSequence encode(Charset charset, CharSequence string) {
        if (Objects.isNull(string)) {
            return null;
        }
        if (Objects.isNull(charset)) {
            charset = Charset.defaultCharset();
        }
        final ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
        final ByteArrayBuffer buf = new ByteArrayBuffer(encoded.remaining());
        buf.append(encoded.array(), encoded.position(), encoded.remaining());
        return buf;
    }

    /**
     * Decodes the specified sequence of bytes into a string using the US-ASCII
     * charset.
     *
     * @param byteSequence sequence of bytes to decode.
     * @return decoded string.
     */
    public static String decode(final ByteSequence byteSequence) {
        if (Objects.isNull(byteSequence)) {
            return null;
        }
        return decode(byteSequence, 0, byteSequence.length());
    }

    /**
     * Decodes the specified sequence of bytes into a string using the specified
     * charset.
     *
     * @param charset      Java charset to be used for the conversion.
     * @param byteSequence sequence of bytes to decode.
     * @return decoded string.
     */
    public static String decode(final Charset charset, final ByteSequence byteSequence) {
        return decode(charset, byteSequence, 0, byteSequence.length());
    }

    /**
     * Decodes a sub-sequence of the specified sequence of bytes into a string
     * using the US-ASCII charset.
     *
     * @param byteSequence sequence of bytes to decode.
     * @param offset       offset into the byte sequence.
     * @param length       number of bytes.
     * @return decoded string.
     */
    public static String decode(final ByteSequence byteSequence, final int offset, final int length) {
        if (Objects.isNull(byteSequence)) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(length);
        for (int i = offset; i < offset + length; i++) {
            buf.append((char) (byteSequence.byteAt(i) & 0xff));
        }
        return buf.toString();
    }

    /**
     * Decodes a sub-sequence of the specified sequence of bytes into a string
     * using the specified charset.
     *
     * @param charset      Java charset to be used for the conversion.
     * @param byteSequence sequence of bytes to decode.
     * @param offset       offset into the byte sequence.
     * @param length       number of bytes.
     * @return decoded string.
     */
    public static String decode(Charset charset, final ByteSequence byteSequence, int offset, int length) {
        if (Objects.isNull(byteSequence)) {
            return null;
        }
        if (Objects.isNull(charset)) {
            charset = Charset.defaultCharset();
        }
        if (byteSequence instanceof ByteArrayBuffer) {
            ByteArrayBuffer bab = (ByteArrayBuffer) byteSequence;
            return decode(charset, bab.buffer(), offset, length);
        } else {
            byte[] bytes = byteSequence.toByteArray();
            return decode(charset, bytes, offset, length);
        }
    }

    private static String decode(Charset charset, byte[] buffer, int offset, int length) {
        return charset.decode(ByteBuffer.wrap(buffer, offset, length))
            .toString();
    }

    public static byte[] toByteArray(final String s, final Charset charset) {
        if (Objects.isNull(s)) {
            return null;
        }
        try {
            return s.getBytes((charset != null ? charset : Charsets.DEFAULT_CHARSET).name());
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public static byte[] toAsciiByteArray(final String s) {
        return toByteArray(s, Charsets.US_ASCII);
    }

    public static String toString(final byte[] b, final Charset charset) {
        if (Objects.isNull(b)) {
            return null;
        }
        try {
            return new String(b, (charset != null ? charset : Charsets.DEFAULT_CHARSET).name());
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public static String toAsciiString(final byte[] b) {
        return toString(b, Charsets.US_ASCII);
    }

    public static String toString(final byte[] b, int off, int len, final Charset charset) {
        if (Objects.isNull(b)) {
            return null;
        }
        try {
            return new String(b, off, len, (Objects.nonNull(charset) ? charset : Charsets.DEFAULT_CHARSET).name());
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public static String toAsciiString(final byte[] b, int off, int len) {
        return toString(b, off, len, Charsets.US_ASCII);
    }

    public static String toString(final ByteArrayBuffer b, final Charset charset) {
        if (Objects.isNull(b)) {
            return null;
        }
        try {
            return new String(b.buffer(), 0, b.length(), (charset != null ? charset : Charsets.DEFAULT_CHARSET).name());
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public static String toAsciiString(final ByteArrayBuffer b) {
        return toString(b, Charsets.US_ASCII);
    }
}
