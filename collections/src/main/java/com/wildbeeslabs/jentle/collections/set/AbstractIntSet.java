package com.wildbeeslabs.jentle.collections.set;

import java.util.NoSuchElementException;

/**
 * A skeletal implementation of the IntSet interface.
 *
 * @author Emina Torlak
 */
public abstract class AbstractIntSet extends AbstractIntCollection implements IntSet {

    /**
     * Constructs an empty int set.
     *
     * @ensures no this.ints'
     */
    protected AbstractIntSet() {
    }

    /**
     * Throws a NoSuchElementException if this is an empty set.
     *
     * @throws NoSuchElementException this.isEmpty()
     */
    final void checkNonEmpty() {
        if (isEmpty()) throw new NoSuchElementException("no this.ints");
    }

    /**
     * Returns an ascending iterator over all elements in this set.
     * This method calls this.iterator(Integer.MIN_VALUE, Integer.MAX_VALUE).
     *
     * @return an ascending iterator over all elements in this set.
     */
    public IntIterator iterator() {
        return iterator(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Returns the first element returned by this.iterator().
     * If this set is empty, throws a NoSuchElementException().
     *
     * @return min(this.ints)
     * @throws NoSuchElementException no this.ints
     */
    public int min() {
        return iterator().next();
    }

    /**
     * Returns the first element returned by this.iterator(Integer.MAX_VALUE, Integer.MIN_VALUE).
     * If this set is empty, throws a NoSuchElementException().
     *
     * @return max(this.ints)
     * @throws NoSuchElementException no this.ints
     */
    public int max() {
        return iterator(Integer.MAX_VALUE, Integer.MIN_VALUE).next();
    }

    /**
     * Returns the result of calling super.clone().
     *
     * @return the result of calling super.clone()
     * @see java.lang.Object#clone()
     */
    public IntSet clone() throws CloneNotSupportedException {
        return (IntSet) super.clone();
    }

    /**
     * Compares the specified object with this set for equality.
     * Returns true if the specified object is also an IntSet,
     * the two sets have the same size, and every member of the
     * specified set is contained in this set (or equivalently,
     * every member of this set is contained in the specified set).
     * This definition ensures that the equals method works properly
     * across different implementations of the IntSet interface.
     *
     * @return o instanceof IntSet and o.size() = this.size() and this.containsAll(o)
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        else if (o instanceof IntSet) {
            final IntSet s = (IntSet) o;
            return size() == s.size() && containsAll(s);
        } else return false;
    }

    /**
     * Returns the hash code value for this set. The hash code of a set is
     * defined to be the {@link Ints#superFastHash(int[])} of the elements in the set,
     * taken in the ascending order of values.
     * This ensures that s1.equals(s2) implies that s1.hashCode()==s2.hashCode()
     * for any two IntSets s1 and s2, as required by the general contract of the Object.hashCode method.
     *
     * @return Ints.superFastHash(this.toArray ())
     */
    public int hashCode() {
        int hash = size();
        for (IntIterator iter = iterator(); iter.hasNext(); ) {
            hash = Ints.superFastHashIncremental(iter.next(), hash);
        }
        return Ints.superFastHashAvalanche(hash);
    }

    /**
     * Returns a string representation of this int set.
     *
     * @return a string representation of this int set.
     */
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        final IntIterator itr = iterator();
        if (itr.hasNext()) buf.append(itr.next());
        while (itr.hasNext()) {
            buf.append(", ");
            buf.append(itr.next());
        }
        buf.append("}");
        return buf.toString();
    }
}
