package com.wildbeeslabs.jentle.collections.map.impl;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import java.util.*;

public class Dataset extends AbstractMap<String, String> {

    /**
     * Default data prefix
     */
    private static final String DEFAULT_DATA_PREFIX = "data-";

    private final Attributes attributes;

    private Dataset(final Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return new EntrySet();
    }

    @Override
    public String put(final String key, final String value) {
        final String dataKey = dataKey(key);
        final String oldValue = this.attributes.hasKey(dataKey) ? attributes.get(dataKey) : null;
        this.attributes.put(dataKey, value);
        return oldValue;
    }

    private static String dataKey(final String key) {
        return DEFAULT_DATA_PREFIX + key;
    }

    private class EntrySet extends AbstractSet<Entry<String, String>> {

        @Override
        public Iterator<Entry<String, String>> iterator() {
            return new DatasetIterator();
        }

        @Override
        public int size() {
            int count = 0;
            Iterator iter = new DatasetIterator();
            while (iter.hasNext())
                count++;
            return count;
        }
    }

    private class DatasetIterator implements Iterator<Map.Entry<String, String>> {
        private Iterator<Attribute> attrIter = attributes.iterator();
        private Attribute attr;

        public boolean hasNext() {
            while (this.attrIter.hasNext()) {
                this.attr = attrIter.next();
                if (this.isDataAttribute(this.attr.getKey())) {
                    return true;
                }
            }
            return false;
        }

        protected boolean isDataAttribute(final String key) {
            return key.startsWith(DEFAULT_DATA_PREFIX) && key.length() > DEFAULT_DATA_PREFIX.length();
        }

        public Entry<String, String> next() {
            return new Attribute(this.attr.getKey().substring(DEFAULT_DATA_PREFIX.length()), this.attr.getValue());
        }

        public void remove() {
            attributes.remove(this.attr.getKey());
        }
    }
}
