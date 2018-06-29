package xyz.algogo.core.statement.block;

import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a statement that accepts children.
 */

public abstract class BlockStatement extends Statement implements Serializable {

	/**
	 * Children statements.
	 */

	private final List<Statement> statements = new ArrayList<>();

	/**
	 * Creates a new block statement.
	 *
	 * @param statements Children statements.
	 */

	public BlockStatement(final Statement... statements) {
		addStatements(statements);
	}

	/**
	 * Gets the statement at the specified index.
	 *
	 * @param index The statement at the specified index.
	 *
	 * @return The statement located at the specified index.
	 */

	public Statement getStatement(final int index) {
		return statements.get(index);
	}

	/**
	 * Adds some children statement.
	 *
	 * @param statements Children statements.
	 */

	public void addStatements(final Statement... statements) {
		for(final Statement statement : statements) {
			addStatement(statement);
		}
	}

	/**
	 * Adds a child statement.
	 *
	 * @param statement Child statement.
	 */

	public void addStatement(final Statement statement) {
		if(!isValidChild(statement)) {
			return;
		}

		statements.add(statement);
	}

	/**
	 * Inserts a statement at the given index.
	 *
	 * @param statement The statement.
	 * @param index The index.
	 */

	public void insertStatement(final Statement statement, final int index) {
		if(!isValidChild(statement)) {
			return;
		}

		statements.add(index, statement);
	}

	/**
	 * Removes a statement.
	 *
	 * @param index Statement index.
	 */

	public void removeStatement(final int index) {
		statements.remove(index);
	}

	/**
	 * Removes a statement.
	 *
	 * @param statement The statement.
	 */

	public void removeStatement(final Statement statement) {
		statements.remove(statement);
	}

	/**
	 * Clears all children statements.
	 */

	public void clearStatements() {
		statements.clear();
	}

	/**
	 * Returns children count.
	 *
	 * @return Children count.
	 */

	public int getStatementCount() {
		return statements.size();
	}

	/**
	 * Returns children statements.
	 *
	 * @return Children statements.
	 */

	public Statement[] listStatements() {
		return statements.toArray(new Statement[statements.size()]);
	}

	/**
	 * Returns all children statements that match the specified ID.
	 *
	 * @param statementId The ID.
	 *
	 * @return All children statements that match the specified ID.
	 */

	public Statement[] listStatementsById(final int statementId) {
		final List<Statement> result = new ArrayList<>();
		for(final Statement statement : statements) {
			if(statement.getStatementId() != statementId) {
				continue;
			}

			result.add(statement);
		}

		return result.toArray(new Statement[result.size()]);
	}

	/**
	 * Returns whether the specified statement can be added as a child of the current block.
	 *
	 * @param statement The statement.
	 *
	 * @return Whether the specified statement can be added as a child of the current block.
	 */

	public boolean isValidChild(final Statement statement) {
		return isValidChild(statement.getStatementId());
	}

	/**
	 * Returns whether the specified statement can be added as a child of the current block.
	 *
	 * @param statementId The statement ID.
	 *
	 * @return Whether the specified statement can be added as a child of the current block.
	 */

	public boolean isValidChild(final int statementId) {
		return statementId != CreateVariableStatement.STATEMENT_ID;
	}

	/**
	 * Copies children statements.
	 *
	 * @return A copy of children statements.
	 */

	protected Statement[] copyStatements() {
		final List<Statement> result = new ArrayList<>();
		for(final Statement statement : statements) {
			result.add(statement.copy());
		}

		return result.toArray(new Statement[result.size()]);
	}

	@Override
	public Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		for(final Statement statement : statements) {
			final Exception ex = statement.evaluate(evaluator, context);
			if(ex != null || context.isStopped()) {
				return ex;
			}
		}

		return null;
	}

}