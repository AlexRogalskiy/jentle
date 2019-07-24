package com.wildbeeslabs.jentle.algorithms.toolset;

/**
 * The actual Redis bitfield type representation for signed and unsigned integers used with
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.1
 */
public class BitFieldType {

    /**
     * 8 bit signed Integer
     */
    public static final BitFieldType INT_8 = new BitFieldType(true, 8);

    /**
     * 16 bit signed Integer
     */
    public static final BitFieldType INT_16 = new BitFieldType(true, 16);

    /**
     * 32 bit signed Integer
     */
    public static final BitFieldType INT_32 = new BitFieldType(true, 32);

    /**
     * 64 bit signed Integer
     */
    public static final BitFieldType INT_64 = new BitFieldType(true, 64);

    /**
     * 8 bit unsigned Integer
     */
    public static final BitFieldType UINT_8 = new BitFieldType(false, 8);

    /**
     * 16 bit unsigned Integer
     */
    public static final BitFieldType UINT_16 = new BitFieldType(false, 16);

    /**
     * 32 bit unsigned Integer
     */
    public static final BitFieldType UINT_32 = new BitFieldType(false, 32);

    /**
     * 64 bit unsigned Integer
     */
    public static final BitFieldType UINT_64 = new BitFieldType(false, 64);

    private final boolean signed;
    private final int bits;

    private BitFieldType(boolean signed, Integer bits) {

        this.signed = signed;
        this.bits = bits;
    }

    /**
     * Create new signed {@link BitFieldType}.
     *
     * @param bits must not be {@literal null}.
     * @return
     */
    public static BitFieldType signed(int bits) {
        return new BitFieldType(true, bits);
    }

    /**
     * Create new unsigned {@link BitFieldType}.
     *
     * @param bits must not be {@literal null}.
     * @return
     */
    public static BitFieldType unsigned(int bits) {
        return new BitFieldType(false, bits);
    }

    /**
     * @return true if {@link BitFieldType} is signed.
     */
    public boolean isSigned() {
        return signed;
    }

    /**
     * Get the actual bits of the type.
     *
     * @return never {@literal null}.
     */
    public int getBits() {
        return bits;
    }

    /**
     * Get the Redis Command representation.
     *
     * @return
     */
    public String asString() {
        return (isSigned() ? "i" : "u") + getBits();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return asString();
    }
}
