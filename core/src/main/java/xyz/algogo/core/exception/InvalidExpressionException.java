package xyz.algogo.core.exception;

import xyz.algogo.core.evaluator.expression.Expression;

/**
 * Occurs when an invalid expression is submitted.
 */

public class InvalidExpressionException extends Exception {

	/**
	 * The expression.
	 */

	private final Expression expression;

	/**
	 * Creates a new invalid expression exception.
	 *
	 * @param expression The expression.
	 */

	public InvalidExpressionException(final Expression expression) {
		this.expression = expression;
	}

	/**
	 * Returns the expression.
	 *
	 * @return The expression.
	 */

	public Expression getExpression() {
		return expression;
	}

}