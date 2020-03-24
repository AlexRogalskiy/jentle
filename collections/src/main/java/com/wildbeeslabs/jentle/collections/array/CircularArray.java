package com.wildbeeslabs.jentle.collections.array;

import org.broadinstitute.sting.utils.exceptions.ReviewedStingException;


/** This class, closely resembling a deque (except that it is not dynamically grown), 
 * provides an object with array-like interface and efficient 
 * implementation of shift operation. Use this class when some kind of sliding window is required: 
 * e.g. an array (window) is populated from some stream of data, and then the window is shifted. 
 * If most of the data in the window remains the same so that only a few old elements sjould be popped from
 * and a few new elements pushed onto the array, both re-populating the whole array from the data and
 * shifting a regular array would be grossly inefficient. Instead, shiftData(int N) method of circular array
 * efficiently pops out N first elements and makes last N elements available.
 * 
 * Consider an example of reading a character stream A,B,C,D,....,Z into an array with requirement of keeping
 * last 5 letters. First, we would read first 5 letters same way as we would with a regular array:<br><br>
 * 
 * <code>
 *  CircularArray a(5);<br>
 *  for ( int i = 0; i < 5; i++ ) a.set(i, readChar());<br>
 * </code>
 *  <br>
 *  and then on the arrival of each next character we shift the array:<br><br>
 * 
 * <code>
 *  a.shiftData(1); a.set(4, readChar() );<br>
 * </code>
 *  <br> 
 * After the lines from the above example are executed, the array will <i>logically</i> look as:<br>
 * 
 *   B,C,D,E,F,<br><br>
 *   
 * e.g. as if we had a regular array, shifted it one element down and added new element on the top.
 * 
 *  
 * @author asivache
 *
 */
public class CircularArray <T> {
	

	private Object[] data ;
	private int offset;
	
	/** Creates an array of fixed length */
	public CircularArray(int length) {
		if ( length <= 0 ) throw new ReviewedStingException("CircularArray length must be positive. Passed: "+length);
		data = new Object[length]; 
		offset = 0;
	}

	/** Returns length of the array */
	public int length() {
		return data.length;
	}
	
	/** Gets i-th element of the array 
	 * 
	 * @throws IndexOutOfBoundsException if value of i is illegal
	 */
	@SuppressWarnings("unchecked")
	public T get(int i) {
		if ( i < 0 || i >= data.length ) 
				throw new IndexOutOfBoundsException("Length of CircularArray: "+data.length+"; element requested: "+i);
		return (T)(data [ ( offset + i ) % data.length ]);
	}
	
	/** Sets i-th element of the array to the specified value. 
	 * 
	 * @throws IndexOutOfBoundsException if value of i is illegal
	 */
	public void set(int i, T value) {
		if ( i < 0 || i >= data.length ) 
			throw new IndexOutOfBoundsException("Length of CircularArray: "+data.length+"; set element request at: "+i);
		data [ ( offset + i ) % data.length ] = value;
	}
		
	/** Set all elements to null.
	 * 
	 */
	public void clear() {
		for ( int i = 0 ; i < data.length ; i++ ) data[i] = null;
		offset = 0;
	}
	
	/** Efficient shift-down of the array data. After this operation, array.get(0), array.get(1), etc will
	 * be returning what array.get(shift), array.get(shift+1),... were returning before the shift was performed,
	 * and last shift elements of the array will be reset to 0.
	 * @param shift
	 */
	public void shiftData(int shift) {
		if ( shift >= data.length ) {
			// if we shift by more than the length of stored data, we lose
			// all that data completely, so we just re-initialize the array.
			// This is not the operating mode CircularArray is intended for 
			// but we can handle it, just in case.
			for ( int i = 0 ; i < data.length ; i++ ) data[i] = null;
			offset = 0; 
			return;
		}
		
		// shift < data.length, so at least some data should be preserved
		
		final int newOffset = ( offset+shift ) % data.length;
		if ( newOffset < offset ) {
			// wrap-around!
			for ( int i = offset ; i < data.length ; i++ ) data[i] = null;
			for ( int i = 0; i < newOffset ; i++ ) data[i] = null;
		} else {
			for ( int i = offset ; i < newOffset ; i++ ) data[i] = null;
		}
		offset = newOffset;
	}

	
	
