package xyz.algogo.core.evaluator.atom;

import xyz.algogo.core.language.Language;

import java.math.BigDecimal;

/**
 * Represents a boolean atom.
 */

public class BooleanAtom extends NumberAtom {

	/**
	 * A boolean atom that is holding the <b>true</b> value.
	 */

	public static final BooleanAtom TRUE = new BooleanAtom(true);

	/**
	 * A boolean atom that is holding the <b>false</b> value.
	 */

	public static final BooleanAtom FALSE = new BooleanAtom(false);

	/**
	 * Creates a new boolean atom.
	 *
	 * @param value The boolean value.
	 */

	public BooleanAtom(final boolean value) {
		super(value ? BigDecimal.ONE : BigDecimal.ZERO);
	}

	/**
	 * Returns the boolean value of this atom.
	 *
	 * @return The boolean value of this atom.
	 */

	public final boolean getBooleanValue() {
		return this.getValue().equals(BigDecimal.ONE);
	}

	/**
	 * Sets the boolean value of this atom.
	 *
	 * @param value The boolean value of this atom.
	 */

	public final void setBooleanValue(final boolean value) {
		super.setValue(value ? BigDecimal.ONE : BigDecimal.ZERO);
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateBooleanAtom(this);
	}

	@Override
	public final BooleanAtom copy() {
		return new BooleanAtom(getBooleanValue());
	}

}