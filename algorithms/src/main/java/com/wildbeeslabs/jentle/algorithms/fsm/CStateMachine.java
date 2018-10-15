/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.jentle.algorithms.fsm;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * Custom state machine implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <C>
 * @param <S>
 */
@Data
@EqualsAndHashCode
@ToString
public class CStateMachine<C, S extends ICState<C, ICTransition<C, S>>> implements ICStateMachine<C, S> {

    @Setter(AccessLevel.NONE)
    protected final S state;

    public CStateMachine(final S state) {
        this.state = state;
    }

    @Override
    public ICStateMachine<C, S> switchState(final C value) {
        return new CStateMachine<>((S) this.state.transit(value));
    }

    @Override
    public S getState() {
        return this.state;
    }

    @Override
    public boolean isTerminated() {
        return this.state.isFinal();
    }
}
