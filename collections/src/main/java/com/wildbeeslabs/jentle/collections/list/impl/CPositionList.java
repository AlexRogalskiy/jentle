package com.wildbeeslabs.jentle.collections.list.impl;

import com.wildbeeslabs.jentle.collections.exception.BoundaryViolationException;
import com.wildbeeslabs.jentle.collections.exception.EmptyContainerException;
import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.iface.IPosition;
import com.wildbeeslabs.jentle.collections.list.iface.IPositionList;
import com.wildbeeslabs.jentle.collections.list.node.ACPositionalListNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Custom {@link IPositionList} implementation
 *
 * @param <T>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CPositionList<T> extends ACList<T, CPositionList.CListNode<T>> implements IPositionList<T, CPositionList.CListNode<T>> {

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

    public CPositionList() {
        this.size = 0;
        this.header = new CListNode<>();
        this.trailer = new CListNode<>();
        this.header.setNext(this.trailer);
    }

    protected CListNode<T> checkPosition(final IPosition<T> node) throws InvalidPositionException {
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
    public T replace(final IPosition<T> position, final T value) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        final T data = node.getData();
        node.setData(value);
        return data;
    }

    @Override
    public void swap(final IPosition<T> positionFirst, final IPosition<T> positionLast) throws InvalidPositionException {
        final CListNode<T> nodeFirst = checkPosition(positionFirst);
        final CListNode<T> nodeLast = checkPosition(positionLast);
        final T temp = nodeFirst.getData();
        nodeFirst.setData(nodeLast.getData());
        nodeLast.setData(temp);
    }

    @Override
    public T remove(final IPosition<T> position) throws InvalidPositionException {
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
    public IPosition<T> insertLast(final T value) {
        final CListNode<T> newNode = new CListNode<>(value, this.trailer.getPrevious(), this.trailer);
        this.trailer.getPrevious().setNext(newNode);
        this.trailer.setPrevious(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public IPosition<T> insertFirst(final T value) {
        final CListNode<T> newNode = new CListNode<>(value, this.header, this.header.getNext());
        this.header.getNext().setPrevious(newNode);
        this.header.setNext(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public IPosition<T> insertAfter(final IPosition<T> position, final T value) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> newNode = new CListNode<T>(value, node, node.getNext());
        node.getNext().setPrevious(newNode);
        node.setNext(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public IPosition<T> insertBefore(final IPosition<T> position, T value) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> newNode = new CListNode<T>(value, node.getPrevious(), node);
        node.getPrevious().setNext(newNode);
        node.setPrevious(newNode);
        this.size++;
        return newNode;
    }

    @Override
    public IPosition<T> after(final IPosition<T> position) throws InvalidPositionException, BoundaryViolationException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> nextNode = node.getNext();
        if (nextNode == this.trailer) {
            throw new BoundaryViolationException("ERROR: cannot process position at the trail of the list");
        }
        return nextNode;
    }

    @Override
    public IPosition<T> before(final IPosition<T> position) throws InvalidPositionException, BoundaryViolationException {
        final CListNode<T> node = checkPosition(position);
        final CListNode<T> prevNode = node.getPrevious();
        if (prevNode == this.header) {
            throw new BoundaryViolationException("ERROR: cannot process position at the head of the list");
        }
        return prevNode;
    }

    @Override
    public IPosition<T> last() throws EmptyContainerException {
        if (this.isEmpty()) {
            throw new EmptyContainerException("ERROR: empty container");
        }
        return this.trailer.getPrevious();
    }

    @Override
    public IPosition<T> first() throws EmptyContainerException {
        if (this.isEmpty()) {
            throw new EmptyContainerException("ERROR: empty container");
        }
        return this.header.getNext();
    }

    @Override
    public boolean isLast(final IPosition<T> position) throws InvalidPositionException {
        final CListNode<T> node = checkPosition(position);
        return node.getNext() == this.trailer;
    }

    @Override
    public boolean isFirst(final IPosition<T> position) throws InvalidPositionException {
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
