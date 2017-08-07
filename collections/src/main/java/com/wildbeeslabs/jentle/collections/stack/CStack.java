package com.wildbeeslabs.jentle.collections.stack;

import com.wildbeeslabs.jentle.collections.exception.EmptyStackException;
import java.util.Collection;
import java.util.Queue;

/**
 *
 * Custom stack implementation
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
public class CStack<T> implements IStack<T> {
	private static class CStackNode<T> {
		private final T data;
		private CStackNode<T> next;

		public CStackNode(final T data) {
			this.data = data;
		}
                
                public CStackNode(final T data, final CStackNode<T> next) {
			this.data = data;
                        this.next = next;
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

	private CStackNode<T> top = null;
        private int size;
        
        public CStack() {
            this.size = 0;
        }

        @Override
	public T pop() throws EmptyStackException {
		if(this.isEmpty()) {
			throw new EmptyStackException(String.format("ERROR: CStack (empty size=%i)", this.size));
		}
		T item = top.data;
		top = top.next;
                this.size--;
		return item;
	}

        @Override
	public void push(T item) {
		CStackNode<T> temp = new CStackNode<T>(item, top);
                this.size++;
		top = temp;
	}

        @Override
	public T peek() throws EmptyStackException {
		if(this.isEmpty()) {
			throw new EmptyStackException(String.format("ERROR: CStack (empty size=%i)", this.size));
		}
		return top.data;
	}
        
        @Override
        public int size() {
		return this.size;
        }

	public boolean isEmpty() {
		return (0 == this.size());
	}

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public boolean remove(T value) {
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
        public Queue<T> toLifoQueue() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Collection<T> toCollection() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
}
