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
package com.wildbeeslabs.jentle.collections.set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom bit-set field implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
@ToString
public class CBitSetField {

    private final int mask;
    private final int shiftCount;

    /**
     * Creates a BitField instance.
     *
     * @param mask the mask specifying which bits apply to this BitField. Bits
     * that are set in this mask are the bits that this BitField operates on
     */
    public CBitSetField(int mask) {
        this.mask = mask;
        int count = 0;
        int bit_pattern = mask;
        if (bit_pattern != 0) {
            while ((bit_pattern & 1) == 0) {
                count++;
                bit_pattern >>= 1;
            }
        }
        this.shiftCount = count;
    }

    /**
     * Obtains the value for the specified BitField, appropriately shifted
     * right.
     *
     * Many users of a BitField will want to treat the specified bits as an int
     * value, and will not want to be aware that the value is stored as a
     * BitField (and so shifted left so many bits).
     *
     * @see #setValue(int,int)
     * @param holder the int data containing the bits we're interested in
     * @return the selected bits, shifted right appropriately
     */
    public int getValue(int holder) {
        return this.getRawValue(holder) >> this.shiftCount;
    }

    /**
     * Obtains the value for the specified BitField, appropriately shifted
     * right, as a short.
     *
     * Many users of a BitField will want to treat the specified bits as an int
     * value, and will not want to be aware that the value is stored as a
     * BitField (and so shifted left so many bits).
     *
     * @see #setShortValue(short,short)
     * @param holder the short data containing the bits we're interested in
     * @return the selected bits, shifted right appropriately
     */
    public short getShortValue(short holder) {
        return (short) this.getValue(holder);
    }

    /**
     * Obtains the value for the specified BitField, unshifted.
     *
     * @param holder the int data containing the bits we're interested in
     * @return the selected bits
     */
    public int getRawValue(int holder) {
        return holder & this.mask;
    }

    /**
     * Obtains the value for the specified BitField, unshifted.
     *
     * @param holder the short data containing the bits we're interested in
     * @return the selected bits
     */
    public short getShortRawValue(short holder) {
        return (short) this.getRawValue(holder);
    }

    /**
     * Returns whether the field is set or not.
     *
     * This is most commonly used for a single-bit field, which is often used to
     * represent a boolean value; the results of using it for a multi-bit field
     * is to determine whether *any* of its bits are set.
     *
     * @param holder the int data containing the bits we're interested in
     * @return <code>true</code> if any of the bits are set, else
     * <code>false</code>
     */
    public boolean isSet(int holder) {
        return (holder & this.mask) != 0;
    }

    /**
     * Returns whether all of the bits are set or not.
     *
     * This is a stricter test than {@link #isSet(int)}, in that all of the bits
     * in a multi-bit set must be set for this method to return
     * <code>true</code>.
     *
     * @param holder the int data containing the bits we're interested in
     * @return <code>true</code> if all of the bits are set, else
     * <code>false</code>
     */
    public boolean isAllSet(int holder) {
        return (holder & this.mask) == this.mask;
    }

    /**
     * Replaces the bits with new values.
     *
     * @see #getValue(int)
     * @param holder the int data containing the bits we're interested in
     * @param value the new value for the specified bits
     * @return the value of holder with the bits from the value parameter
     * replacing the old bits
     */
    public int setValue(int holder, int value) {
        return (holder & ~this.mask) | ((value << this.shiftCount) & this.mask);
    }

    /**
     * Replaces the bits with new values.
     *
     * @see #getShortValue(short)
     * @param holder the short data containing the bits we're interested in
     * @param value the new value for the specified bits
     * @return the value of holder with the bits from the value parameter
     * replacing the old bits
     */
    public short setShortValue(short holder, short value) {
        return (short) this.setValue(holder, value);
    }

    /**
     * Clears the bits.
     *
     * @param holder the int data containing the bits we're interested in
     * @return the value of holder with the specified bits cleared (set to
     * <code>0</code>)
     */
    public int clear(int holder) {
        return holder & ~this.mask;
    }

    /**
     * Clears the bits.
     *
     * @param holder the short data containing the bits we're interested in
     * @return the value of holder with the specified bits cleared (set to
     * <code>0</code>)
     */
    public short clearShort(short holder) {
        return (short) this.clear(holder);
    }

    /**
     * Clears the bits.
     *
     * @param holder the byte data containing the bits we're interested in
     *
     * @return the value of holder with the specified bits cleared (set to
     * <code>0</code>)
     */
    public byte clearByte(byte holder) {
        return (byte) this.clear(holder);
    }

    /**
     * Sets the bits.
     *
     * @param holder the int data containing the bits we're interested in
     * @return the value of holder with the specified bits set to <code>1</code>
     */
    public int set(int holder) {
        return holder | this.mask;
    }

    /**
     * Sets the bits.
     *
     * @param holder the short data containing the bits we're interested in
     * @return the value of holder with the specified bits set to <code>1</code>
     */
    public short setShort(short holder) {
        return (short) this.set(holder);
    }

    /**
     * Sets the bits.
     *
     * @param holder the byte data containing the bits we're interested in
     *
     * @return the value of holder with the specified bits set to <code>1</code>
     */
    public byte setByte(byte holder) {
        return (byte) this.set(holder);
    }

    /**
     * Sets a boolean BitField.
     *
     * @param holder the int data containing the bits we're interested in
     * @param flag indicating whether to set or clear the bits
     * @return the value of holder with the specified bits set or cleared
     */
    public int setBoolean(int holder, boolean flag) {
        return flag ? this.set(holder) : this.clear(holder);
    }

    /**
     * Sets a boolean BitField.
     *
     * @param holder the short data containing the bits we're interested in
     * @param flag indicating whether to set or clear the bits
     * @return the value of holder with the specified bits set or cleared
     */
    public short setShortBoolean(short holder, boolean flag) {
        return flag ? this.setShort(holder) : this.clearShort(holder);
    }

    /**
     * Sets a boolean BitField.
     *
     * @param holder the byte data containing the bits we're interested in
     * @param flag indicating whether to set or clear the bits
     * @return the value of holder with the specified bits set or cleared
     */
    public byte setByteBoolean(byte holder, boolean flag) {
        return flag ? this.setByte(holder) : this.clearByte(holder);
    }
}
