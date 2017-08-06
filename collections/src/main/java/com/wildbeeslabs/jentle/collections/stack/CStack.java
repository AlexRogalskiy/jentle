package com.wildbeeslabs.jentle.collections.stack;

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;

/**
 *
 * Custom Stack implementation
 * @author Alex
 * @param <T>
 */
public class CStack<T> {
	private static class CStackNode<T> {
		private final T data;
		private CStackNode<T> next;

		public CStackNode(T data) {
			this.data = data;
		}

		@Override
		public String toString() {
                    return String.format("CStackNode {data: %s, next: %s}", this.data, this.next);
                }

		@Override
                public boolean equals(Object obj) {
                    if(this == obj) {
                            return true;
                    }
                    if(null == obj || obj.getClass() != this.getClass()) {
                            return false;
                    }
		    CStackNode<T> another = (CStackNode<T>) obj;
		    return (null != this.data && this.data.equals(another.data)) &&
                            (null != this.next && this.next.equals(another.next));
		}

		@Override 
                public int hashCode() {
                    final int prime = 31;
                    int result = 1;
                    result = prime * result + ((null == this.data) ? 0 : this.data.hashCode());
                    result = prime * result + ((null == this.next) ? 0 : this.next.hashCode());
                    return result;
                }
	}

	private CStackNode<T> top;

	public T pop() throws EmptyStackException {
		if(this.isEmpty()) {
			throw new EmptyStackException();
		}
		T item = top.data;
		top = top.next;
		return item;
	}

	public void push(T item) {
		CStackNode<T> temp = new CStackNode<>(item);
		temp.next = top;
		top = temp;
	}

	public T peek() throws EmptyStackException {
		if(this.isEmpty()) {
			throw new EmptyStackException();
		}
		return top.data;
	}

	public boolean isEmpty() {
		return (null == top);
	}
}
