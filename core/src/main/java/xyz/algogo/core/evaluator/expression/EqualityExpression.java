package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.language.Language;

/**
 * Represents an equality expression.
 */

public class EqualityExpression extends RelationalExpression {

	/**
	 * Creates a new equality expression.
	 *
	 * @param left The left expression.
	 * @param relation The relation (equals or not equals).
	 * @param right The right expression.
	 */

	public EqualityExpression(final Expression left, final String relation, final Expression right) {
		super(left, relation, right);
	}

	@Override
	public final BooleanAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom left = this.getLeft().evaluate(evaluator, context);
		final Atom right = this.getRight().evaluate(evaluator, context);

		switch(this.getOperator()) {
			case "==":
				return new BooleanAtom(left.equals(right));
			case "!=":
				return new BooleanAtom(!left.equals(right));
			default:
				return BooleanAtom.FALSE;
		}
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateEqualityExpression(this);
	}

	@Override
	public final EqualityExpression copy() {
		return new EqualityExpression(this.getLeft().copy(), this.getOperator(), this.getRight().copy());
	}

}