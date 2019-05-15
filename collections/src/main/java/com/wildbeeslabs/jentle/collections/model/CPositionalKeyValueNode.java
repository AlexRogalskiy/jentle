package com.wildbeeslabs.jentle.collections.model;

import com.wildbeeslabs.jentle.collections.iface.locator.Locator;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class CPositionalKeyValueNode<K, V> extends CKeyValueNode<K, V> implements Locator<CPositionalKeyValueNode<K, V>> {

    private Position<CPositionalKeyValueNode<K, V>> position;

    public CPositionalKeyValueNode() {
        this(null, null);
    }

    public CPositionalKeyValueNode(final CPositionalKeyValueNode<K, V> data) {
        this(data.getKey(), data.getValue());
    }

    public CPositionalKeyValueNode(final K key, final V value) {
        this(key, value, null);
    }

    public CPositionalKeyValueNode(final K key, final V value, final Position<CPositionalKeyValueNode<K, V>> position) {
        super(key, value);
        this.position = position;
    }

    @Override
    public Position<CPositionalKeyValueNode<K, V>> position() {
        return this.position();
    }

    /**
     * Returns {@link CKeyValueNodeExtended} by input parameters
     *
     * @param <K>      type of node key
     * @param <V>      type of node value
     * @param key      - initial input node key {@code K}
     * @param value    - initial input node value {@code V}
     * @param position - initial input node {@link Position}
     * @return {@link CKeyValueNodeExtended}
     */
    public static <K, V> CPositionalKeyValueNode<K, V> of(final K key, final V value, final Position<CPositionalKeyValueNode<K, V>> position) {
        return new CPositionalKeyValueNode<>(key, value, position);
    }
}
