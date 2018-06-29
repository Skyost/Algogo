package xyz.algogo.core.statement.block.conditional;

import xyz.algogo.core.language.Language;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;

/**
 * Represents an ELSE block.
 */

public class ElseBlock extends BlockStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 10;

	/**
	 * Creates a new ELSE block.
	 *
	 * @param statements Children statements.
	 */

	public ElseBlock(final Statement... statements) {
		super(statements);
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateElseBlock(this);
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final ElseBlock copy() {
		return new ElseBlock(this.copyStatements());
	}

}