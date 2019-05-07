package com.wildbeeslabs.jentle.collections.tree.node;

import com.wildbeeslabs.jentle.collections.model.CKeyValueNode;
import lombok.*;

/**
 * Default {@link CKeyValueNode} implementation
 *
 * @param <K>
 * @param <V>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CRedBlackTreeNode<K, V> extends CKeyValueNode<K, V> {

    private boolean isRed;

    public CRedBlackTreeNode(final K key, final V value) {
        this(key, value, true);
    }

    public CRedBlackTreeNode(final K key, final V value, final boolean isRed) {
        super(key, value);
        this.isRed = isRed;
    }

    public void makeRed() {
        this.isRed = true;
    }

    public void makeBlack() {
        this.isRed = false;
    }
}
