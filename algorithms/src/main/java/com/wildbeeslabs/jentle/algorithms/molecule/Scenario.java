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

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Scenario {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private List<ParameterSet<?>> parameterSets = new ArrayList<ParameterSet<?>>();
    public List<ParameterSet<?>> getParameterSets() { return parameterSets; }
    public ParameterSet<?> getParameterSet(int index) {
        return getParameterSets().get(index);
    }

    private int[][] legalValues;

    /**
     * An ArrayUtils of arrays representing the parameter set (x), and the index of the flattened-out "parameterValues" ArrayUtils (y).
     * See documentation at the top of the PairwiseInventory class for details
     * @return
     */
    public int[][] getLegalValues() { return legalValues; }
    
    public void addParameterSet(ParameterSet<?> parameterSet) {
        parameterSets.add(parameterSet);
        int[] parameterValueIndexes = new int[ parameterSet.getParameterValues().size() ];

        //Rebuild the various metadata arrays each time (since we'll never know from here whether or not we're "done"--they can keep adding Parameter Sets)
        updateLegalValues(parameterSet, parameterValueIndexes);
        updateParameterValues(parameterSet);
        updateParameterPositions();
    }
    
    /**
     * A flattened ArrayUtils representing the values of all the parameters in the set
     */
    private List<?> parameterValues = new ArrayList();
    public List<?> getParameterValues() { return parameterValues; }

    protected void updateParameterValues(ParameterSet<?> parameterSet) {
        parameterValues.addAll((Collection)parameterSet.getParameterValues());
    }

    protected void updateLegalValues(ParameterSet<?> parameterSet, int[] parameterValueIndexes) {
        for (int i=0, j = getParameterValuesCount(); j < getParameterValuesCount() + parameterSet.getParameterValues().size(); i++, j++) {
            parameterValueIndexes[i] = j;
        }
        legalValues = ArrayUtils.addAll( legalValues, parameterValueIndexes );
    }

    /**
     * The total number of Parameter Sets under analysis. In a web form, this would be analogous to the number of fields
     * we're testing (possible values for User Type would be one Parameter Set, etc)
     * @return
     */
    public int getParameterSetCount() {
        return legalValues.length;
    }
    
    /**
     * The total number of values represented by all Parameter Sets under analysis
     * @return
     */
    public int getParameterValuesCount() {
        return parameterValues.size();
    }

    /**
     * A flattened ArrayUtils representing the parameter set to which this value belongs
     * @return An ArrayUtils of int, containing the indices of the parameter sets to which this value belongs
     */
    private int[] parameterPositions = null; // The parameter position for a given value
    public int[] getParameterPositions() { return this.parameterPositions; }

    /**
     * The parameterPositions field (int[]) represents the "parameter position" for each given value. See above for details
     */
    public void updateParameterPositions() {
        int[] parameterPositions = new int[ this.getParameterValuesCount() ]; // the indexes tell us which parameter set the value belongs to

        int k = 0; //The index of the parameter set attached to this value
        for ( int i = 0; i < this.getLegalValues().length; ++i ) {
            int[] curr = this.getLegalValues()[i];
            for (int aCurr : curr) {
                parameterPositions[k++] = i;
            }
        }
        log.debug("Parameter Positions: {}", Arrays.toString(parameterPositions));
        this.parameterPositions = parameterPositions;  
    }

    /**
     * Logs the current Parameter values contained in this Scenario
     */
    public void logParameterValues() {
        log.debug("Parameter Values: {}", getParameterValues().toString());
    }
}
