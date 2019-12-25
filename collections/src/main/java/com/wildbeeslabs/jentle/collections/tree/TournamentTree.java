package com.wildbeeslabs.jentle.collections.tree;

import com.max256.abhot.core.datastore.Order;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class TournamentTree<T> {
    private static class TreeValue<T> {
        private int m_iteratorNum;
        private T m_value;
        private Iterator<T> m_iterator;

        public TreeValue(Iterator<T> iterator, T value, int iteratorNum) {
            m_iterator = iterator;
            m_value = value;
            m_iteratorNum = iteratorNum;
        }

        public int getIteratorNum() {
            return (m_iteratorNum);
        }

        public void setValue(T value) {
            m_value = value;
        }

        public T getValue() {
            return (m_value);
        }

        public Iterator<T> getIterator() {
            return (m_iterator);
        }
    }

    private class TreeComparator implements Comparator<TreeValue<T>> {
        public int compare(TreeValue<T> tv1, TreeValue<T> tv2) {
            int resp = m_comparator.compare(tv1.getValue(), tv2.getValue());

            if (resp == 0)
                return (tv1.getIteratorNum() - tv2.getIteratorNum());
            else
                return (resp);
        }
    }

    //===========================================================================
    private TreeSet<TreeValue<T>> m_treeSet;
    private Comparator<T> m_comparator;
    private int m_iteratorIndex = 0;
    private Order m_order;

    public TournamentTree(Comparator<T> comparator, Order order) {
        m_comparator = comparator;
        m_treeSet = new TreeSet<TreeValue<T>>(new TreeComparator());
        m_order = order;
    }

    //---------------------------------------------------------------------------
    public void addIterator(Iterator<T> iterator) {
        if (iterator.hasNext())
            m_treeSet.add(new TreeValue<T>(iterator, iterator.next(), m_iteratorIndex++));
    }

    //---------------------------------------------------------------------------
    public boolean hasNext() {
        return !m_treeSet.isEmpty();
    }

    //---------------------------------------------------------------------------
    public T nextElement() {
        TreeValue<T> value;
        if (m_order == Order.ASC)
            value = m_treeSet.pollFirst();
        else
            value = m_treeSet.pollLast();

        if (value == null)
            return (null);

        T ret = value.getValue();

        if (value.getIterator().hasNext()) {
            value.setValue(value.getIterator().next());
            m_treeSet.add(value);
        }

        return (ret);
    }
}
