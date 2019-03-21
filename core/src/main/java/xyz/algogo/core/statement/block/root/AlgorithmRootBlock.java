package xyz.algogo.core.statement.block.root;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;

/**
 * Represents the very very top level statement of the algorithm.
 */

public class AlgorithmRootBlock extends BlockStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 0;

	/**
	 * Creates a new algorithm root block.
	 *
	 * @param statements Children statements.
	 */

	public AlgorithmRootBlock(final Statement... statements) {
		super(statements);
	}

	@Override
	public int getStatementId() {
		return STATEMENT_ID;
	}

	/**
	 * Returns the variables block.
	 *
	 * @return The variables block.
	 */

	public VariablesBlock getVariablesBlock() {
		return (VariablesBlock)this.listStatementsById(VariablesBlock.STATEMENT_ID)[0];
	}

	/**
	 * Returns the beginning block.
	 *
	 * @return The beginning block.
	 */

	public BeginningBlock getBeginningBlock() {
		return (BeginningBlock)this.listStatementsById(BeginningBlock.STATEMENT_ID)[0];
	}

	@Override
	public Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		try {
			return super.evaluate(evaluator, context);
		}
		catch(final Exception ex) {
			return ex;
		}
	}

	@Override
	public AlgorithmRootBlock copy() {
		return new AlgorithmRootBlock(this.listStatements());
	}

	@Override
	public boolean isValidChild(final int statementId) {
		return statementId == VariablesBlock.STATEMENT_ID || statementId == BeginningBlock.STATEMENT_ID || statementId == EndBlock.STATEMENT_ID || statementId == LineComment.STATEMENT_ID || statementId == BlockComment.STATEMENT_ID;
	}

}