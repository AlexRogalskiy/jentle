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

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Custom byte utilities implementation
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 */
@UtilityClass
public class CByteUtils {

    /**
     * Concatenate the given {@code byte} arrays into one, with overlapping array elements included twice.
     * <p/>
     * The order of elements in the original arrays is preserved.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @return the new array.
     */
    public static byte[] concat(final byte[] array1, final byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    /**
     * Concatenate the given {@code byte} arrays into one, with overlapping array elements included twice. Returns a new,
     * empty array if {@code arrays} was empty and returns the first array if {@code arrays} contains only a single array.
     * <p/>
     * The order of elements in the original arrays is preserved.
     *
     * @param arrays the arrays.
     * @return the new array.
     */
    public static byte[] concatAll(final byte[]... arrays) {
        if (arrays.length == 0) {
            return new byte[]{};
        }
        if (arrays.length == 1) {
            return arrays[0];
        }

        byte[] cur = concat(arrays[0], arrays[1]);
        for (int i = 2; i < arrays.length; i++) {
            cur = concat(cur, arrays[i]);
        }
        return cur;
    }

    /**
     * Split {@code source} into partitioned arrays using delimiter {@code c}.
     *
     * @param source the source array.
     * @param c      delimiter.
     * @return the partitioned arrays.
     */
    public static byte[][] split(final byte[] source, final int c) {
        if (CArrayUtils.isEmpty(source)) {
            return new byte[][]{};
        }

        List<byte[]> bytes = new ArrayList<>();
        int offset = 0;
        for (int i = 0; i <= source.length; i++) {

            if (i == source.length) {

                bytes.add(Arrays.copyOfRange(source, offset, i));
                break;
            }

            if (source[i] == c) {
                bytes.add(Arrays.copyOfRange(source, offset, i));
                offset = i + 1;
            }
        }
        return bytes.toArray(new byte[bytes.size()][]);
    }

    /**
     * Merge multiple {@code byte} arrays into one array
     *
     * @param firstArray       must not be {@literal null}
     * @param additionalArrays must not be {@literal null}
     * @return
     */
    public static byte[][] mergeArrays(byte[] firstArray, byte[]... additionalArrays) {
        Objects.requireNonNull(firstArray, "first array must not be null");
        Objects.requireNonNull(additionalArrays, "additional arrays must not be null");

        byte[][] result = new byte[additionalArrays.length + 1][];
        result[0] = firstArray;
        System.arraycopy(additionalArrays, 0, result, 1, additionalArrays.length);

        return result;
    }

    /**
     * Extract a byte array from {@link ByteBuffer} without consuming it.
     *
     * @param byteBuffer must not be {@literal null}.
     * @return
     * @since 2.0
     */
    public static byte[] getBytes(ByteBuffer byteBuffer) {
        Objects.requireNonNull(byteBuffer, "ByteBuffer must not be null!");

        final ByteBuffer duplicate = byteBuffer.duplicate();
        final byte[] bytes = new byte[duplicate.remaining()];
        duplicate.get(bytes);
        return bytes;
    }

    /**
     * Tests if the {@code haystack} starts with the given {@code prefix}.
     *
     * @param haystack the source to scan.
     * @param prefix   the prefix to find.
     * @return {@literal true} if {@code haystack} at position {@code offset} starts with {@code prefix}.
     * @see #startsWith(byte[], byte[], int)
     * @since 1.8.10
     */
    public static boolean startsWith(byte[] haystack, byte[] prefix) {
        return startsWith(haystack, prefix, 0);
    }

    /**
     * Tests if the {@code haystack} beginning at the specified {@code offset} starts with the given {@code prefix}.
     *
     * @param haystack the source to scan.
     * @param prefix   the prefix to find.
     * @param offset   the offset to start at.
     * @return {@literal true} if {@code haystack} at position {@code offset} starts with {@code prefix}.
     * @since 1.8.10
     */
    public static boolean startsWith(byte[] haystack, byte[] prefix, int offset) {
        int to = offset;
        int prefixOffset = 0;
        int prefixLength = prefix.length;

        if ((offset < 0) || (offset > haystack.length - prefixLength)) {
            return false;
        }

        while (--prefixLength >= 0) {
            if (haystack[to++] != prefix[prefixOffset++]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Searches the specified array of bytes for the specified value. Returns the index of the first matching value in the
     * {@code haystack}s natural order or {@code -1} of {@code needle} could not be found.
     *
     * @param haystack the source to scan.
     * @param needle   the value to scan for.
     * @return index of first appearance, or -1 if not found.
     * @since 1.8.10
     */
    public static int indexOf(byte[] haystack, byte needle) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Convert a {@link String} into a {@link ByteBuffer} using {@link java.nio.charset.StandardCharsets#UTF_8}.
     *
     * @param theString must not be {@literal null}.
     * @return
     * @since 2.1
     */
    public static ByteBuffer getByteBuffer(String theString) {
        return getByteBuffer(theString, StandardCharsets.UTF_8);
    }

    /**
     * Convert a {@link String} into a {@link ByteBuffer} using the given {@link Charset}.
     *
     * @param theString must not be {@literal null}.
     * @param charset   must not be {@literal null}.
     * @return
     * @since 2.1
     */
    public static ByteBuffer getByteBuffer(String theString, Charset charset) {
        Objects.requireNonNull(theString, "The String must not be null!");
        Objects.requireNonNull(charset, "The String must not be null!");

        return charset.encode(theString);
    }

    /**
     * Extract/Transfer bytes from the given {@link ByteBuffer} into an array by duplicating the buffer and fetching its
     * content.
     *
     * @param buffer must not be {@literal null}.
     * @return the extracted bytes.
     * @since 2.1
     */
    public static byte[] extractBytes(ByteBuffer buffer) {
        final ByteBuffer duplicate = buffer.duplicate();
        final byte[] bytes = new byte[duplicate.remaining()];
        duplicate.get(bytes);
        return bytes;
    }

    /**
     * Read an unsigned integer from the current position in the buffer, incrementing the position by 4 bytes
     *
     * @param buffer The buffer to read from
     * @return The integer read, as a long to avoid signedness
     */
    public static long readUnsignedInt(ByteBuffer buffer) {
        return buffer.getInt() & 0xffffffffL;
    }

    /**
     * Read an unsigned integer from the given position without modifying the buffers position
     *
     * @param buffer the buffer to read from
     * @param index  the index from which to read the integer
     * @return The integer read, as a long to avoid signedness
     */
    public static long readUnsignedInt(ByteBuffer buffer, int index) {
        return buffer.getInt(index) & 0xffffffffL;
    }

    /**
     * Read an unsigned integer stored in little-endian format from the {@link InputStream}.
     *
     * @param in The stream to read from
     * @return The integer read (MUST BE TREATED WITH SPECIAL CARE TO AVOID SIGNEDNESS)
     */
    public static int readUnsignedIntLE(InputStream in) throws IOException {
        return in.read()
            | (in.read() << 8)
            | (in.read() << 16)
            | (in.read() << 24);
    }

    /**
     * Read an unsigned integer stored in little-endian format from a byte array
     * at a given offset.
     *
     * @param buffer The byte array to read from
     * @param offset The position in buffer to read from
     * @return The integer read (MUST BE TREATED WITH SPECIAL CARE TO AVOID SIGNEDNESS)
     */
    public static int readUnsignedIntLE(byte[] buffer, int offset) {
        return (buffer[offset] << 0 & 0xff)
            | ((buffer[offset + 1] & 0xff) << 8)
            | ((buffer[offset + 2] & 0xff) << 16)
            | ((buffer[offset + 3] & 0xff) << 24);
    }

    /**
     * Write the given long value as a 4 byte unsigned integer. Overflow is ignored.
     *
     * @param buffer The buffer to write to
     * @param index  The position in the buffer at which to begin writing
     * @param value  The value to write
     */
    public static void writeUnsignedInt(ByteBuffer buffer, int index, long value) {
        buffer.putInt(index, (int) (value & 0xffffffffL));
    }

    /**
     * Write the given long value as a 4 byte unsigned integer. Overflow is ignored.
     *
     * @param buffer The buffer to write to
     * @param value  The value to write
     */
    public static void writeUnsignedInt(ByteBuffer buffer, long value) {
        buffer.putInt((int) (value & 0xffffffffL));
    }

    /**
     * Write an unsigned integer in little-endian format to the {@link OutputStream}.
     *
     * @param out   The stream to write to
     * @param value The value to write
     */
    public static void writeUnsignedIntLE(OutputStream out, int value) throws IOException {
        out.write(value);
        out.write(value >>> 8);
        out.write(value >>> 16);
        out.write(value >>> 24);
    }

    /**
     * Write an unsigned integer in little-endian format to a byte array
     * at a given offset.
     *
     * @param buffer The byte array to write to
     * @param offset The position in buffer to write to
     * @param value  The value to write
     */
    public static void writeUnsignedIntLE(byte[] buffer, int offset, int value) {
        buffer[offset] = (byte) value;
        buffer[offset + 1] = (byte) (value >>> 8);
        buffer[offset + 2] = (byte) (value >>> 16);
        buffer[offset + 3] = (byte) (value >>> 24);
    }

    /**
     * Read an integer stored in variable-length format using zig-zag decoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html"> Google Protocol Buffers</a>.
     *
     * @param buffer The buffer to read from
     * @return The integer read
     * @throws IllegalArgumentException if variable-length value does not terminate after 5 bytes have been read
     */
    public static int readVarint(ByteBuffer buffer) {
        int value = 0;
        int i = 0;
        int b;
        while (((b = buffer.get()) & 0x80) != 0) {
            value |= (b & 0x7f) << i;
            i += 7;
            if (i > 28)
                throw illegalVarintException(value);
        }
        value |= b << i;
        return (value >>> 1) ^ -(value & 1);
    }

    /**
     * Read an integer stored in variable-length format using zig-zag decoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html"> Google Protocol Buffers</a>.
     *
     * @param in The input to read from
     * @return The integer read
     * @throws IllegalArgumentException if variable-length value does not terminate after 5 bytes have been read
     * @throws IOException              if {@link DataInput} throws {@link IOException}
     */
    public static int readVarint(DataInput in) throws IOException {
        int value = 0;
        int i = 0;
        int b;
        while (((b = in.readByte()) & 0x80) != 0) {
            value |= (b & 0x7f) << i;
            i += 7;
            if (i > 28)
                throw illegalVarintException(value);
        }
        value |= b << i;
        return (value >>> 1) ^ -(value & 1);
    }

    /**
     * Read a long stored in variable-length format using zig-zag decoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html"> Google Protocol Buffers</a>.
     *
     * @param in The input to read from
     * @return The long value read
     * @throws IllegalArgumentException if variable-length value does not terminate after 10 bytes have been read
     * @throws IOException              if {@link DataInput} throws {@link IOException}
     */
    public static long readVarlong(DataInput in) throws IOException {
        long value = 0L;
        int i = 0;
        long b;
        while (((b = in.readByte()) & 0x80) != 0) {
            value |= (b & 0x7f) << i;
            i += 7;
            if (i > 63)
                throw illegalVarlongException(value);
        }
        value |= b << i;
        return (value >>> 1) ^ -(value & 1);
    }

    /**
     * Read a long stored in variable-length format using zig-zag decoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html"> Google Protocol Buffers</a>.
     *
     * @param buffer The buffer to read from
     * @return The long value read
     * @throws IllegalArgumentException if variable-length value does not terminate after 10 bytes have been read
     */
    public static long readVarlong(ByteBuffer buffer) {
        long value = 0L;
        int i = 0;
        long b;
        while (((b = buffer.get()) & 0x80) != 0) {
            value |= (b & 0x7f) << i;
            i += 7;
            if (i > 63)
                throw illegalVarlongException(value);
        }
        value |= b << i;
        return (value >>> 1) ^ -(value & 1);
    }

    /**
     * Write the given integer following the variable-length zig-zag encoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html"> Google Protocol Buffers</a>
     * into the output.
     *
     * @param value The value to write
     * @param out   The output to write to
     */
    public static void writeVarint(int value, DataOutput out) throws IOException {
        int v = (value << 1) ^ (value >> 31);
        while ((v & 0xffffff80) != 0L) {
            out.writeByte((v & 0x7f) | 0x80);
            v >>>= 7;
        }
        out.writeByte((byte) v);
    }

    /**
     * Write the given integer following the variable-length zig-zag encoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html"> Google Protocol Buffers</a>
     * into the buffer.
     *
     * @param value  The value to write
     * @param buffer The output to write to
     */
    public static void writeVarint(int value, ByteBuffer buffer) {
        int v = (value << 1) ^ (value >> 31);
        while ((v & 0xffffff80) != 0L) {
            byte b = (byte) ((v & 0x7f) | 0x80);
            buffer.put(b);
            v >>>= 7;
        }
        buffer.put((byte) v);
    }

    /**
     * Write the given integer following the variable-length zig-zag encoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html"> Google Protocol Buffers</a>
     * into the output.
     *
     * @param value The value to write
     * @param out   The output to write to
     */
    public static void writeVarlong(long value, DataOutput out) throws IOException {
        long v = (value << 1) ^ (value >> 63);
        while ((v & 0xffffffffffffff80L) != 0L) {
            out.writeByte(((int) v & 0x7f) | 0x80);
            v >>>= 7;
        }
        out.writeByte((byte) v);
    }

    /**
     * Write the given integer following the variable-length zig-zag encoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html"> Google Protocol Buffers</a>
     * into the buffer.
     *
     * @param value  The value to write
     * @param buffer The buffer to write to
     */
    public static void writeVarlong(long value, ByteBuffer buffer) {
        long v = (value << 1) ^ (value >> 63);
        while ((v & 0xffffffffffffff80L) != 0L) {
            byte b = (byte) ((v & 0x7f) | 0x80);
            buffer.put(b);
            v >>>= 7;
        }
        buffer.put((byte) v);
    }

    /**
     * Number of bytes needed to encode an integer in variable-length format.
     *
     * @param value The signed value
     */
    public static int sizeOfVarint(int value) {
        int v = (value << 1) ^ (value >> 31);
        int bytes = 1;
        while ((v & 0xffffff80) != 0L) {
            bytes += 1;
            v >>>= 7;
        }
        return bytes;
    }

    /**
     * Number of bytes needed to encode a long in variable-length format.
     *
     * @param value The signed value
     */
    public static int sizeOfVarlong(long value) {
        long v = (value << 1) ^ (value >> 63);
        int bytes = 1;
        while ((v & 0xffffffffffffff80L) != 0L) {
            bytes += 1;
            v >>>= 7;
        }
        return bytes;
    }

    private static IllegalArgumentException illegalVarintException(int value) {
        throw new IllegalArgumentException("Varint is too long, the most significant bit in the 5th byte is set, " +
            "converted value: " + Integer.toHexString(value));
    }

    private static IllegalArgumentException illegalVarlongException(long value) {
        throw new IllegalArgumentException("Varlong is too long, most significant bit in the 10th byte is set, " +
            "converted value: " + Long.toHexString(value));
    }
}
