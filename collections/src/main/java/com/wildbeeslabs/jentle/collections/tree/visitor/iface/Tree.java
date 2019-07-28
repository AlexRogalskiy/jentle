package com.wildbeeslabs.jentle.collections.tree.visitor.iface;

public interface Tree extends Visited<TreeVisitor> {

    String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";

    String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    String NUMBERS = "0123456789";

    String ALL_MY_CHARS = LOWERCASE_CHARS + UPPERCASE_CHARS + NUMBERS;

    int LABEL_SIZE = 2;
}
