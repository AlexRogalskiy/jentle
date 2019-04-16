package com.wildbeeslabs.jentle.collections.list.impl;

import com.wildbeeslabs.jentle.collections.array.iface.IMutableVector;
import com.wildbeeslabs.jentle.collections.exception.BoundaryViolationException;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.list.iface.ISequentialList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

/**
 * Custom mutable {@link ISequentialList} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CMutableSequentialList<T> extends CMutablePositionalList<T> implements ISequentialList<T, CMutablePositionalList.CListNode<T>>, IMutableVector<T> {

    protected void checkRank(int rank) throws BoundaryViolationException {
        if (rank < 0 || rank >= this.size()) {
            throw new BoundaryViolationException(String.format("ERROR: cannot process rank={%s} for list with size={%s}", rank, this.size()));
        }
    }

    @Override
    public Position<T> atRank(int rank) {
        CListNode<T> node;
        checkRank(rank);
        if (rank <= this.size() / 2) {
            node = this.getHeader().getNext();
            for (int i = 0; i < rank; i++) {
                node = node.getNext();
            }
        } else {
            node = this.getTrailer().getPrevious();
            for (int i = 1; i < this.size() - rank; i++) {
                node = node.getPrevious();
            }
        }
        return node;
    }

    @Override
    public T elemAtRank(int rank) {
        final CListNode<T> node = (CListNode<T>) this.atRank(rank);
        return node.getData();
    }

    @Override
    public int rankOf(final Position<T> position) {
        final CListNode<T> node = checkPosition(position);
        CListNode<T> temp = this.getHeader().getNext();
        int rank = 0;
        while (Objects.nonNull(temp) && !Objects.equals(temp, this.getTrailer())) {
            if (Objects.equals(temp, node)) {
                return rank;
            }
            temp = temp.getNext();
            rank++;
        }
        return -1;
    }

    @Override
    public void insertAtRank(int rank, final T value) throws BoundaryViolationException {
        if (rank == this.size()) {
            this.insertLast(value);
        } else {
            checkRank(rank);
            this.insertBefore(this.atRank(rank), value);
        }
    }

    @Override
    public T removeAtRank(int rank) throws BoundaryViolationException {
        checkRank(rank);
        return this.remove(this.atRank(rank));
    }

    @Override
    public T replaceAtRank(int rank, final T value) throws BoundaryViolationException {
        checkRank(rank);
        return this.replace(this.atRank(rank), value);
    }
}
