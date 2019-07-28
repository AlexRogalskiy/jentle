package com.wildbeeslabs.jentle.algorithms.enums;

/**
 * 
 * The visibility of a entity.
 * 
 * @author Gian Perrone
 * 
 */
public enum Visibility {

	/**
	 * Package visibility, toString() returns null.
	 */
	PACKAGE(null),
	/**
	 * Private visibility, toString() returns "private".
	 */
	PRIVATE("private"),
	/**
	 * Protected visibility, toString() returns "protected".
	 */
	PROTECTED("protected"),
	/**
	 * Public visibility, toString() returns "public".
	 */
	PUBLIC("public");

	private String str;

	private Visibility(String str) {

		this.str = str;

	}

	/**
	 * Returns a string description of the visibility. It is formatted like
	 * a visibility modifier in a declaration.
	 * 
	 * @return the description
	 */
	@Override
	public String toString() {

		return str;

	}

}
