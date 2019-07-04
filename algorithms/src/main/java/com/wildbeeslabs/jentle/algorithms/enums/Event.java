package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Type of event.
 */
public interface Event {

    /**
     * Name of event, should be unique.
     * Is logged by JDK logger.
     *
     * @return event name.
     */
    String getName();

    /**
     * Level of event.
     * Is used to check if the event is logged according to application/request settings.
     *
     * @return event trace level.
     */
    Level getLevel();

    /**
     * Message format. Use {@link String#format(String, Object...)} format.
     * Can be null. In that case message arguments are separated by space.
     *
     * @return message format
     */
    String getMessageFormat();
}
