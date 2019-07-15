package com.wildbeeslabs.jentle.algorithms.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnexpectedAlertBehaviour {
    ACCEPT("accept"),
    DISMISS("dismiss"),
    ACCEPT_AND_NOTIFY("accept and notify"),
    DISMISS_AND_NOTIFY("dismiss and notify"),
    IGNORE("ignore");

    private final String text;

    @Override
    public String toString() {
        return String.valueOf(this.text);
    }

    public static UnexpectedAlertBehaviour fromString(final String text) {
        if (text != null) {
            for (final UnexpectedAlertBehaviour b : UnexpectedAlertBehaviour.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
