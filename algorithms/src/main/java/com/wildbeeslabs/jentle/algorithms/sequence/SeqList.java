//package com.wildbeeslabs.jentle.algorithms.sequence;
//
//import java.io.Serializable;
//import java.util.AbstractList;
//import java.util.RandomAccess;
//
//import static java.util.Objects.requireNonNull;
//
///**
// * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
// * @version 3.4
// * @since 3.4
// */
//public class SeqList<T> extends AbstractList<T> implements RandomAccess, Serializable {
//    private static final long serialVersionUID = 1L;
//
//    public final Seq<T> seq;
//
//    SeqList(final Seq<T> seq) {
//        this.seq = requireNonNull(seq, "Seq must not be null.");
//    }
//
//    @Override
//    public T get(final int index) {
//        return seq.get(index);
//    }
//
//    @Override
//    public int size() {
//        return seq.length();
//    }
//
//    @Override
//    public int indexOf(final Object element) {
//        int index = -1;
//        if (element == null) {
//            for (int i = 0; i < seq.length() && index == -1; ++i) {
//                if (seq.get(i) == null) {
//                    index = i;
//                }
//            }
//        } else {
//            for (int i = 0; i < seq.length() && index == -1; ++i) {
//                if (element.equals(seq.get(i))) {
//                    index = i;
//                }
//            }
//        }
//
//        return index;
//    }
//
//    @Override
//    public int lastIndexOf(final Object element) {
//        int index = -1;
//        if (element == null) {
//            for (int i = seq.length(); --i >= 0 && index == -1; ) {
//                if (seq.get(i) == null) {
//                    index = i;
//                }
//            }
//        } else {
//            for (int i = seq.length(); --i >= 0 && index == -1; ) {
//                if (element.equals(seq.get(i))) {
//                    index = i;
//                }
//            }
//        }
//
//        return index;
//    }
//
//    @Override
//    public boolean contains(final Object element) {
//        return indexOf(element) != -1;
//    }
//
//    @Override
//    public Object[] toArray() {
//        final Object[] array = new Object[size()];
//        for (int i = size(); --i >= 0; ) {
//            array[i] = seq.get(i);
//        }
//        return array;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <E> E[] toArray(final E[] array) {
//        if (array.length < size()) {
//            final E[] copy = (E[]) java.lang.reflect.Array.newInstance(
//                array.getClass().getComponentType(), size()
//            );
//            for (int i = size(); --i >= 0; ) {
//                copy[i] = (E) seq.get(i);
//            }
//
//            return copy;
//        }
//
//        for (int i = size(); --i >= 0; ) {
//            array[i] = (E) seq.get(i);
//        }
//        return array;
//    }
//
//}
