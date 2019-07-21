package com.wildbeeslabs.jentle.algorithms.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Event {
    public enum ID {
        Alias, DocumentEnd, DocumentStart, MappingEnd, MappingStart, Scalar, SequenceEnd, SequenceStart, StreamEnd, StreamStart
    }

    private String id;
    private final String startMark;
    private final String endMark;

    public abstract boolean is(final Event.ID id);
}
