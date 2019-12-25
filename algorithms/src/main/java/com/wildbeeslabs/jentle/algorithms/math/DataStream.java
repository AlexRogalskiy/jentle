package com.wildbeeslabs.jentle.algorithms.math;

/**
 * A statistically representative reservoir of a data stream.
 */
public interface DataStream {
	/**
	 * Returns the number of values recorded.
	 *
	 * @return the number of values recorded
	 */
	int size();

	/**
	 * Adds a new recorded value to the reservoir.
	 *
	 * @param value a new recorded value
	 */
	void update(final double value);

	/**
	 * Returns only the double values of the reservoir (not the timestamps)
	 *
	 * @return the double values of the treemap
	 */
	double[] getValues();
}