	/** Implements primitive int type-based circular array. See CircularArray for details.
	 * 
	 * @author asivache
	 *
	 */
	public static class Int {
		private int [] data ;
		private int offset;
		
		/** Creates an array of fixed length */
		public Int(int length) {
			if ( length <= 0 ) throw new ReviewedStingException("CircularArray length must be positive. Passed: "+length);
			data = new int[length]; // automaticaly initialized to zeros
			offset = 0;
		}
		
		/** Returns length of the array */
		public int length() {
			return data.length;
		}
		
		/** Gets i-th element of the array 
		 * 
		 * @throws IndexOutOfBoundsException if value of i is illegal
		 */
		public int get(int i) {
			if ( i < 0 || i >= data.length ) 
					throw new IndexOutOfBoundsException("Length of CircularArray: "+data.length+"; element requested: "+i);
			return data [ ( offset + i ) % data.length ];
		}
		
		/** Sets i-th element of the array to the specified value. 
		 * 
		 * @throws IndexOutOfBoundsException if value of i is illegal
		 */
		public void set(int i, int value) {
			if ( i < 0 || i >= data.length ) 
				throw new IndexOutOfBoundsException("Length of CircularArray: "+data.length+"; set element request at: "+i);
			data [ ( offset + i ) % data.length ] = value;
		}
		
		/** Increments i-th element of the array by the specified value (value can be negative). 
		 * 
		 * @throws IndexOutOfBoundsException if i is illegal
		 */
		public void increment(int i, int value) {
			if ( i < 0 || i >= data.length ) 
				throw new IndexOutOfBoundsException("Length of CircularArray: "+data.length+"; increment element request at: "+i);
			data [ ( offset + i ) % data.length ] += value;
		}
		
		/** Set all elements to 0.
		 * 
		 */
		public void clear() {
			for ( int i = 0 ; i < data.length ; i++ ) data[i] = 0;
			offset = 0;
		}
		
		/** Efficient shift-down of the array data. After this operation, array.get(0), array.get(1), etc will
		 * be returning what array.get(shift), array.get(shift+1),... were returning before the shift was performed,
		 * and last shift elements of the array will be reset to 0.
		 * @param shift
		 */
		public void shiftData(int shift) {
			if ( shift >= data.length ) {
				// if we shift by more than the length of stored data, we lose
				// all that data completely, so we just re-initialize the array.
				// This is not the operating mode CircularArray is intended for 
				// but we can handle it, just in case.
				for ( int i = 0 ; i < data.length ; i++ ) data[i] = 0;
				offset = 0; 
				return;
			}
			
			// shift < data.length, so at least some data should be preserved
			
			final int newOffset = ( offset+shift ) % data.length;
			if ( newOffset < offset ) {
				// wrap-around!
				for ( int i = offset ; i < data.length ; i++ ) data[i] = 0;
				for ( int i = 0; i < newOffset ; i++ ) data[i] = 0;
			} else {
				for ( int i = offset ; i < newOffset ; i++ ) data[i] = 0;
			}
			offset = newOffset;
		}
	}

}

====================================
import java.util.ArrayList;
import java.util.Collection;

public class ExpandingArrayList<E> extends ArrayList<E> {
    public ExpandingArrayList() { super(); }
    public ExpandingArrayList(Collection<? extends E> c) { super(c); }
    public ExpandingArrayList(int initialCapacity) { super(initialCapacity); }

    /**
     * Returns the element at the specified position in this list.  If index > size,
     * returns null.  Otherwise tries to access the array
     * @param index
     * @return
     * @throws IndexOutOfBoundsException in index < 0
     */
    public E get(int index) throws IndexOutOfBoundsException {
        if ( index < size() )
            return super.get(index);
        else
            return null;
    }

    public E expandingGet(int index, E default_value) throws IndexOutOfBoundsException {
        maybeExpand(index, default_value);
        return super.get(index);
    }

    private void maybeExpand(int index, E value) {
        if ( index >= size() ) {
            // We need to add null items until we can safely set index to element
            for ( int i = size(); i <= index; i++ )
                add(value);
        }
    }


    public E set(int index, E element) {
        maybeExpand(index, null);
        return super.set(index, element);
    }
}
