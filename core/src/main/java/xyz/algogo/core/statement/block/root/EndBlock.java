package xyz.algogo.core.statement.block.root;

import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.language.Language;
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
	public final Statement getStatement(final int index) {
		return null;
	}

	@Override
	public final void addStatement(final Statement statement) {}

	@Override
	public final void insertStatement(final Statement statement, final int index) {}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateEndBlock(this);
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final EndBlock copy() {
		return new EndBlock();
	}

	@Override
	public final Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		return null;
	}

	@Override
	public final boolean isValidChild(final int statementId) {
		return false;
	}

}