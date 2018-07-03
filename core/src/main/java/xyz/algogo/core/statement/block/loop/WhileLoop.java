package xyz.algogo.core.statement.block.loop;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.conditional.ConditionalBlock;

import java.math.BigDecimal;

/**
 * Represents a WHILE loop.
 */

public class WhileLoop extends ConditionalBlock {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 12;

	/**
	 * Creates a new WHILE loop.
	 *
	 * @param condition The condition.
	 * @param statements Children statements.
	 */

	public WhileLoop(final Expression condition, final Statement... statements) {
		super(condition, statements);
	}

	@Override
	public final Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Expression expression = this.getCondition();

		Atom result = expression.evaluate(evaluator, context);
		while(BigDecimal.ONE.equals(result.getValue()) && !context.isStopped()) {
			final Exception exception = super.evaluate(evaluator, context);
			if(exception != null) {
				return exception;
			}

			result = expression.evaluate(evaluator, context);
		}

		return null;
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final WhileLoop copy() {
		return new WhileLoop(this.getCondition().copy(), copyStatements());
	}

}