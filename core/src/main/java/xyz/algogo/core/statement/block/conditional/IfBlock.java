package xyz.algogo.core.statement.block.conditional;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.exception.InvalidExpressionException;
import xyz.algogo.core.language.Language;
import xyz.algogo.core.statement.Statement;

import java.math.BigDecimal;

/**
 * Represents an IF/ELSE block.
 */

public class IfBlock extends ConditionalBlock {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 9;

	/**
	 * The ELSE block.
	 */

	private ElseBlock elseBlock;

	/**
	 * Creates a new IF block.
	 *
	 * @param condition The condition.
	 */

	public IfBlock(final Expression condition) {
		this(condition, null);
	}

	/**
	 * Creates a new IF block.
	 *
	 * @param condition The condition.
	 * @param elseBlock The ELSE block.
	 */

	public IfBlock(final Expression condition, final ElseBlock elseBlock) {
		this(condition, elseBlock, new Statement[0]);
	}

	/**
	 * Creates a new IF block.
	 *
	 * @param condition The condition.
	 * @param elseBlock The ELSE block.
	 * @param statements Children statements.
	 */

	public IfBlock(final Expression condition, final ElseBlock elseBlock, final Statement... statements) {
		super(condition, statements);

		this.elseBlock = elseBlock;
	}

	/**
	 * Returns the ELSE block.
	 *
	 * @return The ELSE block.
	 */

	public final ElseBlock getElseBlock() {
		return elseBlock;
	}

	/**
	 * Sets the ELSE block.
	 *
	 * @param elseBlock The ELSE block.
	 */

	public final void setElseBlock(final ElseBlock elseBlock) {
		this.elseBlock = elseBlock;
	}

	/**
	 * Checks if this IF block holds an ELSE block.
	 *
	 * @return Whether this IF block holds an ELSE block.
	 */

	public final boolean hasElseBlock() {
		return elseBlock != null;
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateIfBlock(this);
	}

	@Override
	public final Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom result = this.getCondition().evaluate(evaluator, context);
		if(result == null) {
			return new InvalidExpressionException(this.getCondition());
		}

		if(result.getValue().equals(BigDecimal.ONE)) {
			return super.evaluate(evaluator, context);
		}

		if(elseBlock != null) {
			return elseBlock.evaluate(evaluator, context);
		}

		return null;
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final IfBlock copy() {
		return new IfBlock(this.getCondition().copy(), elseBlock == null ? null : elseBlock.copy(), this.copyStatements());
	}

}
