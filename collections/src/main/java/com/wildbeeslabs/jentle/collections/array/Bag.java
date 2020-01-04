package com.wildbeeslabs.jentle.collections.array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Bag<E> implements Iterable<E> {

	private final Object[] array;
	private final int size;
	private int cursor = 0;

	public Bag(int size) {
		this.size = size;
		array = new Object[size];
	}

	public int getSize() {
		return size;
	}

	public boolean add(E e) {
		if (cursor < size) {
			array[cursor] = e;
			cursor++;
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public E get(int i) {
		if (i >= size) {
			throw new IndexOutOfBoundsException("Size : " + size);
		} else {
			return (E) array[i];
		}
	}

	public void forEach(Consumer<? super E> action) {
		for (Object object : array) {
			@SuppressWarnings("unchecked")
			E e = (E) object;
			action.accept(e);
		}
	}

	@SuppressWarnings("unchecked")
	public E[] filter(Predicate<? super E> action) {
		ArrayList<? super E> a = new ArrayList<>();
		for (Object object : array) {
			E e = (E) object;
			if (action.test(e)) {
				a.add(e);
			}
		}
		return (E[]) a.toArray();
	}

	@SuppressWarnings("unchecked")
	public void replaceAll(UnaryOperator<E> action) {
		for (int i = 0; i < array.length; i++) {
			Object object = array[i];
			E e = (E) object;
			array[i] = action.apply(e);
		}
	}

	public static void main(String[] args) {
//		Bag<Integer> bag = new Bag<>(5);
//		bag.add(1);
//		bag.add(2);
//		bag.add(3);
//		bag.add(4);
//		bag.add(5);
//		bag.forEach(System.out::println);
//		System.out.println("Printing Square");
//		bag.replaceAll(Bag::squareIt);
//		for (Integer integer : bag) {
//			System.out.println(integer);
//		}
		
		Bag<String> anotherBag = new Bag<>(5);
		anotherBag.add("A");
		anotherBag.add("Rajan");
		anotherBag.add("Rajan Punchouty");
		anotherBag.add("Tamanna");
		anotherBag.add("Khwaish");
		anotherBag.replaceAll( string -> {
			if(string.length() <= 5)
				return string;
			else return string.substring(0,5);
		});
		for(String str : anotherBag) {
			System.out.println(str);
		}
	}

	public static int squareIt(int i) {
		return i * i;
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	private class Itr implements Iterator<E> {

		private int cursorInternal = 0;

		@Override
		public boolean hasNext() {
			return cursorInternal < size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			if (cursorInternal < size) {
				E e = (E) array[cursorInternal];
				cursorInternal++;
				return e;
			} else {
				throw new IllegalStateException();
			}
		}

	}

}
