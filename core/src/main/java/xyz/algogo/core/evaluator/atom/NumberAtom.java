package xyz.algogo.core.evaluator.atom;

import java.math.BigDecimal;

/**
 * Represents a number atom that holds a BigDecimal value.
 */

public class NumberAtom extends Atom<BigDecimal> {

	/**
	 * A number atom that is holding the <b>BigDecimal.ZERO</b> value.
	 */

	public static final NumberAtom ZERO = new NumberAtom(BigDecimal.ZERO);

	/**
	 * Creates a new number atom.
	 *
	 * @param value An int value.
	 */

	public NumberAtom(final int value) {
		this(new BigDecimal(String.valueOf(value)));
	}

	/**
	 * Creates a new number atom.
	 *
	 * @param value A double value.
	 */

	public NumberAtom(final double value) {
		this(new BigDecimal(String.valueOf(value)));
	}

	/**
	 * Creates a new number atom.
	 *
	 * @param value The BigDecimal value.
	 */

	public NumberAtom(final BigDecimal value) {
		super(value);
	}

	@Override
	public final boolean hasSameType(final Atom atom) {
		return hasNumberType(atom);
	}

	@Override
	public NumberAtom copy() {
		return new NumberAtom(new BigDecimal(this.getValue().toString()));
	}

	/**
	 * Checks whether the given atom has a number type.
	 * <br>In fact, we check if the value of the provided atom is an instance of BigDecimal.
	 *
	 * @param atom The given atom.
	 *
	 * @return Whether the given atom has a number type.
	 */

	public static boolean hasNumberType(final Atom atom) {
		return atom != null && atom.getValue() instanceof BigDecimal;
	}

}