package com.wildbeeslabs.jentle.collections.common;

/**
 * Represents a range of integers, [min..max]. 
 * 
 * @specfield min: int
 * @specfield max: int
 * @invariant min <= max
 * @author Emina Torlak
 */
public abstract class IntRange {

	private IntRange() {	}
	
	/**
	 * Returns the left endpoint of this range.
	 * @return this.min
	 */
	public abstract int min();
	
	/**
	 * Returns the right endpoint of this range.
	 * @return this.max
	 */
	public abstract int max();

	/**
	 * Returns the number of element in this range.
	 * @return this.max - this.min + 1
	 */
	public int size() {
		return max() - min() + 1;
	}
	
	/**
	 * Returns true if the given integer is within 
	 * this range; otherwise returns false.
	 * @return i in [min..max]
	 */
	public boolean contains(int i) {
		return i >= min() && i <= max();
	}
	
	/**
	 * Returns true if this range contains the 
	 * given range.
	 * @return this.min <= range.min <= range.max <= this.max
	 * @throws NullPointerException  range = null
	 */
	public boolean contains(IntRange range) {
		return min() <= range.min() && range.max() <= max();
	}
	
	/**
	 * Returns true if this and the given range intersect.
	 * @return some i: int | this.contains(i) && range.contains(i)
	 * @throws NullPointerException  range = null
	 */
	public boolean intersects(IntRange range) {
		return contains(range.min()) || contains(range.max());
	}
	
	
	/**
	 * Returns true if o is an int range with the same endpoints as this.
	 * @return o in IntRange && o.min==this.min && o.max==this.max
	 */
	public boolean equals(Object o) {
		if (o instanceof IntRange) {
			final IntRange r = (IntRange) o;
			return min()==r.min() && max()==r.max();
		}
		return false;
	}
	
	/**
	 * Returns the hash code for this int range.  The implementation
	 * is guaranteed to obey the Object contract.
	 * @return the hashcode for this intrange
	 */
	public int hashCode() {
		return min()==max() ? min() : min() ^ max();
	}
	
	public String toString() { 
		return "[" + min() + ".." + max() + "]";
	}
	
	/**
	 * Represents an int range that consists of a single point. 
	 * 
	 * @invariant  min==max
	 * @author Emina Torlak
	 */
	static final class OnePointRange extends IntRange {
		private final int min;
		
		/**
		 * Constructs a new one point range.
		 */
		OnePointRange(int min) {
			this.min = min;
		}
		
		@Override
		public final int min() { return min; }
		
		@Override
		public final int max() { return min; }
	}
	
	/**
	 * Represents an int range with two distinct end points. 
	 * 
	 * @invariant  min < max
	 * @author Emina Torlak
	 */
	static final class TwoPointRange extends IntRange {
		private final int min, max;
		
		/**
		 * Constructs a new two point range.
		 * @requires  min < max
		 */
		TwoPointRange(int min, int max) {
			assert min < max;
			this.min = min;
			this.max = max;
		}
		
		@Override
		public final int min() { return min; }
		
		@Override
		public final int max() { return max; }
	}
	
	
}
