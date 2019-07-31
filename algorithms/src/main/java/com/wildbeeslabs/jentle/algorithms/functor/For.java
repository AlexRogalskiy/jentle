package com.wildbeeslabs.jentle.algorithms.functor;

public class For {

	public void firstToLimit(int first, int limit, IndexedTask task) {
		for (int i = first; i <= limit; i++) {
			task.execute(i);
		}
	}
	
	public void firstToLimit(int limit, IndexedTask task) {
		for (int i = 0; i <= limit; i++) {
			task.execute(i);
		}
	}

}
