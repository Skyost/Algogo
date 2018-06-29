package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.language.Language;

import java.math.BigDecimal;

/**
 * Represents an unary minus expression.
 */

public class UnaryMinusExpression extends Expression {

	/**
	 * The held expression.
	 */

	private Expression expression;

	/**
	 * Creates a new unary minus expression.
	 *
	 * @param expression Expression to hold.
	 */

	public UnaryMinusExpression(final Expression expression) {
		this.expression = expression;
	}

	/**
	 * Gets the held expression.
	 *
	 * @return The held expression.
	 */

	public final Expression getExpression() {
		return expression;
	}

	/**
	 * Sets the expression to hold.
	 *
	 * @param expression Expression to hold.
	 */

	public final void setExpression(final Expression expression) {
		this.expression = expression;
	}

	@Override
	public final NumberAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom<?> atom = expression.evaluate(evaluator, context);

		if(!NumberAtom.hasNumberType(atom)) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(((BigDecimal)atom.getValue()).negate());
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateUnaryMinusExpression(this);
	}

	@Override
	public final UnaryMinusExpression copy() {
		return new UnaryMinusExpression(expression.copy());
	}

}