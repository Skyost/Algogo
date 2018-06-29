package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.language.Language;

import java.math.BigDecimal;

/**
 * Represents a multiplication expression.
 */

public class MultiplicationExpression extends LeftOpRightExpression {

	/**
	 * Creates a new multiplication expression.
	 *
	 * @param left The left expression.
	 * @param relation The relation.
	 * @param right The right expression.
	 */

	public MultiplicationExpression(final Expression left, final String relation, final Expression right) {
		super(left, relation, right);
	}

	@Override
	public NumberAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom left = this.getLeft().evaluate(evaluator, context);
		final Atom right = this.getRight().evaluate(evaluator, context);

		if(!NumberAtom.hasNumberType(left) || !NumberAtom.hasNumberType(right)) {
			return NumberAtom.ZERO;
		}

		final BigDecimal leftNumber = (BigDecimal)left.getValue();
		final BigDecimal rightNumber = (BigDecimal)right.getValue();

		switch(this.getOperator()) {
			case "*":
				return new NumberAtom(leftNumber.multiply(rightNumber, context.getMathContext()));
			case "/":
				return new NumberAtom(leftNumber.divide(rightNumber, context.getMathContext()));
			case "%":
				return new NumberAtom(leftNumber.remainder(rightNumber, context.getMathContext()));
			default:
				return NumberAtom.ZERO;
		}
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateMultiplicationExpression(this);
	}

	@Override
	public final MultiplicationExpression copy() {
		return new MultiplicationExpression(this.getLeft().copy(), this.getOperator(), this.getRight().copy());
	}

}