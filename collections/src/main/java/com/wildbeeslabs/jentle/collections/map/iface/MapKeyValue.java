package com.wildbeeslabs.jentle.collections.map.iface;

import java.io.Serializable;
import java.util.Collection;

public interface MapKeyValue<T extends Serializable, V extends Object, S extends Serializable> extends Serializable {
    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#put(java.io.Serializable, java.lang.Object, java.io.Serializable)
     */
    V put(final T id, V item, final S keyspace);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#contains(java.io.Serializable, java.io.Serializable)
     */
    boolean contains(final T id, final S keyspace);

    /* (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#count(java.io.Serializable)
     */
    long count(final S keyspace);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#get(java.io.Serializable, java.io.Serializable)
     */
    V get(final T id, final S keyspace);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#delete(java.io.Serializable, java.io.Serializable)
     */
    V delete(final T id, final S keyspace);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#getAllOf(java.io.Serializable)
     */
    Collection<V> getAllOf(final S keyspace);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#deleteAllOf(java.io.Serializable)
     */
    void deleteAllOf(final S keyspace);

    /*
     * (non-Javadoc)
     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#clear()
     */
    void clear();

    /*
     * (non-Javadoc)
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    void destroy() throws Exception;
}
