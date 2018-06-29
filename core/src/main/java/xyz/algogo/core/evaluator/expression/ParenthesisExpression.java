package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.language.Language;

/**
 * Creates a new parenthesis expression.
 */

public class ParenthesisExpression extends Expression {

	/**
	 * The parenthesized expression.
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
	 * Returns the parenthesized expression.
	 *
	 * @return The parenthesized expression.
	 */

	public final Expression getExpression() {
		return expression;
	}

	/**
	 * Sets the parenthesized expression.
	 *
	 * @param expression The parenthesized expression.
	 */

	public final void setExpression(final Expression expression) {
		this.expression = expression;
	}

	@Override
	public final Atom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		return expression.evaluate(evaluator, context);
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateParenthesisExpression(this);
	}

	@Override
	public final ParenthesisExpression copy() {
		return new ParenthesisExpression(expression.copy());
	}

}