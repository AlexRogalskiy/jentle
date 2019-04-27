package com.wildbeeslabs.jentle.collections.queue.impl;

import com.wildbeeslabs.jentle.collections.list.node.CLocatorListNodeExtended;
import com.wildbeeslabs.jentle.collections.model.impl.CPositionalKeyValueNode;
import com.wildbeeslabs.jentle.collections.model.interfaces.Locator;
import com.wildbeeslabs.jentle.collections.queue.iface.IPriorityQueue2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/**
 * Custom sorted sequence {@link IPriorityQueue2} implementation (version 2)
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CSortedSequencePriorityQueue2<K, V> extends CSortedSequencePriorityQueue<K, V, CPositionalKeyValueNode<K, V>, CLocatorListNodeExtended<K, V>> implements IPriorityQueue2<CLocatorListNodeExtended<K, V>> {

    public CSortedSequencePriorityQueue2(final Comparator<? super K> comparator) {
        super(comparator);
    }

    protected Locator<CPositionalKeyValueNode<K, V>> locateInsert(final CPositionalKeyValueNode<K, V> locItem) {
        return null;
    }
}
