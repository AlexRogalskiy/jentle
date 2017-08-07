package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.collections.exception.EmptyListException;
import java.util.Collection;
import java.util.Queue;

/**
 *
 * Custom List implementation
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CList<T> implements IList<T> {
	private static class CListNode<T> {
		private final T data;
		private CListNode<T> next;

		public CListNode(T data) {
                    this.data = data;
		}

		@Override
		public String toString() {
                    return String.format("CListNode {data: %s, next: %s}", this.data, this.next);
                }

		@Override
                public boolean equals(Object obj) {
                    if(this == obj) {
                            return true;
                    }
                    if(null == obj || obj.getClass() != this.getClass()) {
                            return false;
                    }
                    CListNode<T> another = (CListNode<T>) obj;
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

	private CListNode<T> first = null;
	private CListNode<T> last = null;
        private int size;
        
        public CList() {
            this.size = 0;
        }

	public void add(T item) {
		CListNode<T> temp = new CListNode<>(item);
		if(null != last) {
			last.next = temp;
		}
		last = temp;
		if(null == first) {
			first = last;
		}
                this.size++;
	}

	public T remove() throws EmptyListException {
		if(this.isEmpty()) {
			throw new EmptyListException(String.format("ERROR: CList (empty size=%i)", this.size));
		}
		T data = first.data;
		first = first.next;
		if(this.isEmpty()) {
			this.last = null;
		}
                this.size--;
		return data;
	}

        @Override
	public T peek() throws EmptyListException {
		if(this.isEmpty()) {
			throw new EmptyListException(String.format("ERROR: CList (empty size=%i)", this.size));
		}
		return first.data;
	}

	public boolean isEmpty() {
		return (null == first);
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
        public int size() {
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