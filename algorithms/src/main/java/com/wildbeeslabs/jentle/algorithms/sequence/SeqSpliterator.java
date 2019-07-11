//package com.wildbeeslabs.jentle.algorithms.sequence;
//
//import java.util.Spliterator;
//import java.util.function.Consumer;
//
//import static java.util.Objects.requireNonNull;
//
///**
// * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
// * @version 3.4
// * @since 3.0
// */
//final class SeqSpliterator<T> implements Spliterator<T> {
//
//    private final Seq<T> _seq;
//    private final int _fence;
//    private int _index;
//
//    SeqSpliterator(
//        final Seq<T> seq,
//        final int origin,
//        final int fence
//    ) {
//        _seq = requireNonNull(seq);
//        _index = origin;
//        _fence = fence;
//    }
//
//    SeqSpliterator(final Seq<T> seq) {
//        this(seq, 0, seq.length());
//    }
//
//    @Override
//    public void forEachRemaining(final Consumer<? super T> action) {
//        requireNonNull(action);
//
//        Seq<T> seq;
//        int i;
//        int hi;
//
//        if ((seq = _seq).length() >= (hi = _fence) &&
//            (i = _index) >= 0 && i < (_index = hi)) {
//            do {
//                action.accept(seq.get(i));
//            } while (++i < hi);
//        }
//    }
//
//    @Override
//    public boolean tryAdvance(final Consumer<? super T> action) {
//        if (_index >= 0 && _index < _fence) {
//            action.accept(_seq.get(_index++));
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public Spliterator<T> trySplit() {
//        final int lo = _index;
//        final int mid = (lo + _fence) >>> 1;
//
//        return (lo >= mid)
//            ? null
//            : new SeqSpliterator<>(_seq, lo, _index = mid);
//    }
//
//    @Override
//    public long estimateSize() {
//        return _fence - _index;
//    }
//
//    @Override
//    public int characteristics() {
//        return Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED;
//    }
//}
