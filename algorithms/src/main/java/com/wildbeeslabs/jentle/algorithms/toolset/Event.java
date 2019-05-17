package com.wildbeeslabs.jentle.algorithms.toolset;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * Enumerates events which can be monitored.
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Event {

    public static final Event MIME_BODY_PREMATURE_END = Event.of("Body part ended prematurely. Boundary detected in header or EOF reached.");
    public static final Event HEADERS_PREMATURE_END = Event.of("Unexpected end of headers detected. Higher level boundary detected or EOF reached.");
    public static final Event INVALID_HEADER = Event.of("Invalid header encountered");
    public static final Event OBSOLETE_HEADER = Event.of("Obsolete header encountered");

    private final String code;

    public Event(final String code) {
        this.code = Objects.requireNonNull(code, "Code should not be null!");
    }

    public static Event of(final String code) {
        return new Event(code);
    }
}
