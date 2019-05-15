package com.wildbeeslabs.jentle.collections.array.impl;

import com.wildbeeslabs.jentle.collections.exception.BoundaryViolationException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;

/**
 * Custom {@link ACArray} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CVector<T> extends ACArray<T> implements Cloneable {

    /**
     * Default indexed vector size
     */
    public static final int DEFAULT_VECTOR_SIZE = 10;

    protected int lastElement;

    public CVector() {
        this(DEFAULT_VECTOR_SIZE);
    }

    public CVector(final int var1) {
        this.lastElement = -1;
        this.array = (T[]) new Object[var1];
    }

    public synchronized void appendElement(final T var1) {
        this.ensureCapacity(this.lastElement + 2);
        this.array[++this.lastElement] = var1;
    }

    public int capacity() {
        return this.array.length;
    }

    public Object clone() {
        CVector<T> var1 = null;
        try {
            var1 = (CVector<T>) super.clone();
        } catch (CloneNotSupportedException var3) {
            System.err.println("cannot clone CVector.super");
            return null;
        }
        var1.array = (T[]) new Object[this.size()];
        System.arraycopy(this.array, 0, var1.array, 0, this.size());
        return var1;
    }

    public synchronized T get(int var1) {
        if (var1 >= this.array.length) {
            throw new ArrayIndexOutOfBoundsException(var1 + " >= " + this.array.length);
        } else if (var1 < 0) {
            throw new ArrayIndexOutOfBoundsException(var1 + " < 0 ");
        }
        return this.array[var1];
    }

    public synchronized Enumeration elements() {
        return new VectorEnumeration(this);
    }

    public synchronized void ensureCapacity(int var1) {
        if (var1 + 1 > this.array.length) {
            Object[] var2 = this.array;
            int var3 = this.array.length * 2;
            if (var1 + 1 > var3) {
                var3 = var1 + 1;
            }
            this.array = (T[]) new Object[var3];
            System.arraycopy(var2, 0, this.array, 0, var2.length);
        }
    }

    public synchronized boolean removeElement(final T var1) {
        int var2;
        for (var2 = 0; var2 <= this.lastElement && this.array[var2] != var1; ++var2) {
        }
        if (var2 <= this.lastElement) {
            this.array[var2] = null;
            int var3 = this.lastElement - var2;
            if (var3 > 0) {
                System.arraycopy(this.array, var2 + 1, this.array, var2, var3);
            }
            --this.lastElement;
            return true;
        }
        return false;
    }

    @Override
    public synchronized T set(final T var1, int var2) {
        if (var2 >= this.array.length) {
            throw new BoundaryViolationException(var2 + " >= " + this.array.length);
        }
        final T value = this.array[var2];
        this.array[var2] = var1;
        if (var2 > this.lastElement) {
            this.lastElement = var2;
        }
        return value;
    }

    public int size() {
        return (this.lastElement + 1);
    }

    public Enumeration<T> enumerator() {
        return new CVector.VectorEnumeration<>(this);
    }

    protected static class VectorEnumeration<T> implements Enumeration<T> {

        private final CVector<T> vector;
        private int i;

        public VectorEnumeration(final CVector<T> vector) {
            this.vector = vector;
            this.i = 0;
        }

        public boolean hasMoreElements() {
            synchronized (this.vector) {
                return this.i <= this.vector.size();
            }
        }

        public T nextElement() {
            synchronized (this.vector) {
                if (this.i <= this.vector.size()) {
                    return this.vector.array[this.i++];
                }
                throw new BoundaryViolationException(String.format("ERROR: vector index: {%s} is out of bounds", this.i));
            }
        }
    }
}
