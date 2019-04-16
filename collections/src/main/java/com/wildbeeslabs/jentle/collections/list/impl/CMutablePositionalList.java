package com.wildbeeslabs.jentle.collections.list.impl;

import com.wildbeeslabs.jentle.collections.exception.BoundaryViolationException;
import com.wildbeeslabs.jentle.collections.exception.EmptyContainerException;
import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.iterator.PositionIterator;
import com.wildbeeslabs.jentle.collections.iface.position.Position;
import com.wildbeeslabs.jentle.collections.list.iface.IMutablePositionalList;
import com.wildbeeslabs.jentle.collections.list.iface.IPositionalList;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Custom {@link IPositionalList} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CMutablePositionalList<T> extends ACList<T, CMutablePositionalList.CListNode<T>> implements IMutablePositionalList<T, CMutablePositionalList.CListNode<T>> {

    /**
     * Default list size
     */
    private int size;
    /**
     * Default {@link CListNode} header
     */
    private CListNode<T> header;
    /**
     * Default {@link CListNode} trailer
     */
    private CListNode<T> trailer;

    @Override
    public @NonNull <S extends Position<T>> PositionIterator<S> positionIterator() {
        throw new UnsupportedOperationException("ERROR: operation is not supported");
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class CListNode<T> extends ACPositionalListNode<T, CListNode<T>> {

        public CListNode() {
            this(null);
        }

        public CListNode(final T data) {
            this(data, null, null);
        }

        public CListNode(final T data, final CListNode<T> previous, final CListNode<T> next) {
            super(data, next, previous);
        }
    }

    public CMutablePositionalList() {
        this.size = 0;
        this.header = new CListNode<>();
        this.trailer = new CListNode<>();
        this.header.setNext(this.trailer);
    }

    protected CListNode<T> checkPosition(final Position<T> node) throws InvalidPositionException {
        if (Objects.isNull(node)) {
            throw new InvalidPositionException("ERROR: cannot process null position");
        }
        if (Objects.equals(node, this.header)) {
            throw new InvalidPositionException("ERROR: invalid header position");
        }
        if (Objects.equals(node, this.trailer)) {
            throw new InvalidPositionException("ERROR: invalid trailer position");
        }
        try {
            final CListNode<T> temp = (CListNode<T>) node;
            if (Objects.isNull(temp.getPrevious()) || Objects.isNull(temp.getNext())) {
                throw new InvalidPositionException("ERROR: invalid position");
            }
            return temp;
        } catch (ClassCastException ex) {
            throw new InvalidPositionException(String.format("ERROR: invalid position type={%s}", node));
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return (0 == this.size());
    }

    @Override
    public T replace(final Position<T> position, final T value) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        final T data = node.getData();
        node.setData(value);
        return data;
    }

    @Override
    public void swap(final Position<T> positionFirst, final Position<T> positionLast) throws InvalidPositionException {
        final CListNode<T> nodeFirst = checkPosition(positionFirst);
        final CListNode<T> nodeLast = checkPosition(positionLast);
        final T temp = nodeFirst.getData();
        nodeFirst.setData(nodeLast.getData());
        nodeLast.setData(temp);
    }

    @Override
    public T remove(final Position<T> position) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> nodePrev = node.getPrevious();
        final CListNode<T> nodeNext = node.getNext();
        nodePrev.setNext(nodeNext);
        nodeNext.setPrevious(nodePrev);
        final T data = node.getData();
        node.setPrevious(null);
        this.size--;
        return data;
    }

    @Override
    public Position<T> insertLast(final T value) {
        final CListNode<T> newNode = new CListNode<>(value, this.trailer.getPrevious(), this.trailer);
        this.trailer.getPrevious().setNext(newNode);
        this.trailer.setPrevious(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public Position<T> insertFirst(final T value) {
        final CListNode<T> newNode = new CListNode<>(value, this.header, this.header.getNext());
        this.header.getNext().setPrevious(newNode);
        this.header.setNext(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public Position<T> insertAfter(final Position<T> position, final T value) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> newNode = new CListNode<T>(value, node, node.getNext());
        node.getNext().setPrevious(newNode);
        node.setNext(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public Position<T> insertBefore(final Position<T> position, T value) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> newNode = new CListNode<T>(value, node.getPrevious(), node);
        node.getPrevious().setNext(newNode);
        node.setPrevious(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public Position<T> after(final Position<T> position) throws InvalidPositionException, BoundaryViolationException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> nextNode = node.getNext();
        if (nextNode == this.trailer) {
            throw new BoundaryViolationException("ERROR: cannot process position at the trail of the list");
        }
        return nextNode;
    }

    @Override
    public Position<T> before(final Position<T> position) throws InvalidPositionException, BoundaryViolationException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> prevNode = node.getPrevious();
        if (prevNode == this.header) {
            throw new BoundaryViolationException("ERROR: cannot process position at the head of the list");
        }
        return prevNode;
    }

    @Override
    public Position<T> last() throws EmptyContainerException {
        if (this.isEmpty()) {
            throw new EmptyContainerException("ERROR: empty container");
        }
        return this.trailer.getPrevious();
    }

    @Override
    public Position<T> first() throws EmptyContainerException {
        if (this.isEmpty()) {
            throw new EmptyContainerException("ERROR: empty container");
        }
        return this.header.getNext();
    }

    @Override
    public boolean isLast(final Position<T> position) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        return node.getNext() == this.trailer;
    }

    @Override
    public boolean isFirst(final Position<T> position) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        return node.getPrevious() == this.header;
    }

    @Override
    protected CListNode<T> createNode(final Optional<? extends T> value) {
        if (value.isPresent()) {
            return new CListNode<>(value.get());
        }
        return new CListNode<>();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
