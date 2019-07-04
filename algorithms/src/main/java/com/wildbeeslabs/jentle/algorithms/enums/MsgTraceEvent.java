package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Common tracing events.
 *
 * @author Libor Kramolis (libor.kramolis at oracle.com)
 * @since 2.3
 */
@Getter
@RequiredArgsConstructor
public enum MsgTraceEvent implements Event {
    RI_BEFORE(Level.TRACE, "RI", "%s BEFORE context.proceed()"),
    RI_AFTER(Level.TRACE, "RI", "%s AFTER context.proceed()"),
    RI_SUMMARY(Level.SUMMARY, "RI", "ReadFrom summary: %s interceptors"),
    MBR_FIND(Level.TRACE, "MBR", "Find MBR for type=[%s] genericType=[%s] mediaType=[%s] annotations=%s"),
    MBR_NOT_READABLE(Level.VERBOSE, "MBR", "%s is NOT readable"),
    MBR_SELECTED(Level.TRACE, "MBR", "%s IS readable"),
    MBR_SKIPPED(Level.VERBOSE, "MBR", "%s is skipped"),
    MBR_READ_FROM(Level.TRACE, "MBR", "ReadFrom by %s"),
    MBW_FIND(Level.TRACE, "MBW", "Find MBW for type=[%s] genericType=[%s] mediaType=[%s] annotations=%s"),
    MBW_NOT_WRITEABLE(Level.VERBOSE, "MBW", "%s is NOT writeable"),
    MBW_SELECTED(Level.TRACE, "MBW", "%s IS writeable"),
    MBW_SKIPPED(Level.VERBOSE, "MBW", "%s is skipped"),
    MBW_WRITE_TO(Level.TRACE, "MBW", "WriteTo by %s"),
    WI_BEFORE(Level.TRACE, "WI", "%s BEFORE context.proceed()"),
    WI_AFTER(Level.TRACE, "WI", "%s AFTER context.proceed()"),
    WI_SUMMARY(Level.SUMMARY, "WI", "WriteTo summary: %s interceptors");

    private final Level level;
    private final String name;
    private final String messageFormat;
}
