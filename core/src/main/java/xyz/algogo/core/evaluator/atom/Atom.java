package xyz.algogo.core.evaluator.atom;

import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.language.Translatable;

import java.io.Serializable;

/**
 * Represents a simple atomic element.
 *
 * @param <T> Type of the current atom.
 */

public abstract class Atom<T extends Comparable<T>> implements Comparable<Atom>, Serializable, Translatable {

	/**
	 * The currently held value.
	 */

	private T value;

	/**
	 * Creates a new atomic element.
	 *
	 * @param value The held value.
	 */

	public Atom(final T value) {
		this.value = value;
	}

	/**
	 * Returns the currently held value of this atom.
	 *
	 * @return The currently held value.
	 */

	public T getValue() {
		return value;
	}

	/**
	 * Sets the new atom value.
	 *
	 * @param value The new atom value.
	 */

	public void setValue(final T value) {
		this.value = value;
	}

	/**
	 * Evaluates the current atom value.
	 *
	 * @param evaluator The expression evaluator.
	 *
	 * @return The evaluated value of this atom.
	 */

	public Atom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		return this;
	}

	/**
	 * Checks if a given atom has the same type as the current one.
	 *
	 * @param atom The given atom.
	 *
	 * @return Whether the given atom has the same type as the current one.
	 */

	public abstract boolean hasSameType(final Atom atom);

	/**
	 * Copies this atom instance.
	 *
	 * @return A copy of this atom instance.
	 */

	public abstract Atom<T> copy();

	@Override
	public final int compareTo(final Atom atom) {
		if(!hasSameType(atom)) {
			return -1;
		}

		return value.compareTo((T)atom.value);
	}

	@Override
	public final boolean equals(final Object object) {
		if(!(object instanceof Atom) || !hasSameType((Atom)object)) {
			return super.equals(object);
		}

		return compareTo((Atom)object) == 0;
	}

}