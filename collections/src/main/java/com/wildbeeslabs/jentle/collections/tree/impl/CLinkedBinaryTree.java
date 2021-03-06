package com.wildbeeslabs.jentle.collections.tree.impl;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.node.PositionTreeNode;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.tree.iface.tree.binary.position.IPositionalBinaryTreeContainer;
import com.wildbeeslabs.jentle.collections.tree.node.CPositionalTreeNode;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Custom {@link IPositionalBinaryTreeContainer} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@EqualsAndHashCode
@ToString
public class CLinkedBinaryTree<T> implements IPositionalBinaryTreeContainer<T, CPositionalTreeNode<T>> {

    private PositionTreeNode<T, CPositionalTreeNode<T>> root;
    private int size;

    public CLinkedBinaryTree() {
        this.root = new CPositionalTreeNode<>();
        this.size = 1;
    }

//    @Override
//    public PositionTreeNode<T> getSibling(final PositionTreeNode<T> position) {
//        final PositionTreeNode<T> parent = this.parent(position);
//        final PositionTreeNode<T> leftChild = this.getLeft(parent);
//        if (Objects.equals(position, leftChild)) {
//            return this.getRight(parent);
//        }
//        return leftChild;
//    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public @NonNull PositionIterator<PositionTreeNode<T, CPositionalTreeNode<T>>> positionIterator() {
        final Position<T>[] positions = new Position[this.size()];
//        this.inOrderPositions(this.root(), positions, 0);
//        return new ArrayPositionIterator(positions);
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public <S extends Position<T>> void swap(final S positionFirst, final S positionLast) {
        final T temp = positionLast.element();
        positionLast.setElement(positionFirst.element());
        positionFirst.setElement(temp);
    }

    @Override
    public <S extends Position<T>> T replace(final S position, final T value) {
        final T temp = position.element();
        position.setElement(value);
        return temp;
    }

    public void expandExternal(final CPositionalTreeNode<T> position) {
        if (this.isExternal(position)) {
            position.setLeft(new CPositionalTreeNode<>(null, null, null, position));
            position.setRight(new CPositionalTreeNode<>(null, null, null, position));
            this.size += 2;
        }
    }

    public void removeAboveExternal(final CPositionalTreeNode<T> position) {
        if (this.isExternal(position)) {
            final CPositionalTreeNode<T> parent = this.getParent(position);
            final CPositionalTreeNode<T> sibling = this.getSibling(position);
            if (this.isRoot(parent)) {
                sibling.setParent(null);
                this.root = sibling;
            } else {
                final CPositionalTreeNode<T> node = this.getParent(parent);
                if (Objects.equals(parent, this.getLeft(node))) {
                    node.setLeft(sibling);
                } else {
                    node.setRight(sibling);
                }
                sibling.setParent(node);
            }
            this.size -= 2;
        }
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getLeft(final S value) {
        return (S) value.getLeft();
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getRight(final S value) {
        return (S) value.getRight();
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getSibling(final S value) {
        return (S) value.getParent();
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S root() {
        return (S) this.root;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getParent(final S position) {
        return (S) position.getParent();
    }

    @Override
    public @NonNull <S extends CPositionalTreeNode<T>> PositionIterator<CPositionalTreeNode<T>> children(final S position) {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> boolean isInternal(final S position) {
        return Objects.nonNull(position) && Objects.nonNull(position.getRight());
    }

    @Override
    public <S extends CPositionalTreeNode<T>> boolean isExternal(final S position) {
        return Objects.isNull(position.getLeft()) && Objects.isNull(position.getRight());
    }

    @Override
    public void setRoot(final Optional<? extends T> value) {
    }

    @Override
    public <S extends CPositionalTreeNode<T>> S getRoot() {
        return null;
    }

    @Override
    public <S extends CPositionalTreeNode<T>> boolean isRoot(final S position) {
        return Objects.equals(position, this.root());
    }
}
