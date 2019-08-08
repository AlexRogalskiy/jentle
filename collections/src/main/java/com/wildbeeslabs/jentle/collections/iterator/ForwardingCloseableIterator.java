package com.wildbeeslabs.jentle.collections.iterator;

import com.wildbeeslabs.jentle.collections.utils.CValidationUtils;

import java.util.Iterator;

/**
 * Forwards {@link CloseableIterator} invocations to the configured {@link Iterator} delegate.
 *
 * @param <T>
 */
public class ForwardingCloseableIterator<T> implements CloseableIterator<T> {
    private final Iterator<? extends T> delegate;
    private final Runnable closeHandler;

    /**
     * Creates a new {@link ForwardingCloseableIterator}.
     *
     * @param delegate must not be {@literal null}.
     */
    public ForwardingCloseableIterator(final Iterator<? extends T> delegate) {
        this(delegate, null);
    }

    /**
     * Creates a new {@link ForwardingCloseableIterator} that invokes the configured {@code closeHandler} on
     * {@link #close()}.
     *
     * @param delegate     must not be {@literal null}.
     * @param closeHandler may be {@literal null}.
     */
    public ForwardingCloseableIterator(final Iterator<? extends T> delegate, final Runnable closeHandler) {
        CValidationUtils.notNull(delegate, "Delegate iterator must not be null!");
        this.delegate = delegate;
        this.closeHandler = closeHandler;
    }

    /*
     * (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public T next() {
        return this.delegate.next();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.util.CloseableIterator#close()
     */
    @Override
    public void close() {
        if (this.closeHandler != null) {
            this.closeHandler.run();
        }
    }
}
