package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.function.other.AbsFunction;

/**
 * Creates a new absolute value expression.
 */

public class AbsoluteValueExpression extends ParenthesisExpression {

	/**
	 * Creates a new absolute value expression.
	 *
	 * @param expression The absolute value expression.
	 */

	public AbsoluteValueExpression(final Expression expression) {
		super(expression);
	}

	@Override
	public NumberAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom atom = super.evaluate(evaluator, context);
		if(!NumberAtom.hasNumberType(atom)) {
			return NumberAtom.ZERO;
		}

		return AbsFunction.abs(atom);
	}

	@Override
	public AbsoluteValueExpression copy() {
		return new AbsoluteValueExpression(this.getExpression().copy());
	}

}