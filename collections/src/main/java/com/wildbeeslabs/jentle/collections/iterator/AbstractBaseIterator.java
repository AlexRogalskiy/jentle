package com.wildbeeslabs.jentle.collections.iterator;

import java.util.Iterator;

/**
 * Iterator extension for Redis collection removal.
 *
 * @author Costin Leau
 */
public abstract class AbstractBaseIterator<E> implements Iterator<E> {

    private final Iterator<E> delegate;

    private E item;

    /**
     * Constructs a new <code>AbstractBaseIterator</code> instance.
     *
     * @param delegate - initial input {@link Iterator} instance
     */
    public AbstractBaseIterator(final Iterator<E> delegate) {
        this.delegate = delegate;
    }

    /**
     * @return
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    /**
     * @return
     * @see java.util.Iterator#next()
     */
    public E next() {
        this.item = delegate.next();
        return this.item;
    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        this.delegate.remove();
        this.removeFromStorage(this.item);
        this.item = null;
    }

    protected abstract void removeFromStorage(E item);
}

/*
	private class DefaultRedisSortedSetIterator extends RedisIterator<E> {

		public DefaultRedisSortedSetIterator(Iterator<E> delegate) {
			super(delegate);
		}

		@Override
		protected void removeFromRedisStorage(E item) {
			DefaultRedisZSet.this.remove(item);
		}
	}
 */
