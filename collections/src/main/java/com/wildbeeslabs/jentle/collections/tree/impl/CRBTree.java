package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.dictionary.Dictionary;
import com.wildbeeslabs.jentle.collections.model.CRBTNode;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;

/**
 * Custom red-black {@link CBinarySearchTree} implementation
 *
 * @param <K>
 * @param <V>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CRBTree<K, V> extends CBinarySearchTree<K, V, CRBTNode<K, V>> implements Dictionary<K, V> {

    public CRBTree(final Comparator<? super K> comparator) {
        super(comparator);
        //this.binaryTree = new ResctructurableNodeBinaryTree<>();
    }
}
