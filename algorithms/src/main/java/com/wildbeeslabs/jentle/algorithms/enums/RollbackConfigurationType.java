package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Enum containing common rollback configurations for the Unit of Work.
 *
 * @author Rene de Waele
 * @since 3.0
 */
public enum RollbackConfigurationType {

    /**
     * Configuration that never performs a rollback of the unit of work.
     */
    NEVER {
        @Override
        public boolean rollBackOn(Throwable throwable) {
            return false;
        }
    },

    /**
     * Configuration that prescribes a rollback on any sort of exception or error.
     */
    ANY_THROWABLE {
        @Override
        public boolean rollBackOn(Throwable throwable) {
            return true;
        }
    },

    /**
     * Configuration that prescribes a rollback on any sort of unchecked exception, including errors.
     */
    UNCHECKED_EXCEPTIONS {
        @Override
        public boolean rollBackOn(Throwable throwable) {
            return !(throwable instanceof Exception) || throwable instanceof RuntimeException;
        }
    },

    /**
     * Configuration that prescribes a rollback on runtime exceptions only.
     */
    RUNTIME_EXCEPTIONS {
        @Override
        public boolean rollBackOn(Throwable throwable) {
            return throwable instanceof RuntimeException;
        }
    };

    public abstract boolean rollBackOn(Throwable throwable);
}
