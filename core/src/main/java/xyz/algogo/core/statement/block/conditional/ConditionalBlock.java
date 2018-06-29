package xyz.algogo.core.statement.block.conditional;

import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;

/**
 * Represents a conditional block.
 */

public abstract class ConditionalBlock extends BlockStatement {

	/**
	 * The condition.
	 */

	private Expression condition;

	/**
	 * Creates a new conditional block.
	 *
	 * @param condition The condition.
	 */

	public ConditionalBlock(final Expression condition) {
		this.condition = condition;
	}

	/**
	 * Creates a new conditional block.
	 *
	 * @param condition The condition.
	 * @param statements Children statements.
	 */

	public ConditionalBlock(final Expression condition, final Statement... statements) {
		super(statements);

		this.setCondition(condition);
	}

	/**
	 * Returns the condition.
	 *
	 * @return The condition.
	 */

	public final Expression getCondition() {
		return condition;
	}

	/**
	 * Sets the condition.
	 *
	 * @param expression The condition.
	 */

	public final void setCondition(final Expression expression) {
		this.condition = expression;
	}

	@Override
	public abstract ConditionalBlock copy();

}