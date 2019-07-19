//package com.wildbeeslabs.jentle.algorithms.sequence;
//
//import static java.util.Objects.requireNonNull;
//
//import java.util.List;
//import java.util.function.Function;
//
///**
// * ISeq view of an given list. The content is not copied on creation.
// *
// * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
// * @version 4.2
// * @since 4.2
// */
//final class SeqView<T> implements Seq<T> {
//
//	private final List<? extends T> _list;
//
//	SeqView(final List<? extends T> list) {
//		_list = requireNonNull(list);
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<T> asList() {
//		return (List<T>)_list;
//	}
//
//	@Override
//	public T get(final int index) {
//		return _list.get(index);
//	}
//
//	@Override
//	public int length() {
//		return _list.size();
//	}
//
//	@Override
//	public Seq<T> subSeq(final int start, final int end) {
//		return new SeqView<>(_list.subList(start, end));
//	}
//
//	@Override
//	public Seq<T> subSeq(final int start) {
//		return new SeqView<>(_list.subList(start, _list.size()));
//	}
//
//	@Override
//	public <B> Seq<B> map(final Function<? super T, ? extends B> mapper) {
//		requireNonNull(mapper);
//
//		final MSeq<B> result = MSeq.ofLength(length());
//		for (int i = 0; i < length(); ++i) {
//			result.set(i, mapper.apply(get(i)));
//		}
//
//		return result.toISeq();
//	}
//
//	@Override
//	public Seq<T> append(final Iterable<? extends T> values) {
//		requireNonNull(values);
//		return ISeq.<T>of(_list).append(values);
//	}
//
//	@Override
//	public Seq<T> prepend(final Iterable<? extends T> values) {
//		requireNonNull(values);
//		return ISeq.<T>of(_list).prepend(values);
//	}
//
//	@Override
//	public Object[] toArray() {
//		return _list.toArray();
//	}
//
//	@Override
//	public <B> B[] toArray(final B[] ArrayUtils) {
//		return _list.toArray(ArrayUtils);
//	}
//
//}
