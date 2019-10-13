//package com.wildbeeslabs.jentle.collections.map.impl;
//
//import com.sensiblemetrics.api.common.utils.ValidationUtils;
//import com.wildbeeslabs.jentle.collections.map.iface.MapKeyValue;
//import org.springframework.core.CollectionFactory;
//import org.springframework.util.Assert;
//import org.springframework.util.ClassUtils;
//
//import java.io.Serializable;
//import java.util.Collection;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * {@link MapKeyValue} implementation for {@link Map}.
// */
//public class DefaultMapKeyValue implements MapKeyValue<Serializable, Object, Serializable> {
//
//    private static final long serialVersionUID = 3083048721192220532L;
//
//    private final Class<? extends Map> keySpaceMapType;
//    private final Map<Serializable, Map<Serializable, Object>> store;
//
//    /**
//     * Create new {@link MapKeyValue} using {@link ConcurrentHashMap} as backing store type.
//     */
//    public DefaultMapKeyValue() {
//        this(ConcurrentHashMap.class);
//    }
//
//    /**
//     * Creates a new {@link MapKeyValue} using the given {@link Map} as backing store.
//     *
//     * @param mapType must not be {@literal null}.
//     */
//    public DefaultMapKeyValue(final Class<? extends Map> mapType) {
//        this(CollectionFactory.<Serializable, Map<Serializable, Object>>createMap(mapType, 100), mapType);
//    }
//
//    /**
//     * Create new instance of {@link MapKeyValue} using given dataStore for persistence.
//     *
//     * @param store must not be {@literal null}.
//     */
//    public DefaultMapKeyValue(final Map<Serializable, Map<Serializable, Object>> store) {
//        this(store, (Class<? extends Map>) ClassUtils.getUserClass(store));
//    }
//
//    /**
//     * Creates a new {@link MapKeyValue} with the given store and type to be used when creating key spaces.
//     *
//     * @param store           must not be {@literal null}.
//     * @param keySpaceMapType must not be {@literal null}.
//     */
//    private DefaultMapKeyValue(final Map<Serializable, Map<Serializable, Object>> store, final Class<? extends Map> keySpaceMapType) {
//        ValidationUtils.notNull(store, "Store must not be null.");
//        ValidationUtils.notNull(keySpaceMapType, "Map type to be used for key spaces must not be null!");
//
//        this.store = store;
//        this.keySpaceMapType = keySpaceMapType;
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#put(java.io.Serializable, java.lang.Object, java.io.Serializable)
//     */
//    @Override
//    public Object put(final Serializable id, final Object item, final Serializable keyspace) {
//        ValidationUtils.notNull(id, "Cannot add item with null id.");
//        ValidationUtils.notNull(keyspace, "Cannot add item for null collection.");
//
//        return this.getKeySpaceMap(keyspace).put(id, item);
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#contains(java.io.Serializable, java.io.Serializable)
//     */
//    @Override
//    public boolean contains(final Serializable id, final Serializable keyspace) {
//        return get(id, keyspace) != null;
//    }
//
//    /* (non-Javadoc)
//     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#count(java.io.Serializable)
//     */
//    @Override
//    public long count(final Serializable keyspace) {
//        return getKeySpaceMap(keyspace).size();
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#get(java.io.Serializable, java.io.Serializable)
//     */
//    @Override
//    public Object get(final Serializable id, final Serializable keyspace) {
//        Assert.notNull(id, "Cannot get item with null id.");
//        return getKeySpaceMap(keyspace).get(id);
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#delete(java.io.Serializable, java.io.Serializable)
//     */
//    @Override
//    public Object delete(final Serializable id, final Serializable keyspace) {
//        ValidationUtils.notNull(id, "Cannot delete item with null id.");
//        return getKeySpaceMap(keyspace).remove(id);
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#getAllOf(java.io.Serializable)
//     */
//    @Override
//    public Collection<Object> getAllOf(final Serializable keyspace) {
//        return getKeySpaceMap(keyspace).values();
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#deleteAllOf(java.io.Serializable)
//     */
//    @Override
//    public void deleteAllOf(final Serializable keyspace) {
//        getKeySpaceMap(keyspace).clear();
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.keyvalue.core.KeyValueAdapter#clear()
//     */
//    @Override
//    public void clear() {
//        this.store.clear();
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.beans.factory.DisposableBean#destroy()
//     */
//    @Override
//    public void destroy() {
//        this.clear();
//    }
//
//    /**
//     * Get map associated with given key space.
//     *
//     * @param keyspace must not be {@literal null}.
//     * @return
//     */
//    protected Map<Serializable, Object> getKeySpaceMap(final Serializable keyspace) {
//        ValidationUtils.notNull(keyspace, "Collection must not be null for lookup.");
//        final Map<Serializable, Object> map = this.store.get(keyspace);
//        if (map != null) {
//            return map;
//        }
//        this.addMapForKeySpace(keyspace);
//        return this.store.get(keyspace);
//    }
//
//    private void addMapForKeySpace(final Serializable keyspace) {
//        this.store.put(keyspace, CollectionFactory.<Serializable, Object>createMap(this.keySpaceMapType, 1000));
//    }
//}
