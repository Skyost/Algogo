package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.language.Language;

import java.math.BigDecimal;

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

	public final Expression getExpression() {
		return expression;
	}

	/**
	 * Sets the expression to negate.
	 *
	 * @param expression The expression to negate.
	 */

	public final void setExpression(final Expression expression) {
		this.expression = expression;
	}

	@Override
	public final BooleanAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom atom = expression.evaluate(evaluator, context);
		return new BooleanAtom(atom.getValue().equals(BigDecimal.ZERO));
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateNotExpression(this);
	}

	@Override
	public final NotExpression copy() {
		return new NotExpression(expression.copy());
	}

}