package com.wildbeeslabs.jentle.algorithms.toolset.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class IntegrationEvent extends ApplicationEvent {

    private final Throwable cause;

    public IntegrationEvent(final Object source) {
        super(source);
        this.cause = null;
    }

    public IntegrationEvent(final Object source, final Throwable cause) {
        super(source);
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [source=" + this.getSource() + (Objects.isNull(this.cause) ? "" : ", cause=" + this.cause) + "]";
    }
}
