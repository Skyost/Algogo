package xyz.algogo.desktop.utils;

import java.util.Stack;

public class SizedStack<E> extends Stack<E> {

	private static final long serialVersionUID = 1L;
	
	private int maxSize;
	
	public SizedStack(final int maxSize) {
		this.maxSize = maxSize;
	}
	
	public final int getMaxSize() {
		return maxSize;
	}
	
	public final void setMaxSize(final int maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public final E push(final E object) {
		while(this.size() >= maxSize) {
            this.remove(0);
        }
        return super.push(object);
	}
	
}