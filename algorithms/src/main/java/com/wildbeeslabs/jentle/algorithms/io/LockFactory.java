package com.wildbeeslabs.jentle.algorithms.io;

/**
 * Interface to the lock factory. A lock factory produces locks on resources that are shared between threads.
 *
 * @author Allard Buijze
 * @since 0.3
 */
@FunctionalInterface
public interface LockFactory {

    /**
     * Obtain a lock for a resource identified by given {@code identifier}. Depending on the strategy, this
     * method may return immediately or block until a lock is held.
     *
     * @param identifier the identifier of the resource to obtain a lock for.
     * @return a handle to release the lock.
     */
    Lock obtainLock(final String identifier);
}
