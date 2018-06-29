package xyz.algogo.desktop.utils;

import java.util.Stack;

/**
 * A stack class that has a fixed size.
 *
 * @param <T> Type of the stack.
 */

public class SizedStack<T> extends Stack<T> {

	/**
	 * The max size.
	 */

	private int maxSize;

	/**
	 * Creates a new sized stack.
	 *
	 * @param maxSize The max size.
	 */

	public SizedStack(final int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * Returns the max size of the stack.
	 *
	 * @return The max size of the stack.
	 */

	public final int getMaxSize() {
		return maxSize;
	}

	/**
	 * Sets the max size of the stack.
	 *
	 * @param maxSize The max size of the stack.
	 */

	public final void setMaxSize(final int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public final T push(final T object) {
		while(this.size() >= maxSize) {
			this.remove(0);
		}

		return super.push(object);
	}

}