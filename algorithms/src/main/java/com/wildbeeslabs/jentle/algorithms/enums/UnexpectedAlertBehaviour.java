package com.wildbeeslabs.jentle.algorithms.enums;

public enum UnexpectedAlertBehaviour {

    ACCEPT("accept"),
    DISMISS("dismiss"),
    ACCEPT_AND_NOTIFY("accept and notify"),
    DISMISS_AND_NOTIFY("dismiss and notify"),
    IGNORE("ignore");

    private String text;

    private UnexpectedAlertBehaviour(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.valueOf(text);
    }

    public static UnexpectedAlertBehaviour fromString(String text) {
        if (text != null) {
            for (UnexpectedAlertBehaviour b : UnexpectedAlertBehaviour.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
