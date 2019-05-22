package com.wildbeeslabs.jentle.algorithms.decode;

/**
 * An immutable sequence of bytes.
 */
public interface ByteSequence {

    /**
     * An empty byte sequence.
     */
    ByteSequence EMPTY = new EmptyByteSequence();

    /**
     * Returns the length of this byte sequence.
     *
     * @return the number of <code>byte</code>s in this sequence.
     */
    int length();

    /**
     * Returns the <code>byte</code> value at the specified index.
     *
     * @param index the index of the <code>byte</code> value to be returned.
     * @return the corresponding <code>byte</code> value
     * @throws IndexOutOfBoundsException if <code>index < 0 || index >= length()</code>.
     */
    byte byteAt(int index);

    /**
     * Copies the contents of this byte sequence into a newly allocated byte
     * array and returns that array.
     *
     * @return a byte array holding a copy of this byte sequence.
     */
    byte[] toByteArray();

    public final class EmptyByteSequence implements ByteSequence {

        private static final byte[] EMPTY_BYTES = {};

        public int length() {
            return 0;
        }

        public byte byteAt(int index) {
            throw new IndexOutOfBoundsException();
        }

        public byte[] toByteArray() {
            return EMPTY_BYTES;
        }
    }
}
