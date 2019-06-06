package com.wildbeeslabs.jentle.algorithms.toolset.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.EventObject;

/**
 * Class to be extended by all application events. Abstract as it
 * doesn't make sense for generic events to be published directly.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
@Data
@EqualsAndHashCode
@ToString
public abstract class ApplicationEvent extends EventObject {

    /**
     * use serialVersionUID from Spring 1.2 for interoperability.
     */
    private static final long serialVersionUID = 7099057708183571937L;

    /**
     * System time when the event happened.
     */
    private final long timestamp;


    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ApplicationEvent(final Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }


    /**
     * Return the system time in milliseconds when the event happened.
     */
    public final long getTimestamp() {
        return this.timestamp;
    }
}
