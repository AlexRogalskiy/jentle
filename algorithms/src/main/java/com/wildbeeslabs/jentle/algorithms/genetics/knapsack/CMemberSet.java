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
package com.wildbeeslabs.jentle.algorithms.genetics.knapsack;

import com.wildbeeslabs.jentle.collections.utils.CUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * Custom member set implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode
@ToString
public class CMemberSet<T extends CMember> {

    /**
     * Default member set
     */
    @Setter(AccessLevel.NONE)
    protected final List<T> members;

    public CMemberSet(int initialSize, boolean isNew) {
        assert (initialSize > 0);
        members = new ArrayList<>(initialSize);
        if (isNew) {
            createNewPopulation(initialSize);
        }
    }

    protected T getFittest() {
        T fittest = this.members.get(0);
        for (int i = 0; i < this.members.size(); i++) {
            if (fittest.getFitness() <= this.getMember(i).getFitness()) {
                fittest = getMember(i);
            }
        }
        return fittest;
    }

    private void createNewPopulation(int size) {
        final Class<? extends T> clazz = (Class<? extends T>) this.members.getClass();
        for (int i = 0; i < size; i++) {
            final T person = CUtils.getInstance(clazz);
            this.members.add(i, person);
        }
    }

    public T getMember(int index) {
        assert (index > 0);
        if (Objects.nonNull(this.members) && index < this.members.size()) {
            return this.members.get(index);
        }
        return null;
    }

    public void setMember(int index, final T value) {
        assert (index > 0);
        if (Objects.nonNull(this.members) && index < this.members.size()) {
            this.members.set(index, value);
        }
    }

    public void setMembers(final Collection<T> members) {
        this.members.clear();
        if (Objects.nonNull(members)) {
            this.members.addAll(members);
        }
    }

    public void addMember(final T member) {
        if (Objects.nonNull(member)) {
            this.members.add(member);
        }
    }

    public void removeMember(final T member) {
        if (Objects.nonNull(member)) {
            this.members.remove(member);
        }
    }
}
