package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

/**
 * Creates a new parenthesis expression.
 */

public class ParenthesisExpression extends Expression {

	/**
	 * The inner parenthesized expression.
	 */

	private Expression expression;

	/**
	 * Creates a new parenthesis expression.
	 *
	 * @param expression The parenthesized expression.
	 */

	public ParenthesisExpression(final Expression expression) {
		this.expression = expression;
	}

	/**
	 * Returns the inner expression.
	 *
	 * @return The inner expression.
	 */

	public Expression getExpression() {
		return expression;
	}

	/**
	 * Sets the inner expression.
	 *
	 * @param expression The inner expression.
	 */

	public void setExpression(final Expression expression) {
		this.expression = expression;
	}

	@Override
	public Atom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		return expression.evaluate(evaluator, context);
	}

	@Override
	public ParenthesisExpression copy() {
		return new ParenthesisExpression(expression.copy());
	}

}