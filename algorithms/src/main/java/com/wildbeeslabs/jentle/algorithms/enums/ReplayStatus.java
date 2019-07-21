package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Type that can be used as parameter of Event Handler methods that indicates whether a message is delivered as part of
 * a replay, or in regular operations. Messages delivered as part of a replay may have been handled by this handler
 * before.
 * <p>
 * Note that this is only sensible for event handlers that are assigned to a Tracking Processor. Event Handlers assigned
 * to another type of processor will only receive events in "Regular" operation.
 *
 * @author Allard Buijze
 * @see AllowReplay @AllowReplay
 * @since 3.2
 */
public enum ReplayStatus {

    /**
     * Indicats the message is delivered as part of a replay (and may have been delivered before)
     */
    REPLAY(true),
    /**
     * Indicates the message is not delivered as part of a replay (and has not been delivered before).
     */
    REGULAR(false);

    private final boolean isReplay;

    ReplayStatus(boolean isReplay) {
        this.isReplay = isReplay;
    }

    /**
     * Indicates whether this status represents a replay.
     *
     * @return {@code true} if this status indicates a replay, otherwise {@code false}
     */
    public boolean isReplay() {
        return isReplay;
    }
}
