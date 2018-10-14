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
package com.wildbeeslas.jentle.algorithms.sudoku;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom column node implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CColumnNode extends CDancingNode {

    protected int size;
    protected String name;

    @SuppressWarnings("LeakingThisInConstructor")
    CColumnNode(final String name) {
        super();
        this.size = 0;
        this.name = name;
        this.C = this;
    }

    void cover() {
        unlinkLR();
        for (CDancingNode i = this.D; i != this; i = i.D) {
            for (CDancingNode j = i.R; j != i; j = j.R) {
                j.unlinkUD();
                j.C.size--;
            }
        }
    }

    void uncover() {
        for (CDancingNode i = this.U; i != this; i = i.U) {
            for (CDancingNode j = i.L; j != i; j = j.L) {
                j.C.size++;
                j.relinkUD();
            }
        }
        relinkLR();
    }
}
