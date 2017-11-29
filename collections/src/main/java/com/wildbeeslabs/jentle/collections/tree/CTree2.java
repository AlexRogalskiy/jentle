package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;

import java.util.Comparator;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom tree2 implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class CTree2<T> {

    protected T data;
    protected CTree2<T> left;
    protected CTree2<T> right;
    protected CTree2<T> parent;

    protected final Comparator<? super T> cmp;
    protected int size;

    public CTree2() {
        this(null);
    }

    public CTree2(final T data) {
        this(data, null, null, CSort.DEFAULT_SORT_COMPARATOR);
    }

    public CTree2(final T data, final CTree2<T> left, final CTree2<T> right, final Comparator<? super T> cmp) {
        this.data = data;
        this.right = right;
        this.left = left;
        this.parent = null;
        this.size = 1;
        this.cmp = cmp;
    }

    public void insertInOrder(final T item) {
        if (Objects.compare(item, this.data, this.cmp) <= 0) {
            if (Objects.isNull(this.left)) {
                this.setLeftChild(new CTree2<>(item));
            } else {
                this.left.insertInOrder(item);
            }
        } else {
            if (Objects.isNull(this.right)) {
                this.setRightChild(new CTree2<>(item));
            } else {
                this.right.insertInOrder(item);
            }
        }
        this.size++;
    }

    public int size() {
        return this.size;
    }

    public CTree2<T> find(final T item) {
        if (Objects.compare(item, data, this.cmp) == 0) {
            return this;
        } else if (Objects.compare(item, data, cmp) <= 0) {
            return Objects.nonNull(this.left) ? this.left.find(item) : null;
        } else if (Objects.compare(item, data, cmp) > 0) {
            return Objects.nonNull(this.right) ? this.right.find(item) : null;
        }
        return null;
    }

    public void setLeftChild(final CTree2<T> left) {
        this.left = left;
        if (Objects.nonNull(left)) {
            left.parent = this;
        }
    }

    public void setRightChild(final CTree2<T> right) {
        this.right = right;
        if (Objects.nonNull(right)) {
            right.parent = this;
        }
    }
}
