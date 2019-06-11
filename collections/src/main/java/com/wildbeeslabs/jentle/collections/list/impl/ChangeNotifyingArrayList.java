package com.wildbeeslabs.jentle.collections.list.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of ArrayList that watches out for changes to the contents.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ChangeNotifyingArrayList<E> extends ArrayList<E> {

    public ChangeNotifyingArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public E set(int index, final E element) {
        this.onContentsChanged();
        return super.set(index, element);
    }

    @Override
    public boolean add(final E e) {
        this.onContentsChanged();
        return super.add(e);
    }

    @Override
    public void add(int index, final E element) {
        this.onContentsChanged();
        super.add(index, element);
    }

    @Override
    public E remove(int index) {
        this.onContentsChanged();
        return super.remove(index);
    }

    @Override
    public boolean remove(final Object o) {
        this.onContentsChanged();
        return super.remove(o);
    }

    @Override
    public void clear() {
        this.onContentsChanged();
        super.clear();
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        this.onContentsChanged();
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, final Collection<? extends E> c) {
        this.onContentsChanged();
        return super.addAll(index, c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        this.onContentsChanged();
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        this.onContentsChanged();
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        this.onContentsChanged();
        return super.retainAll(c);
    }

    public abstract void onContentsChanged();
}
