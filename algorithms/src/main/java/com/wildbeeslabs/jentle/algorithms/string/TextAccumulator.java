package com.wildbeeslabs.jentle.algorithms.string;

/**
 * Simple utility class used to efficiently accumulate and concatenate
 * text passed in various forms
 */
public final class TextAccumulator {
    private String mText = null;

    private StringBuilder mBuilder = null;

    public TextAccumulator() {
    }

    public boolean hasText() {
        return (mBuilder != null) || (mText != null);
    }

    public void addText(String text) {
        int len = text.length();
        if (len > 0) {
            // Any prior text?
            if (mText != null) {
                mBuilder = new StringBuilder(mText.length() + len);
                mBuilder.append(mText);
                mText = null;
            }
            if (mBuilder != null) {
                mBuilder.append(text);
            } else {
                mText = text;
            }
        }
    }

    public void addText(char[] buf, int start, int end) {
        int len = end - start;
        if (len > 0) {
            // Any prior text?
            if (mText != null) {
                mBuilder = new StringBuilder(mText.length() + len);
                mBuilder.append(mText);
                mText = null;
            } else if (mBuilder == null) {
                /* more efficient to use a builder than a string; and although
                 * could use a char ArrayUtils, StringBuilder has the benefit of
                 * being able to share the ArrayUtils, eventually.
                 */
                mBuilder = new StringBuilder(len);
            }
            mBuilder.append(buf, start, end - start);
        }
    }

    public String getAndClear() {
        if (mText != null) {
            String result = mText;
            mText = null;
            return result;
        }
        if (mBuilder != null) {
            String result = mBuilder.toString();
            mBuilder = null;
            return result;
        }
        return "";
    }
}
