package xyz.algogo.core.statement.block.root;

import xyz.algogo.core.language.Language;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;

/**
 * Represents the beginning of the algorithm. Must be placed after a variables block.
 */

public class BeginningBlock extends BlockStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 2;

	/**
	 * Creates a new beginning block.
	 *
	 * @param statements Children statements.
	 */

	public BeginningBlock(final Statement... statements) {
		super(statements);
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateBeginningBlock(this);
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final BeginningBlock copy() {
		return new BeginningBlock(copyStatements());
	}

}