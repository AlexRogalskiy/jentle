package com.wildbeeslabs.jentle.collections.array;

import org.apache.log4j.Logger;
import org.broadinstitute.sting.utils.exceptions.ReviewedStingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ebanks
 * Date: July 1, 2012
 */

public class NestedIntegerArray<T> {

    private static Logger logger = Logger.getLogger(NestedIntegerArray.class);

    protected final Object[] data;

    protected final int numDimensions;
    protected final int[] dimensions;

    // Preallocate the first two dimensions to limit contention during tree traversals in put()
    private static final int NUM_DIMENSIONS_TO_PREALLOCATE = 2;

    public NestedIntegerArray(final int... dimensions) {
        numDimensions = dimensions.length;
        if ( numDimensions == 0 )
            throw new ReviewedStingException("There must be at least one dimension to an NestedIntegerArray");
        this.dimensions = dimensions.clone();

        int dimensionsToPreallocate = Math.min(dimensions.length, NUM_DIMENSIONS_TO_PREALLOCATE);

        logger.info(String.format("Creating NestedIntegerArray with dimensions %s", Arrays.toString(dimensions)));
        logger.info(String.format("Pre-allocating first %d dimensions", dimensionsToPreallocate));

        data = new Object[dimensions[0]];
        preallocateArray(data, 0, dimensionsToPreallocate);

        logger.info(String.format("Done pre-allocating first %d dimensions", dimensionsToPreallocate));
    }

    /**
     * Recursively allocate the first dimensionsToPreallocate dimensions of the tree
     *
     * Pre-allocating the first few dimensions helps limit contention during tree traversals in put()
     *
     * @param subarray current node in the tree
     * @param dimension current level in the tree
     * @param dimensionsToPreallocate preallocate only this many dimensions (starting from the first)
     */
    private void preallocateArray( Object[] subarray, int dimension, int dimensionsToPreallocate ) {
        if ( dimension >= dimensionsToPreallocate - 1 ) {
            return;
        }

        for ( int i = 0; i < subarray.length; i++ ) {
            subarray[i] = new Object[dimensions[dimension + 1]];
            preallocateArray((Object[])subarray[i], dimension + 1, dimensionsToPreallocate);
        }
    }

    public T get(final int... keys) {
        final int numNestedDimensions = numDimensions - 1;
        Object[] myData = data;

        for( int i = 0; i < numNestedDimensions; i++ ) {
            if ( keys[i] >= dimensions[i] )
                return null;

            myData = (Object[])myData[keys[i]];
            if ( myData == null )
                return null;
        }

        return (T)myData[keys[numNestedDimensions]];
    }

    /**
     * Insert a value at the position specified by the given keys.
     *
     * This method is thread-safe, however the caller MUST check the
     * return value to see if the put succeeded. This method RETURNS FALSE if
     * the value could not be inserted because there already was a value present
     * at the specified location. In this case the caller should do a get() to get
     * the already-existing value and (potentially) update it.
     *
     * @param value value to insert
     * @param keys keys specifying the location of the value in the tree
     * @return true if the value was inserted, false if it could not be inserted because there was already
     *         a value at the specified position
     */
    public boolean put(final T value, final int... keys) { // WARNING! value comes before the keys!
        if ( keys.length != numDimensions )
            throw new ReviewedStingException("Exactly " + numDimensions + " keys should be passed to this NestedIntegerArray but " + keys.length + " were provided");

        final int numNestedDimensions = numDimensions - 1;
        Object[] myData = data;
        for ( int i = 0; i < numNestedDimensions; i++ ) {
            if ( keys[i] >= dimensions[i] )
                throw new ReviewedStingException("Key " + keys[i] + " is too large for dimension " + i + " (max is " + (dimensions[i]-1) + ")");

            // If we're at or beyond the last dimension that was pre-allocated, we need to do a synchronized
            // check to see if the next branch exists, and if it doesn't, create it
            if ( i >= NUM_DIMENSIONS_TO_PREALLOCATE - 1 ) {
                synchronized ( myData ) {
                    if ( myData[keys[i]] == null ) {
                        myData[keys[i]] = new Object[dimensions[i + 1]];
                    }
                }
            }

            myData = (Object[])myData[keys[i]];
        }

        synchronized ( myData ) {   // lock the bottom row while we examine and (potentially) update it

            // Insert the new value only if there still isn't any existing value in this position
            if ( myData[keys[numNestedDimensions]] == null ) {
                myData[keys[numNestedDimensions]] = value;
            }
            else {
                // Already have a value for this leaf (perhaps another thread came along and inserted one
                // while we traversed the tree), so return false to notify the caller that we didn't put
                // the item
                return false;
            }
        }

        return true;
    }

    public List<T> getAllValues() {
        final List<T> result = new ArrayList<T>();
        fillAllValues(data, result);
        return result;
    }

    private void fillAllValues(final Object[] array, final List<T> result) {
        for ( Object value : array ) {
            if ( value == null )
                continue;
            if ( value instanceof Object[] )
                fillAllValues((Object[])value, result);
            else
                result.add((T)value);
        }
    }

    public static class Leaf {
        public final int[] keys;
        public final Object value;

        public Leaf(final int[] keys, final Object value) {
            this.keys = keys;
            this.value = value;
        }
    }

    public List<Leaf> getAllLeaves() {
        final List<Leaf> result = new ArrayList<Leaf>();
        fillAllLeaves(data, new int[0], result);
        return result;
    }

    private void fillAllLeaves(final Object[] array, final int[] path, final List<Leaf> result) {
        for ( int key = 0; key < array.length; key++ ) {
            final Object value = array[key];
            if ( value == null )
                continue;
            final int[] newPath = appendToPath(path, key);
            if ( value instanceof Object[] ) {
                fillAllLeaves((Object[]) value, newPath, result);
            } else {
                result.add(new Leaf(newPath, value));
            }
        }
    }

    private int[] appendToPath(final int[] path, final int newKey) {
        final int[] newPath = new int[path.length + 1];
        for ( int i = 0; i < path.length; i++ )
            newPath[i] = path[i];
        newPath[path.length] = newKey;
        return newPath;
    }
}
