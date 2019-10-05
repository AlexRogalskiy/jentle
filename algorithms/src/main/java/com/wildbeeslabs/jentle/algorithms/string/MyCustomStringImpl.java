package com.wildbeeslabs.jentle.algorithms.string;

public class MyCustomStringImpl implements CharSequence {
    private final String myString;

    public MyCustomStringImpl(String s) {
        this.myString = s;
    }

    @Override
    public int length() {
        return myString.length();
    }

    @Override
    public char charAt(int i) {
        return myString.charAt(i);
    }

    @Override
    public CharSequence subSequence(int i, int j) {
        return myString.subSequence(i, j);
    }

    @Override
    public String toString() {
        return myString;
    }
}
