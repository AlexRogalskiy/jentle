package com.wildbeeslabs.jentle.collections.set.iface;

import com.wildbeeslabs.jentle.collections.set.ConcurrentSet;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A ConcurrentHashSet.
 * <p>
 * Offers same concurrency as ConcurrentHashMap but for a Set
 *
 * @author <a href="tim.fox@jboss.com">Tim Fox</a>
 * @version <tt>$Revision: 1935 $</tt>
 * <p>
 * $Id: ConcurrentReaderHashSet.java 1935 2007-01-09 23:29:20Z clebert.suconic@jboss.com $
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements ConcurrentSet<E> {
    private final ConcurrentMap<E, Object> theMap;

    private static final Object dummy = new Object();

    public ConcurrentHashSet() {
        theMap = new ConcurrentHashMap<E, Object>();
    }

    @Override
    public int size() {
        return theMap.size();
    }

    @Override
    public Iterator<E> iterator() {
        return theMap.keySet().iterator();
    }

    @Override
    public boolean isEmpty() {
        return theMap.isEmpty();
    }

    @Override
    public boolean add(final E o) {
        return theMap.put(o, ConcurrentHashSet.dummy) == null;
    }

    @Override
    public boolean contains(final Object o) {
        return theMap.containsKey(o);
    }

    @Override
    public void clear() {
        theMap.clear();
    }

    @Override
    public boolean remove(final Object o) {
        return theMap.remove(o) == ConcurrentHashSet.dummy;
    }

    public boolean addIfAbsent(final E o) {
        Object obj = theMap.putIfAbsent(o, ConcurrentHashSet.dummy);

        return obj == null;
    }

}
