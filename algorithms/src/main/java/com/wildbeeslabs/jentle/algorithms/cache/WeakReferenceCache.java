package com.wildbeeslabs.jentle.algorithms.cache;

import lombok.Getter;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Cache2 implementation that keeps values in the cache until the garbage collector has removed them. Unlike the
 * WeakHashMap, which uses weak references on the keys, this Cache2 uses weak references on the values.
 * <p/>
 * Values are Weakly referenced, which means they are not eligible for removal as long as any other references to the
 * value exist.
 * <p/>
 * Items expire once the garbage collector has removed them. Some time after they have been removed, the entry
 * listeners are being notified thereof. Note that notification are emitted when the cache is being accessed (either
 * for reading or writing). If the cache is not being accessed for a longer period of time, it may occur that listeners
 * are not notified.
 *
 * @author Allard Buijze
 * @since 2.2.1
 */
public class WeakReferenceCache implements Cache {
    private final ConcurrentMap<Object, Entry> cache = new ConcurrentHashMap<>();
    private final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    private final Set<EntryListener> adapters = new CopyOnWriteArraySet<>();

    @Override
    public void registerCacheEntryListener(EntryListener entryListener) {
        this.adapters.add(entryListener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> V get(K key) {
        purgeItems();
        final Reference<Object> entry = cache.get(key);

        final V returnValue = entry == null ? null : (V) entry.get();
        if (returnValue != null) {
            for (EntryListener adapter : adapters) {
                adapter.onEntryRead(key, returnValue);
            }
        }
        return returnValue;
    }

    @Override
    public <K, V> void put(K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values not supported");
        }

        purgeItems();
        if (cache.put(key, new Entry(key, value)) != null) {
            for (EntryListener adapter : adapters) {
                adapter.onEntryUpdated(key, value);
            }
        } else {
            for (EntryListener adapter : adapters) {
                adapter.onEntryCreated(key, value);
            }
        }
    }

    @Override
    public <K, V> boolean putIfAbsent(K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values not supported");
        }
        purgeItems();
        if (cache.putIfAbsent(key, new Entry(key, value)) == null) {
            for (EntryListener adapter : adapters) {
                adapter.onEntryCreated(key, value);
            }
            return true;
        }
        return false;
    }

    @Override
    public <K> boolean remove(K key) {
        if (cache.remove(key) != null) {
            for (EntryListener adapter : adapters) {
                adapter.onEntryRemoved(key);
            }
            return true;
        }
        return false;
    }

    @Override
    public <K> boolean containsKey(K key) {
        purgeItems();
        final Reference<Object> entry = cache.get(key);

        return entry != null && entry.get() != null;
    }

    private void purgeItems() {
        Entry purgedEntry;
        while ((purgedEntry = (Entry) referenceQueue.poll()) != null) {
            if (cache.remove(purgedEntry.getKey()) != null) {
                for (EntryListener adapter : adapters) {
                    adapter.onEntryExpired(purgedEntry.getKey());
                }
            }
        }
    }

    @Getter
    private class Entry extends WeakReference<Object> {
        private final Object key;

        public Entry(final Object key, final Object value) {
            super(value, referenceQueue);
            this.key = key;
        }
    }
}
