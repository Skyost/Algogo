package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

/**
 * Represents a relational expression.
 */

public class RelationalExpression extends LeftOpRightExpression {

	/**
	 * Creates a new relational expression.
	 *
	 * @param left The left expression.
	 * @param relation The relation (greater, less, greater or equal, less or equal).
	 * @param right The right expression.
	 */

	public RelationalExpression(final Expression left, final String relation, final Expression right) {
		super(left, relation, right);
	}

	@Override
	public BooleanAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom<?> left = this.getLeft().evaluate(evaluator, context);
		final Atom<?> right = this.getRight().evaluate(evaluator, context);
		final int comparison = left.compareTo(right);

		switch(this.getOperator()) {
			case "<=":
				return new BooleanAtom(comparison <= 0);
			case ">=":
				return new BooleanAtom(comparison >= 0);
			case "<":
				return new BooleanAtom(comparison < 0);
			case ">":
				return new BooleanAtom(comparison > 0);
			default:
				return BooleanAtom.FALSE;
		}
	}

	@Override
	public RelationalExpression copy() {
		return new RelationalExpression(this.getLeft().copy(), this.getOperator(), this.getRight().copy());
	}

}