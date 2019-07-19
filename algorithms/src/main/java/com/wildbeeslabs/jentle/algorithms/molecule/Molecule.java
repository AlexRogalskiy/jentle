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
package com.wildbeeslabs.jentle.algorithms.molecule;

import java.util.Arrays;

/**
 * Represents a single "molecule" of data. Not a test set, not a parameter set, but the smallest "piece" of the scenario you want to test. It
 * is analagous to a "pair" in the pairwise terminology, but I didn't want to be limited to 2 "atoms". Also note that a molecule doesn't
 * represents the values themselves--it represents the indices of the one-dimensional ArrayUtils the Scenario uses to keep track of ALL atoms
 *
 * @author mmerrell
 */
public class Molecule {
    /**
     * Initializes a new Molecule, which has an ArrayUtils of indexes to the "larger" scenario
     *
     * @param atoms
     */
    public Molecule(int[] atoms) {
        this.setAtoms(atoms);
    }

    /**
     * Initialize a Molecule without the set of atomic indexes, but with at least an idea of how many atoms there will be per Molecule
     *
     * @param atomsPerMolecule
     */
    public Molecule(int atomsPerMolecule) {
        this.setAtomsPerMolecule(atomsPerMolecule);
    }

    private int atomsPerMolecule;

    public int getAtomsPerMolecule() {
        return atomsPerMolecule;
    }

    public void setAtomsPerMolecule(int atomsPerMolecule) {
        this.atomsPerMolecule = atomsPerMolecule;
    }

    private int[] atoms;

    public int[] getAtoms() {
        return atoms;
    }

    public void setAtoms(int[] atoms) {
        this.atoms = atoms;
        if (null != atoms) {
            this.setAtomsPerMolecule(atoms.length);
        }
    }

    private boolean used = false;

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    private boolean illegal = false;

    public boolean isIllegal() {
        return illegal;
    }

    public void setIllegal(boolean illegal) {
        this.illegal = illegal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Molecule that = (Molecule) o;

        if (atoms != null ? !Arrays.equals(atoms, that.getAtoms()) : that.getAtoms() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return atoms != null ? Arrays.hashCode(atoms) : 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(getAtoms());
    }
}
