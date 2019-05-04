package com.wildbeeslabs.jentle.collections.tree.iface;

import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.PositionalCollection;
import lombok.NonNull;

import java.util.Objects;

/**
 * Custom tree {@link PositionalCollection} interface declaration
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
public interface IPositionalTree<T, R extends TreePosition<T, R>> extends PositionalCollection<T>, ITreeCollection<T, R> {

    /**
     * Returns root position node {@code S}
     *
     * @return root position {@code S}
     */
    <S extends R> S root();

    /**
     * Returns {@link PositionIterator} of children {@code S} nodes by input {@code S} position node
     *
     * @param <S>
     * @param position - initial input {@code S} position node
     * @return {@link PositionIterator} of children {@code S} nodes
     */
    <S extends R> @NonNull PositionIterator<R> children(final S position);

    /**
     * Returns parent node {@code S} of input {@code S} position
     *
     * @param <S>
     * @param position - initial input position node {@code S}
     * @return parent position node {@code S}
     */
    default <S extends R> S getParent(final S position) {
        Objects.requireNonNull(position);
        return (S) position.getParent();
    }

    /**
     * Checks if current position node {@code S} has left child
     *
     * @param node - current position node {@code S}
     * @return true - if current node {@code S} has left child node, false - otherwise
     */
    default <S extends R> boolean hasLeftChild(final S position) {
        Objects.requireNonNull(position);
        return (Objects.nonNull(position.getLeft()));
    }

    /**
     * Checks if current position node {@code S} has right child
     *
     * @param node - current position node {@code S}
     * @return true - if current node {@code S} has right child node {@code S}, false - otherwise
     */
    default <S extends R> boolean hasRightChild(final S position) {
        Objects.requireNonNull(position);
        return (Objects.nonNull(position.getRight()));
    }

    /**
     * Checks if current node is internal position node {@code S}
     *
     * @param node - current position node {@code S}
     * @return true - if current position node is internal, false - otherwise
     */
    default <S extends R> boolean isInternal(final S position) {
        Objects.requireNonNull(position);
        return (Objects.nonNull(position.getLeft()) || Objects.nonNull(position.getRight()));
    }

    /**
     * Checks if current node is external position node {@code S}
     *
     * @param node - current position node {@code S}
     * @return true - if current position node {@code S} is external, false - otherwise
     */
    default <S extends R> boolean isExternal(final S position) {
        Objects.requireNonNull(position);
        return (Objects.isNull(position.getLeft()) && Objects.isNull(position.getRight()));
    }

    /**
     * Returns left child node {@code S} of the current position node {@code S}
     *
     * @param node - current position node {@code S}
     * @return left child position node {@code S}
     */
    default <S extends R> S getLeftChild(final S position) {
        if (this.hasLeftChild(position)) {
            return (S) position.getLeft();
        }
        return null;
    }

    /**
     * Returns right child node {@code S} of the current position node {@code S}
     *
     * @param node - current position node {@code S}
     * @return right child position node {@code S}
     */
    default <S extends R> S getRightChild(final S position) {
        if (this.hasRightChild(position)) {
            return (S) position.getRight();
        }
        return null;
    }

    /**
     * Returns binary flag by input position node {@code S}
     *
     * @param <S>
     * @param position - initial input position node {@code S}
     * @return true - if current position node {@code S} is root, false - otherwise
     */
    default <S extends R> boolean isRoot(final S position) {
        Objects.requireNonNull(position);
        return Objects.isNull(position.getParent());
    }
}
