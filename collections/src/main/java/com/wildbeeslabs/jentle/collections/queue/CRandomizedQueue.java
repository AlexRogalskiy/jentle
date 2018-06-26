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

package com.wildbeeslabs.jentle.collections.queue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * Custom randomized queue implementation
 *
 * @author Alex
 * @version 1.0.0
 * @since 2017-08-07
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CRandomizedQueue<T> extends ACQueue<T> {
	
	private Item[] queue;
	private int pointer = 0;
	private int size = 0;
	private int arraySize = 2;
    
	private class QueueIterator implements Iterator<Item>
	{	
		private Item[] iteratorQueue;
		private int iteratorPointer;
		
		
		public QueueIterator()
		{
			iteratorPointer = size - 1;
			iteratorQueue = queue;
		}
		
		public Item next()
		{
			if (!hasNext()) {
				throw new NoSuchElementException("The queue is empty");
			}
			
			int randomPointer = iteratorPointer;
			
			if (randomPointer > 0) {
				randomPointer = StdRandom.uniform(iteratorPointer);
			}
			
			Item item = iteratorQueue[randomPointer];
			iteratorPointer--;
			
			if(randomPointer != iteratorPointer) {
				iteratorQueue[randomPointer] = iteratorQueue[iteratorPointer];
			}
			
			return item;
		}
		
		public boolean hasNext()
		{
			return pointer > -1;
		}
		
		public void remove()
		{
			throw new UnsupportedOperationException("Remove operation is not supported");
		}
	}
	
    public RandomizedQueue()
    {
    	queue = (Item[]) new Object[arraySize];
    }
    
    public boolean isEmpty()
    {
    	return size == 0;
    }
    
    public int size()
    {
    	return size;
    }
    
    public void enqueue(Item item)
    {
 	    if (item == null) {
		    throw new IllegalArgumentException("Item cannot be null");
	    }

 	    if (pointer >= arraySize) {
 	    	resizeArray((size + 1) * 2);
 	    }
 	    
 	    queue[pointer] = item;
 	    pointer++;
    	size++;
    }
    
    public Item dequeue()
    {
 	    if (isEmpty()) {
		    throw new NoSuchElementException("The queue is empty");
	    }
 	    
 	    int randomNumber = 0;
 	   
 	    if (size > 1) {
 		   randomNumber = StdRandom.uniform(size - 1);
 	    }

 	    Item returnItem = queue[randomNumber];
 	   
 	    pointer--;
 	    
 	    if (size != pointer) {
 	    	queue[randomNumber] = queue[pointer];
 	    }
    	
    	if (size <= arraySize / 4) {
    		resizeArray(arraySize / 2);
    	}
    	
    	size--;

    	return returnItem;
    }
    
    private void resizeArray(int newArraySize)
    {    	
    	arraySize = newArraySize;
    	Item[] tempArray = (Item[]) new Object[arraySize];
    	int tempPointer = 0;
    	
    	for (int i = 0; i < size; i++) {
			tempArray[i] = queue[i];
			tempPointer++;
    	}
    	
    	queue = tempArray;
    	pointer = tempPointer;
    }
    
    public Item sample()
    {
 	    if (isEmpty()) {
		    throw new NoSuchElementException("The queue is empty");
	    }
 	    
 	    int randomNumber = 0;
  	   
 	    if (size > 1) {
 		   randomNumber = StdRandom.uniform(size - 1);
 	    }
 	    
	    return queue[randomNumber];
    }
    
    public Iterator<Item> iterator()
    {
    	return new QueueIterator();
    }
}