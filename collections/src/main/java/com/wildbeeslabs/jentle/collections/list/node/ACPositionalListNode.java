package com.wildbeeslabs.jentle.collections.list.node;

import com.wildbeeslabs.jentle.collections.exception.InvalidPositionException;
import com.wildbeeslabs.jentle.collections.interfaces.service.Position;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

/**
 * Custom abstract {@link Position} list node implementation
 *
 * @param <T>
 * @param <E>
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ACPositionalListNode<T, E extends ACPositionalListNode<T, E>> extends ACListNodeExtended<T, E> implements Position<T> {

    @Override
    public T element() throws InvalidPositionException {
        if (Objects.isNull(this.getPrevious()) && Objects.isNull(this.getNext())) {
            throw new InvalidPositionException("ERROR: position is not valid");
        }
        return this.getData();
    }
}
