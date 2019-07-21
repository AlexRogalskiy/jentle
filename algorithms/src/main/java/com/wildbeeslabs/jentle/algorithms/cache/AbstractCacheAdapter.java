//package com.wildbeeslabs.jentle.algorithms.cache;
//
//import org.axonframework.common.Registration;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
///**
// * Abstract implementation of the Cache2 interface which makes it easier to implement Adapters.
// *
// * @param <L> The type of event listener the cache uses
// * @author Allard Buijze
// * @since 2.1.2
// */
//public abstract class AbstractCacheAdapter<L> implements Cache {
//
//    private final ConcurrentMap<Cache.EntryListener, L> registeredAdapters = new ConcurrentHashMap<>();
//
//    /**
//     * Creates an adapter for the given {@code cacheEntryListener}. The adapter must forward all incoming
//     * notifications to the respective methods on the {@code cacheEntryListener}.
//     *
//     * @param cacheEntryListener The listener to create an adapter for
//     * @return an adapter that forwards notifications
//     */
//    protected abstract L createListenerAdapter(Cache.EntryListener cacheEntryListener);
//
//    @Override
//    public void registerCacheEntryListener(Cache.EntryListener entryListener) {
//        L adapter = createListenerAdapter(entryListener);
//        Registration registration
//            = registeredAdapters.putIfAbsent(entryListener, adapter) == null ? doRegisterListener(adapter) : null;
//        return () -> {
//            L removedAdapter = registeredAdapters.remove(entryListener);
//            if (removedAdapter != null) {
//                if (registration != null) {
//                    registration.cancel();
//                }
//                return true;
//            }
//            return false;
//        };
//    }
//
//    /**
//     * Registers the given listener with the cache implementation
//     *
//     * @param listenerAdapter the listener to register
//     * @return a handle to unregister the listener
//     */
//    protected abstract Registration doRegisterListener(L listenerAdapter);
//}
