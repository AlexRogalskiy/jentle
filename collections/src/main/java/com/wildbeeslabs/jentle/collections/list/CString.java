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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

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
@EqualsAndHashCode(callSuper = false)
public class CString {
    
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
    @ToString
    private static class CStringList extends ACList<CStringList.StringItem, CStringList.StringItemNode> implements IList<CStringList.StringItem> {

        /**
         * Default string item size
         */
        public static final int DEFAULT_ITEM_SIZE = 16;
        
        @Override
        public void addLast(StringItem item) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void addFirst(StringItem item) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void insertAt(StringItem item, int index) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public Iterator<? extends StringItem> iterator() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Data
        @EqualsAndHashCode(callSuper = false)
        @ToString
        protected static class StringItem {
            
            private int[] symbols = new int[CStringList.DEFAULT_ITEM_SIZE];
            private int size = 0;
            
            public StringItem(int symbol) {
                this.add(symbol);
            }
            
            public StringItem(int[] symbols) {
                this.symbols = symbols;
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
        @ToString
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
        @EqualsAndHashCode(callSuper = false)
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
        
        public CStringList(final String value) {
            this.append(value);
        }
        
        public void append(final String value) {
            if (Objects.nonNull(value) && value.length() > 0) {
                this.append(value.codePoints().toArray());
            }
        }
        
        public void append(int[] codepoints) {
            int full = codepoints.length / CStringList.DEFAULT_ITEM_SIZE;
            int half = codepoints.length % CStringList.DEFAULT_ITEM_SIZE;
            int index = 0;
            while (index < full) {
                final CStringList.StringItem item = new CStringList.StringItem(Arrays.copyOfRange(codepoints, index * CStringList.DEFAULT_ITEM_SIZE, CStringList.DEFAULT_ITEM_SIZE));
                this.addToLast(item);
                index++;
            }
            if (half > 0) {
                final CStringList.StringItem item = new CStringList.StringItem(Arrays.copyOf(codepoints, index * CStringList.DEFAULT_ITEM_SIZE));
                this.addToLast(item);
            }
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
        protected CStringList.StringItemNode createNode(final CStringList.StringItem value) {
            return new CStringList.StringItemNode(value);
        }
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (CStringList.StringItemNode current = this.stringList.first; Objects.nonNull(current);) {
            for (int i = 0; i < current.getData().getSize(); i++) {
                sb.appendCodePoint(current.getData().getSymbols()[i]);
            }
            current = current.getNext();
        }
        return sb.toString();
    }
    
    public int length() {
        int length = 0;
        for (CStringList.StringItemNode current = this.stringList.first; Objects.nonNull(current);) {
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
    
    public void append(final String value) {
        this.stringList.append(value);
    }
    
    public void append(char value) {
        final CStringList.StringItemNode last = this.stringList.getLast();
        if (Objects.isNull(last)) {
            this.stringList.setFirst(new CStringList.StringItemNode(new CStringList.StringItem(value)));
        } else if (last.getData().getSize() < CStringList.DEFAULT_ITEM_SIZE) {
            last.getData().add(value);
        } else {
            last.setNext(new CStringList.StringItemNode(new CStringList.StringItem(value)));
        }
    }
}
