/*
 * The MIT License
 *
 * Copyright 2017 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.collections.list;

import com.wildbeeslabs.jentle.collections.interfaces.IList;
import com.wildbeeslabs.jentle.collections.list.node.ACListNode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom string implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode
public class CString implements Serializable, Comparable<CString>, CharSequence {

    private final CStringList stringList;

    public CString(final String value) {
        this.stringList = new CStringList(value);
    }

    /**
     *
     * Custom string list implementation
     *
     * @author Alex
     * @version 1.0.0
     * @since 2017-08-07
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    private static class CStringList extends ACList<CStringList.StringItem, CStringList.StringItemNode> implements IList<CStringList.StringItem, CStringList.StringItemNode> {

        /**
         * Default string item size
         */
        public static final int DEFAULT_ITEM_SIZE = 16;

        @Data
        @EqualsAndHashCode
        @ToString
        protected static class StringItem {

            private int[] symbols = new int[CStringList.DEFAULT_ITEM_SIZE];
            private int size = 0;

            public StringItem(final StringItem item) {
                this(item.getSymbols(), 0, item.getSize());
            }

            public StringItem(int symbol) {
                this.add(symbol);
            }

            public StringItem(int[] symbols) {
                this(symbols, 0, symbols.length);
            }

            public StringItem(int[] symbols, int start, int end) {
                assert (start >= 0 && end >= 0 && start <= end && start < this.symbols.length && end < symbols.length);
                this.symbols = Arrays.copyOfRange(symbols, start, end);
                this.size = symbols.length;
            }

            public void add(int symbol) {
                this.symbols[this.size++] = symbol;
            }

            public int getSymbol(int index) {
                assert (index > 0 && index < this.size);
                return this.symbols[index];
            }

            public void setSymbol(int index, int codePoint) {
                assert (index > 0 && index < this.size);
                this.symbols[index] = codePoint;
            }
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        @ToString(callSuper = true)
        protected static class StringItemNode extends ACListNode<CStringList.StringItem, CStringList.StringItemNode> {

            public StringItemNode() {
                this(null);
            }

            public StringItemNode(final StringItem data) {
                this(data, null);
            }

            public StringItemNode(final StringItem data, StringItemNode next) {
                super(data, next);
            }
        }

        @Data
        @EqualsAndHashCode
        @ToString
        protected static class SymbolPosition {

            private StringItemNode item;
            private int position;

            public SymbolPosition(final StringItemNode item, int position) {
                this.item = item;
                this.position = position;
            }
        }

        public CStringList() {
            this(null);
        }

        public CStringList(final CharSequence value) {
            this.append(value);
        }

        public void append(final CharSequence value) {
            if (Objects.nonNull(value) && value.length() > 0) {
                this.append(value.codePoints().toArray());
            }
        }

        public void append(int[] codepoints) {
            int full = codepoints.length / CStringList.DEFAULT_ITEM_SIZE;
            int half = codepoints.length % CStringList.DEFAULT_ITEM_SIZE;
            int index = 0;
            while (index < full) {
                final CStringList.StringItem item = new CStringList.StringItem(codepoints, index * CStringList.DEFAULT_ITEM_SIZE, CStringList.DEFAULT_ITEM_SIZE);
                this.addLast(item);
                index++;
            }
            if (half > 0) {
                final CStringList.StringItem item = new CStringList.StringItem(codepoints, index * CStringList.DEFAULT_ITEM_SIZE, codepoints.length);
                this.addLast(item);
            }
        }

        public void append(char value) {
            final CStringList.StringItemNode lastNode = this.getLast();
            if (Objects.isNull(lastNode)) {
                this.setFirst(new CStringList.StringItemNode(new CStringList.StringItem(value)));
            } else if (lastNode.getData().getSize() < CStringList.DEFAULT_ITEM_SIZE) {
                lastNode.getData().add(value);
            } else {
                lastNode.setNext(new CStringList.StringItemNode(new CStringList.StringItem(value)));
                this.size++;
            }
        }

        public void append(final CStringList.StringItemNode first, final CStringList.StringItemNode second) {
            final CStringList.StringItemNode newNode = new CStringList.StringItemNode(new CStringList.StringItem(second.getData()));
            this.insertAfter(first, newNode);
        }

        public CStringList.SymbolPosition findPosition(int index) {
            if (index < 0) {
                throw new IndexOutOfBoundsException(String.format("ERROR: invalid index=(%d), cannot be lower than zero", index));
            }
            int curIndex = index;
            CStringList.StringItemNode current = this.first;
            while (Objects.nonNull(current) && current.getData().getSize() <= curIndex) {
                curIndex -= current.getData().getSize();
                current = current.getNext();
            }
            if (Objects.isNull(current)) {
                throw new IndexOutOfBoundsException(String.format("ERROR: invalid index=(%d), cannot exceed max value", index));
            }
            return new SymbolPosition(current, curIndex);
        }

        @Override
        public CStringList.StringItemNode getLast() {
            CStringList.StringItemNode current = this.getFirst();
            CStringList.StringItemNode last = null;
            while (Objects.nonNull(current)) {
                last = current;
                current = current.getNext();
            }
            while (Objects.nonNull(current) && last.getData().size + current.getData().size <= CStringList.DEFAULT_ITEM_SIZE) {
                for (int i = last.getData().size; i < last.getData().size + current.getData().size; i++) {
                    last.getData().symbols[i] = current.getData().symbols[i - last.getData().getSize()];
                }
                last.getData().size += current.getData().size;
                last.setNext(current.getNext());
                current = current.getNext();
            }
            return last;
        }

        @Override
        public Iterator<StringItem> iterator() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ListIterator<StringItem> listIterator(int index) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected CStringList.StringItemNode createNode(final Optional<? extends CStringList.StringItem> value) {
            if (value.isPresent()) {
                return new CStringList.StringItemNode(value.get());
            }
            return new CStringList.StringItemNode();
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (CStringList.StringItemNode current = this.stringList.getFirst(); Objects.nonNull(current);) {
            for (int i = 0; i < current.getData().getSize(); i++) {
                sb.appendCodePoint(current.getData().getSymbols()[i]);
            }
            current = current.getNext();
        }
        return sb.toString();
    }

    public int length() {
        int length = 0;
        for (CStringList.StringItemNode current = this.stringList.getFirst(); Objects.nonNull(current);) {
            length += current.getData().getSize();
            current = current.getNext();
        }
        return length;
    }

    public int codePointAt(int index) {
        final CStringList.SymbolPosition sp = this.stringList.findPosition(index);
        return sp.item.getData().getSymbol(index);
    }

    public char[] charsAt(int index) {
        return Character.toChars(this.codePointAt(index));
    }

    public void setCodePointAt(int index, int codePointAt) {
        final CStringList.SymbolPosition sp = this.stringList.findPosition(index);
        sp.item.getData().setSymbol(index, codePointAt);
    }

    @Override
    public int compareTo(final CString o) {
        CStringList.StringItemNode headFirst = this.stringList.first;
        CStringList.StringItemNode headLast = o.stringList.first;
        while (Objects.nonNull(headFirst) && Objects.nonNull(headLast)) {
            int value = Objects.compare(headFirst.getData(), headLast.getData(), this.stringList.cmp);
            if (value != 0) {
                return value;
            }
            headFirst = headFirst.getNext();
            headLast = headLast.getNext();
        }
        return (this.stringList.size - o.stringList.size);
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void append(final String value) {
        this.stringList.append(value);
    }

    public void append(char value) {
        this.stringList.append(value);
    }

    public void append(final CString cString) {
        CStringList.StringItemNode current = this.stringList.getFirst();
        while (Objects.nonNull(current)) {
            this.stringList.append(current.getData().getSymbols());
            current = current.getNext();
        }
    }

    protected void append(CStringList.StringItemNode node, final CString cString) {
        CStringList.StringItemNode current = this.stringList.getFirst();
        final CStringList.StringItemNode next = node.getNext();
        while (Objects.nonNull(current)) {
            this.stringList.append(node, current);
            node = current;
            current = current.getNext();
        }
        node.setNext(next);
    }

    public boolean insertAt(int index, final CString cString) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(String.format("ERROR: invalid index=(%d), cannot be lower than zero", index));
        }
        CStringList.StringItemNode current = this.stringList.getFirst();
        CStringList.StringItemNode previous = null;
        int curIndex = index;
        while (Objects.nonNull(current) && current.getData().getSize() <= curIndex) {
            curIndex -= current.getData().getSize();
            previous = current;
            current = current.getNext();
        }
        if (Objects.isNull(current) && curIndex != 0) {
            throw new IndexOutOfBoundsException(String.format("ERROR: invalid index=(%d), cannot exceed max value", index));
        }
        if (Objects.isNull(cString) || Objects.isNull(cString.stringList.getFirst())) {
            return false;
        }
        CStringList.StringItemNode last = cString.stringList.getLast();
        if (curIndex > 0) {
            CStringList.StringItemNode newItem = new CStringList.StringItemNode(new CStringList.StringItem(Arrays.copyOfRange(current.getData().getSymbols(), curIndex, current.getData().getSize() - curIndex)), current.getNext());
            current.getData().setSize(curIndex);
            current.setNext(newItem);
            previous = current;
            current = newItem;
        }
        if (Objects.isNull(previous)) {
            this.append(cString);
        } else {
            this.append(previous, cString);
        }
        last.setNext(current);
        return true;
    }

    public boolean insertAt(int index, final String value) {
        return this.insertAt(index, new CString(value));
    }
}
