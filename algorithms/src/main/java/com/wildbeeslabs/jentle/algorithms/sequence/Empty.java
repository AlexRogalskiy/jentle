//package com.wildbeeslabs.jentle.algorithms.sequence;
//
//import io.jenetics.util.ISeq;
//import io.jenetics.util.MSeq;
//
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Stream;
//
//import static java.util.Objects.requireNonNull;
//
///**
// * Contains static {@code Seq} definitions.
// *
// * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
// * @version 4.1
// * @since 3.4
// */
//public final class Empty {
//    private Empty() {
//    }
//
//    public static enum EmptyMSeq implements MSeq<Object> {
//
//        INSTANCE;
//
//        @Override
//        public void set(final int index, final Object value) {
//            throw new ArrayIndexOutOfBoundsException("MSeq is empty.");
//        }
//
//        @Override
//        public MSeq<Object> sort(
//            final int start,
//            final int end,
//            final Comparator<? super Object> comparator
//        ) {
//            Array.checkIndex(start, end, length());
//            return this;
//        }
//
//        @Override
//        public ListIterator<Object> listIterator() {
//            return asList().listIterator();
//        }
//
//        @Override
//        public MSeq<Object> subSeq(final int start, final int end) {
//            Array.checkIndex(start, end, length());
//            return this;
//        }
//
//        @Override
//        public MSeq<Object> subSeq(final int start) {
//            Array.checkIndex(start, 0, length());
//            return this;
//        }
//
//        @Override
//        public <B> MSeq<B> map(final Function<? super Object, ? extends B> mapper) {
//            requireNonNull(mapper);
//            return mseq();
//        }
//
//        @Override
//        public MSeq<Object> append(final Object... values) {
//            return MSeq.of(values);
//        }
//
//        @Override
//        public MSeq<Object> append(final Iterable<?> values) {
//            return MSeq.of(values);
//        }
//
//        @Override
//        public MSeq<Object> prepend(final Object... values) {
//            return MSeq.of(values);
//        }
//
//        @Override
//        public MSeq<Object> prepend(final Iterable<?> values) {
//            return MSeq.of(values);
//        }
//
//        @Override
//        public Stream<Object> stream() {
//            return Stream.empty();
//        }
//
//        @Override
//        public Stream<Object> parallelStream() {
//            return Stream.empty();
//        }
//
//        @Override
//        public Spliterator<Object> spliterator() {
//            return Spliterators.emptySpliterator();
//        }
//
//        @Override
//        public ISeq<Object> toISeq() {
//            return EmptyISeq.INSTANCE;
//        }
//
//        @Override
//        public MSeq<Object> copy() {
//            return this;
//        }
//
//        @Override
//        public Object get(final int index) {
//            throw new ArrayIndexOutOfBoundsException("MSeq is empty.");
//        }
//
//        @Override
//        public int length() {
//            return 0;
//        }
//
//        @Override
//        public List<Object> asList() {
//            return Collections.emptyList();
//        }
//
//        @Override
//        public Iterator<Object> iterator() {
//            return asList().iterator();
//        }
//
//    }
//
//
//    public static enum EmptyISeq implements ISeq<Object> {
//
//        INSTANCE;
//
//        @Override
//        public Iterator<Object> iterator() {
//            return asList().iterator();
//        }
//
//        @Override
//        public ISeq<Object> subSeq(final int start, final int end) {
//            Array.checkIndex(start, end, length());
//            return this;
//        }
//
//        @Override
//        public ISeq<Object> subSeq(final int start) {
//            Array.checkIndex(start, 0, length());
//            return this;
//        }
//
//        @Override
//        public Object get(final int index) {
//            throw new ArrayIndexOutOfBoundsException("ISeq is empty.");
//        }
//
//        @Override
//        public int length() {
//            return 0;
//        }
//
//        @Override
//        public List<Object> asList() {
//            return Collections.emptyList();
//        }
//
//        @Override
//        public <B> ISeq<B> map(final Function<? super Object, ? extends B> mapper) {
//            requireNonNull(mapper);
//            return iseq();
//        }
//
//        @Override
//        public ISeq<Object> append(final Object... values) {
//            return ISeq.of(values);
//        }
//
//        @Override
//        public ISeq<Object> append(final Iterable<?> values) {
//            return ISeq.of(values);
//        }
//
//        @Override
//        public ISeq<Object> prepend(final Object... values) {
//            return ISeq.of(values);
//        }
//
//        @Override
//        public ISeq<Object> prepend(final Iterable<?> values) {
//            return ISeq.of(values);
//        }
//
//        @Override
//        public Stream<Object> stream() {
//            return Stream.empty();
//        }
//
//        @Override
//        public Stream<Object> parallelStream() {
//            return Stream.empty();
//        }
//
//        @Override
//        public Spliterator<Object> spliterator() {
//            return Spliterators.emptySpliterator();
//        }
//
//        @Override
//        public MSeq<Object> copy() {
//            return EmptyMSeq.INSTANCE;
//        }
//
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> MSeq<T> mseq() {
//        return (MSeq<T>) EmptyMSeq.INSTANCE;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> ISeq<T> iseq() {
//        return (ISeq<T>) EmptyISeq.INSTANCE;
//    }
//
//}
