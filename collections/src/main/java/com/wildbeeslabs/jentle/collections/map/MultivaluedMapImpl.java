//package com.wildbeeslabs.jentle.collections.map;
//
//import javax.ws.rs.core.MultivaluedMap;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Implementation of {@link MultivaluedMap}.
// *
// * @author <a href="mailto:jmnarloch@gmail.com">Jakub Narloch</a>
// */
//public class MultivaluedMapImpl<K, V> extends HashMap<K, List<V>> implements MultivaluedMap<K, V> {
//
//    /**
//     * Creates new instance of {@link MultivaluedMapImpl} class.
//     */
//    public MultivaluedMapImpl() {
//        // empty constructor
//    }
//
//    /**
//     * Creates new instance of {@link MultivaluedMapImpl} from specfied map
//     *
//     * @param map the map
//     */
//    public MultivaluedMapImpl(MultivaluedMap<K, V> map) {
//
//        addAll(map);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void putSingle(K key, V value) {
//
//        List<V> list = new ArrayList<V>();
//        list.add(value);
//        put(key, list);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void add(K k, V v) {
//
//        getList(k).add(v);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public V getFirst(K k) {
//
//        List<V> list = get(k);
//
//        return list != null ? list.get(0) : null;
//    }
//
//    /**
//     * Adds all elements of given map.
//     *
//     * @param map the map
//     */
//    private void addAll(MultivaluedMap<K, V> map) {
//
//        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
//
//            getList(entry.getKey()).addAll(entry.getValue());
//        }
//    }
//
//    /**
//     * Retrieves the list associated with given key.
//     *
//     * @param k the key
//     *
//     * @return the list associated with given key
//     */
//    private List<V> getList(K k) {
//
//        List<V> list = get(k);
//
//        if (list == null) {
//            list = new ArrayList<V>();
//            put(k, list);
//        }
//
//        return list;
//    }
//}
