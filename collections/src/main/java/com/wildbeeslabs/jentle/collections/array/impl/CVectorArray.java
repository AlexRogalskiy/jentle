package com.wildbeeslabs.jentle.collections.array.impl;

import com.wildbeeslabs.jentle.collections.array.iface.IMutableVector;
import com.wildbeeslabs.jentle.collections.array.iface.IVector;
import com.wildbeeslabs.jentle.collections.utils.CUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom {@link IVector} implementation
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
        checkRange(rank);
        return this.array[rank];
    }

    @Override
    public T replaceAtRank(int rank, final T value) {
        checkRange(rank);
        final T temp = this.array[rank];
        this.array[rank] = value;
        return temp;
    }

    @Override
    public T removeAtRank(int rank) {
        checkRange(rank);
        final T temp = this.array[rank];
        for (int i = rank; i < this.size() - 1; i++) {
            this.array[i] = this.array[i + 1];
        }
        this.size--;
        return temp;
    }

    @Override
    public void insertAtRank(int rank, final T value) {
        checkRange(rank);
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
}
