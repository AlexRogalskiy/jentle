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
package com.wildbeeslabs.jentle.algorithms.hillclimbing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * Custom state model
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CState {

    @Setter(AccessLevel.NONE)
    private List<Stack<String>> state;
    private int heuristics;

    public CState(final List<Stack<String>> state) {
        this.state = state;
    }

    public CState(final List<Stack<String>> state, int heuristics) {
        this.state = state;
        this.heuristics = heuristics;
    }

    public CState(final CState state) {
        if (Objects.nonNull(state)) {
            this.state = new ArrayList<>();
            state.getState().stream().map((s) -> {
                return (Stack) s.clone();
            }).forEach((s1) -> {
                this.state.add(s1);
            });
            this.heuristics = state.getHeuristics();
        }
    }
}
