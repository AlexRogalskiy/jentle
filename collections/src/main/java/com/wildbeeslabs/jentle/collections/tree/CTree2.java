package com.wildbeeslabs.jentle.collections.tree;

import com.wildbeeslabs.jentle.algorithms.sort.CSort;

import java.util.Comparator;
import java.util.Objects;

/**
 *
 * Custom tree2 implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
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
            if (null == this.left) {
                this.setLeftChild(new CTree2<>(item));
            } else {
                this.left.insertInOrder(item);
            }
        } else {
            if (null == this.right) {
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
            return null != this.left ? this.left.find(item) : null;
        } else if (Objects.compare(item, data, cmp) > 0) {
            return null != this.right ? this.right.find(item) : null;
        }
        return null;
    }

    public void setLeftChild(final CTree2<T> left) {
        this.left = left;
        if (null != left) {
            left.parent = this;
        }
    }

    public void setRightChild(final CTree2<T> right) {
        this.right = right;
        if (null != right) {
            right.parent = this;
        }
    }

    @Override
    public String toString() {
        return String.format("CTree2 {data: %s, left: %s, right: %s, parent: %s}", this.data, this.left, this.right, this.parent);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final CTree2<T> other = (CTree2<T>) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        if (!Objects.equals(this.right, other.right)) {
            return false;
        }
        if (!Objects.equals(this.parent, other.parent)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.data);
        hash = 17 * hash + Objects.hashCode(this.left);
        hash = 17 * hash + Objects.hashCode(this.right);
        hash = 17 * hash + Objects.hashCode(this.parent);
        return hash;
    }
}
