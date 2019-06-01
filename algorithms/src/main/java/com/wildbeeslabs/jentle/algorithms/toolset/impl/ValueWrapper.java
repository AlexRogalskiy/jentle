package com.wildbeeslabs.jentle.algorithms.toolset.impl;

import java.io.Serializable;

public final class ValueWrapper implements Serializable {

    private static final long serialVersionUID = 8409433513361859324L;

    private static final ValueWrapper nullValueWrapper = new ValueWrapper(null);

    /**
     * Factory for creating a new {@code ValueWrapper} for the supplied {@code value}.
     *
     * <p>If the supplied {@code value} is {@code null}, this method will return a
     * cached {@code ValueWrapper} suitable for all null values.
     *
     * @param value the value to wrap
     * @return a wrapper for the supplied value
     */
    public static ValueWrapper create(Object value) {
        return (value == null ? nullValueWrapper : new ValueWrapper(value));
    }

    private final Serializable value;
    private final Class<?> type;
    private final String stringRepresentation;
    private final int identityHashCode;

    /**
     * Reads and stores the supplied value's runtime type, string representation, and
     * identity hash code.
     */
    private ValueWrapper(Object value) {
        this.value = value instanceof Serializable ? (Serializable) value : null;
        this.type = value != null ? value.getClass() : null;
        String stringValue;
        try {
            stringValue = String.valueOf(value);
        } catch (Exception e) {
            stringValue = "<Exception in toString(): " + e + ">";
        }
        this.stringRepresentation = stringValue;
        this.identityHashCode = System.identityHashCode(value);
    }

    /**
     * Returns the value as passed to the constructor in case it implemented
     * {@link Serializable}; otherwise, {@code null}.
     */
    public Serializable getValue() {
        return this.value;
    }

    /**
     * Returns the value's runtime type in case it wasn't {@code null};
     * otherwise, {@code null}.
     */
    public Class<?> getType() {
        return this.type;
    }

    /**
     * Returns the value's string representation, i.e. the result of invoking
     * {@link Object#toString} at the time this object's constructor was
     * called. Returns {@code "null"} if the value was {@code null}.
     */
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }

    /**
     * Returns the value's identity hash code, i.e. the result of invoking
     * {@link System#identityHashCode} at the time this object's constructor
     * was called. Returns {@code 0} if the value was {@code null}.
     */
    public int getIdentityHashCode() {
        return this.identityHashCode;
    }

    /**
     * Returns the value's string representation along with its type and
     * identity hash code.
     */
    @Override
    public String toString() {
        if (this.type == null) {
            return "null";
        }
        return this.stringRepresentation + //
            " (" + this.type.getName() + "@" + Integer.toHexString(this.identityHashCode) + ")";
    }

}
