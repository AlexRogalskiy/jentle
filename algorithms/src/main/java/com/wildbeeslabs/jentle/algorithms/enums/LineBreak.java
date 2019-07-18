package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * Platform dependent line break.
 */
public enum LineBreak {
    WIN("\r\n"), MAC("\r"), UNIX("\n");

    private String lineBreak;

    private LineBreak(String lineBreak) {
        this.lineBreak = lineBreak;
    }

    public String getString() {
        return lineBreak;
    }

    @Override
    public String toString() {
        return "Line break: " + name();
    }

    public static LineBreak getPlatformLineBreak() {
        String platformLineBreak = System.getProperty("line.separator");
        for (LineBreak lb : values()) {
            if (lb.lineBreak.equals(platformLineBreak)) {
                return lb;
            }
        }
        return LineBreak.UNIX;
    }
}
