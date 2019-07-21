package com.wildbeeslabs.jentle.algorithms.io;

/**
 * LockFactory implementation that does nothing. Can be useful in cases where a Locking Repository implementation  needs
 * to be configured to ignore locks, for example in scenario's where an underlying storage mechanism already performs
 * the necessary locking.
 *
 * @author Allard Buijze
 * @since 0.6
 */
public enum NullLockFactory implements LockFactory {

    /**
     * Singleton instance of a {@link NullLockFactory}.
     */
    INSTANCE;

    /**
     * {@inheritDoc}
     * <p/>
     * This implementation does nothing.
     */
    @Override
    public Lock obtainLock(String identifier) {
        return NO_LOCK;
    }

    private static final Lock NO_LOCK = new Lock() {
        @Override
        public void release() {
        }

        @Override
        public boolean isHeld() {
            return true;
        }
    };

}
