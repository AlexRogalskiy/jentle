package com.wildbeeslabs.jentle.collections.array.impl;

import com.wildbeeslabs.jentle.collections.array.iface.IMutableVector;
import com.wildbeeslabs.jentle.collections.exception.BoundaryViolationException;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
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
public abstract class CVectorArray<T> extends ACArray<T> implements IMutableVector<T> {

    /**
     * Default vector capacity
     */
    private int capacity = 1 << 4;
    /**
     * Default vector {@link Class} element type
     */
    private Class<? extends T> clazz;

    /**
     * Default vector constructor by initial element {@link Class} instance
     *
     * @param clazz - initial input element {@link Class} instance
     */
    public CVectorArray(final Class<? extends T> clazz) {
        this.clazz = clazz;
        this.array = CUtils.newArray2(clazz, this.capacity);
    }

    @Override
    public T elemAtRank(int rank) {
        this.checkRange(rank);
        return this.array[rank];
    }

    @Override
    public T replaceAtRank(int rank, final T value) {
        this.checkRange(rank);
        final T temp = this.array[rank];
        this.array[rank] = value;
        return temp;
    }

    @Override
    public T removeAtRank(int rank) {
        this.checkRange(rank);
        final T temp = this.array[rank];
        for (int i = rank; i < this.size() - 1; i++) {
            this.array[i] = this.array[i + 1];
        }
        this.size--;
        return temp;
    }

    @Override
    public void insertAtRank(int rank, final T value) {
        this.checkRange(rank);
        if (this.size() == this.capacity) {
            this.capacity *= 2;
            final T[] temp = CUtils.newArray2(this.clazz, this.capacity);
            for (int i = 0; i < this.size(); i++) {
                temp[i] = this.array[i];
            }
            this.array = temp;
        }
        for (int i = this.size() - 1; i >= rank; i--) {
            this.array[i + 1] = this.array[i];
        }
        this.array[rank] = value;
        this.size++;
    }

    public Enumeration<T> enumerator() {
        return new CVectorArray.VectorEnumeration<>(this);
    }

    protected static class VectorEnumeration<T> implements Enumeration<T> {

        private final CVectorArray<T> vector;
        private int i;

        public VectorEnumeration(final CVectorArray<T> vector) {
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
                    return this.vector.elemAtRank(this.i++);
                }
                throw new BoundaryViolationException(String.format("ERROR: vector index: {%s} is out of bounds", this.i));
            }
        }
    }
}
