package com.wildbeeslabs.jentle.collections.queue;

import com.wildbeeslabs.jentle.collections.exception.EmptyQueueException;

/**
 *
 * Custom Queue implementation
 * @author Alex
 * @param <T>
 */
public class CQueue<T> {
	private static class CQueueNode<T> {
		private final T data;
		private CQueueNode<T> next;

		public CQueueNode(T data) {
                    this.data = data;
		}

		@Override
		public String toString() {
                    return String.format("CQueueNode {data: %s, next: %s}", this.data, this.next);
                }

		@Override
                public boolean equals(Object obj) {
                    if(this == obj) {
                            return true;
                    }
                    if(null == obj || obj.getClass() != this.getClass()) {
                            return false;
                    }
                    CQueueNode<T> another = (CQueueNode<T>) obj;
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

	private CQueueNode<T> first;
	private CQueueNode<T> last;

	public void add(T item) {
		CQueueNode<T> temp = new CQueueNode<>(item);
		if(null != last) {
			last.next = temp;
		}
		last = temp;
		if(this.isEmpty()) {
			first = last;
		}
	}

	public T remove() throws EmptyQueueException {
		if(this.isEmpty()) {
			throw new EmptyQueueException();
		}
		T data = first.data;
		first = first.next;
		if(this.isEmpty()) {
			this.last = null;
		}
		return data;
	}

	public T peek() throws EmptyQueueException {
		if(this.isEmpty()) {
			throw new EmptyQueueException();
		}
		return first.data;
	}

	public boolean isEmpty() {
		return (null == first);
	}
}