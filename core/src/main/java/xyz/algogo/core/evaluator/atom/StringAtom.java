package xyz.algogo.core.evaluator.atom;

import xyz.algogo.core.language.Language;

/**
 * Represents a string atom.
 */

public class StringAtom extends Atom<String> {

	/**
	 * Creates a new string atom.
	 *
	 * @param value The string value.
	 */

	public StringAtom(final String value) {
		super(value);
	}

	@Override
	public final boolean hasSameType(final Atom atom) {
		return hasStringType(atom);
	}

	@Override
	public String toLanguage(final Language language) {
		return language.translateStringAtom(this);
	}

	@Override
	public StringAtom copy() {
		return new StringAtom(this.getValue());
	}

	/**
	 * Checks whether the given atom has a string type.
	 * <br>In fact, we check if the value of the provided atom is an instance of String.
	 *
	 * @param atom The given atom.
	 *
	 * @return Whether the given atom has a string type.
	 */

	public static boolean hasStringType(final Atom atom) {
		return atom.getValue() instanceof String;
	}

}