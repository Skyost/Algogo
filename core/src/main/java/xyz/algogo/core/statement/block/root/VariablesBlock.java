package xyz.algogo.core.statement.block.root;

import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;

/**
 * Represents the first top-level statement of the algorithm (where you declare your variables).
 */

public class VariablesBlock extends BlockStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 1;

	/**
	 * Creates a new variables block.
	 *
	 * @param statements Children statements.
	 */

	public VariablesBlock(final Statement... statements) {
		super(statements);
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final VariablesBlock copy() {
		return new VariablesBlock(copyStatements());
	}

	@Override
	public final boolean isValidChild(final int statementId) {
		return statementId == CreateVariableStatement.STATEMENT_ID || statementId == LineComment.STATEMENT_ID || statementId == BlockComment.STATEMENT_ID;
	}

}