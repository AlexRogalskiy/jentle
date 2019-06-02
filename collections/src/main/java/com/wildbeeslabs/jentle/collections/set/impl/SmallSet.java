package com.wildbeeslabs.jentle.collections.set.impl;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A set of at most two elements.
 *
 * @author Eric Bruneton
 */
class SmallSet extends AbstractSet implements Iterator {

    // if e1 is null, e2 must be null; otherwise e2 must be different from e1

    Object e1, e2;

    static final Set EMPTY_SET = new SmallSet(null, null);

    SmallSet(final Object e1, final Object e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    // -------------------------------------------------------------------------
    // Implementation of inherited abstract methods
    // -------------------------------------------------------------------------

    public Iterator iterator() {
        return new SmallSet(e1, e2);
    }

    public int size() {
        return e1 == null ? 0 : (e2 == null ? 1 : 2);
    }

    // -------------------------------------------------------------------------
    // Implementation of the Iterator interface
    // -------------------------------------------------------------------------

    public boolean hasNext() {
        return e1 != null;
    }

    public Object next() {
        Object e = e1;
        e1 = e2;
        e2 = null;
        return e;
    }

    public void remove() {
    }

    // -------------------------------------------------------------------------
    // Utility methods
    // -------------------------------------------------------------------------

    Set union(final SmallSet s) {
        if ((s.e1 == e1 && s.e2 == e2) || (s.e1 == e2 && s.e2 == e1)) {
            return this; // if the two sets are equal, return this
        }
        if (s.e1 == null) {
            return this; // if s is empty, return this
        }
        if (e1 == null) {
            return s; // if this is empty, return s
        }
        if (s.e2 == null) { // s contains exactly one element
            if (e2 == null) {
                return new SmallSet(e1, s.e1); // necessarily e1 != s.e1
            } else if (s.e1 == e1 || s.e1 == e2) { // s is included in this
                return this;
            }
        }
        if (e2 == null) { // this contains exactly one element
            // if (s.e2 == null) { // cannot happen
            // return new SmallSet(e1, s.e1); // necessarily e1 != s.e1
            // } else
            if (e1 == s.e1 || e1 == s.e2) { // this in included in s
                return s;
            }
        }
        // here we know that there are at least 3 distinct elements
        HashSet r = new HashSet(4);
        r.add(e1);
        if (e2 != null) {
            r.add(e2);
        }
        r.add(s.e1);
        if (s.e2 != null) {
            r.add(s.e2);
        }
        return r;
    }
}
