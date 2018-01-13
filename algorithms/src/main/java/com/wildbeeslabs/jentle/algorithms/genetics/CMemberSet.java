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
package com.wildbeeslabs.jentle.algorithms.genetics;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Data
@EqualsAndHashCode(callSuper = false)
public class CMemberSet<T extends CMember> {

    /**
     * Default Logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(this.getClass());

    private List<T> population;

    public CMemberSet(int size, boolean isNew) {
        assert (size > 0);
        this.population = new ArrayList<>(size);
        if (isNew) {
            createNewPopulation(size);
        }
    }

    protected T getMember(int index) {
        return this.population.get(index);
    }

    protected T getFittest() {
        T fittest = this.population.get(0);
        for (int i = 0; i < this.population.size(); i++) {
            if (fittest.getFitness() <= this.getMember(i).getFitness()) {
                fittest = getMember(i);
            }
        }
        return fittest;
    }

    private void createNewPopulation(int size) {
        final Class<? extends T> clazz = (Class<? extends T>) this.population.getClass();
        for (int i = 0; i < size; i++) {
            final T person = this.createNewMember(clazz);
            this.population.add(i, person);
        }
    }

    private T createNewMember(final Class<? extends T> clazz) {
        try {
            return (T) clazz.getComponentType().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.error("ERROR: cannot initialize member, message=" + ex.getMessage());
        }
        return null;
    }
}
