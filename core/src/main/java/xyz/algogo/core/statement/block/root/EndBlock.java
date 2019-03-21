package xyz.algogo.core.statement.block.root;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;

/**
 * Represents the end of the algorithm. It is not necessary in itself, it is more of a "structure" statement.
 */

public class EndBlock extends BlockStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 3;

	@Override
	public Statement getStatement(final int index) {
		return null;
	}

	@Override
	public void addStatement(final Statement statement) {}

	@Override
	public void insertStatement(final Statement statement, final int index) {}

	@Override
	public int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public EndBlock copy() {
		return new EndBlock();
	}

	@Override
	public Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		return null;
	}

	@Override
	public boolean isValidChild(final int statementId) {
		return false;
	}

}