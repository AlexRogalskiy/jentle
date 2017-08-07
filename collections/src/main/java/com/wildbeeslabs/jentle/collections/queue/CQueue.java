package com.wildbeeslabs.jentle.collections.queue;

import com.wildbeeslabs.jentle.collections.exception.EmptyQueueException;
import java.util.Collection;
import java.util.Queue;

/**
 *
 * Custom Queue implementation
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CQueue<T> implements IQueue<T> {
	private static class CQueueNode<T> {
		private final T data;
		private CQueueNode<T> next;

		public CQueueNode(T data) {
                    this.data = data;
		}
                
                public CQueueNode(final T data, final CQueueNode<T> next) {
                    this.data = data;
                    this.next = next;
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

	private CQueueNode<T> first = null;
	private CQueueNode<T> last = null;
        private int size;
        
        public CQueue() {
            this.size = 0;
        }

	public void enqueue(T item) {
		CQueueNode<T> temp = new CQueueNode<T>(item);
		if(null != last) {
			last.next = temp;
		}
		last = temp;
		if(null == first) {
			first = last;
		}
                this.size++;
	}

	public T dequeue() throws EmptyQueueException {
		if(this.isEmpty()) {
			throw new EmptyQueueException(String.format("ERROR: CQueue (empty size=%i)", this.size));
		}
		T data = first.data;
		first = first.next;
		if(null == first) {
			this.last = null;
		}
                this.size--;
		return data;
	}

        @Override
	public T peek() throws EmptyQueueException {
		if(this.isEmpty()) {
			throw new EmptyQueueException(String.format("ERROR: CQueue (empty size=%i)", this.size));
		}
		return first.data;
	}
        
        @Override
        public int size() {
		return this.size;
        }

	public boolean isEmpty() {
		return (0 == this.size());
	}
        
        @Override
        public boolean offer(T value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public T poll() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean remove(T value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean contains(T value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean validate() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Queue<T> toQueue() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Collection<T> toCollection() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
}