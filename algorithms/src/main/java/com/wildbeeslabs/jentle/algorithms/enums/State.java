package com.wildbeeslabs.jentle.algorithms.enums;

public enum State {
    CLOSED {
        /**
         * {@inheritDoc}
         */
        @Override
        public State oppositeState() {
            return OPEN;
        }
    },

    OPEN {
        /**
         * {@inheritDoc}
         */
        @Override
        public State oppositeState() {
            return CLOSED;
        }
    };

    /**
     * Returns the opposite state to the represented state. This is useful
     * for flipping the current state.
     *
     * @return the opposite state
     */
    public abstract State oppositeState();
}
