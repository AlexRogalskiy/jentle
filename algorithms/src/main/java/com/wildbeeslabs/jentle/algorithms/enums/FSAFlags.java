package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * FSA automaton flags. Where applicable, flags follow Daciuk's <code>fsa</code>
 * package.
 */
@Getter
@RequiredArgsConstructor
public enum FSAFlags {
    /**
     * Daciuk: flexible FSA encoding.
     */
    FLEXIBLE(1 << 0),

    /**
     * Daciuk: stop bit in use.
     */
    STOPBIT(1 << 1),

    /**
     * Daciuk: next bit in use.
     */
    NEXTBIT(1 << 2),

    /**
     * Daciuk: tails compression.
     */
    TAILS(1 << 3),

    /*
     * These flags are outside of byte range (never occur in Daciuk's FSA).
     */

    /**
     * The FSA contains right-language count numbers on states.
     */
    NUMBERS(1 << 8),

    /**
     * The FSA supports legacy built-in separator and filler characters (Daciuk's
     * FSA package compatibility).
     */
    SEPARATORS(1 << 9);

    /**
     * Bit mask for the corresponding flag.
     */
    public final int bits;

    /**
     * @param flags The bitset with flags.
     * @return Returns <code>true</code> iff this flag is set in <code>flags</code>.
     */
    public boolean isSet(int flags) {
        return (flags & this.bits) != 0;
    }

    /**
     * @param flags A set of flags to encode.
     * @return Returns the set of flags encoded as packed <code>short</code>.
     */
    public static short asShort(final Set<FSAFlags> flags) {
        short value = 0;
        for (final FSAFlags f : flags) {
            value |= f.bits;
        }
        return value;
    }
}
