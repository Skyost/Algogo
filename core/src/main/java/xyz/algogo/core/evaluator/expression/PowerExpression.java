package xyz.algogo.core.evaluator.expression;

import java.math.BigDecimal;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

/**
 * Represents a power expression.
 */

public class PowerExpression extends Expression {

	/**
	 * The base.
	 */

	private Expression base;

	/**
	 * The exponent.
	 */

	private Expression exponent;

	/**
	 * Creates a new power expression.
	 *
	 * @param base The base.
	 * @param exponent The exponent.
	 */

	public PowerExpression(final Expression base, final Expression exponent) {
		this.base = base;
		this.exponent = exponent;
	}

	/**
	 * Returns the base.
	 *
	 * @return The base.
	 */

	public Expression getBase() {
		return base;
	}

	/**
	 * Sets the base.
	 *
	 * @param base The base.
	 */

	public void setBase(final Expression base) {
		this.base = base;
	}

	/**
	 * Returns the exponent.
	 *
	 * @return The exponent.
	 */

	public Expression getExponent() {
		return exponent;
	}

	/**
	 * Sets the exponent.
	 *
	 * @param exponent The exponent.
	 */

	public void setExponent(final Expression exponent) {
		this.exponent = exponent;
	}

	@Override
	public NumberAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom base = this.base.evaluate(evaluator, context);
		final Atom exponent = this.exponent.evaluate(evaluator, context);

		if(!NumberAtom.hasNumberType(base) || !NumberAtom.hasNumberType(exponent)) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.pow((BigDecimal)base.getValue(), (BigDecimal)exponent.getValue(), context.getMathContext()));
	}

	@Override
	public PowerExpression copy() {
		return new PowerExpression(base.copy(), exponent.copy());
	}

}