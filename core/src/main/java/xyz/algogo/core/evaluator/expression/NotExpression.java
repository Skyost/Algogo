package xyz.algogo.core.evaluator.expression;

import java.math.BigDecimal;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

/**
 * Represents a NOT expression.
 */

public class NotExpression extends Expression {

	/**
	 * The expression to negate.
	 */

	private Expression expression;

	/**
	 * Creates a new NOT expression.
	 *
	 * @param expression The expression to negate.
	 */

	public NotExpression(final Expression expression) {
		this.expression = expression;
	}

	/**
	 * Returns the expression to negate.
	 *
	 * @return The expression to negate.
	 */

	public Expression getExpression() {
		return expression;
	}

	/**
	 * Sets the expression to negate.
	 *
	 * @param expression The expression to negate.
	 */

	public void setExpression(final Expression expression) {
		this.expression = expression;
	}

	@Override
	public BooleanAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom atom = expression.evaluate(evaluator, context);
		return new BooleanAtom(atom.getValue().equals(BigDecimal.ZERO));
	}

	@Override
	public NotExpression copy() {
		return new NotExpression(expression.copy());
	}

}