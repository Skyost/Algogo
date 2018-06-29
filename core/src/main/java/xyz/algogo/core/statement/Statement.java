package xyz.algogo.core.statement;

import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.language.Translatable;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;
import xyz.algogo.core.statement.block.root.BeginningBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.core.statement.block.root.VariablesBlock;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.io.PrintStatement;
import xyz.algogo.core.statement.simple.io.PrintVariableStatement;
import xyz.algogo.core.statement.simple.io.PromptStatement;
import xyz.algogo.core.statement.simple.variable.AssignStatement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;

/**
 * Represents a statement which is a basic algorithm instruction.
 */

public abstract class Statement implements Translatable {

	/**
	 * Returns the unique statement ID of the current statement.
	 * 
	 * @return Unique statement ID.
	 */

	public abstract int getStatementId();

	/**
	 * Evaluates the current statement.
	 * 
	 * @param evaluator The expression evaluator.
	 * @param context The evaluation context.
	 * 
	 * @return Whether an exception occurs.
	 */

	public abstract Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context);

	/**
	 * Copies the current statement.
	 * 
	 * @return A copy of the current statement.
	 */

	public abstract Statement copy();

	/**
	 * Calls a function according to the provided statement.
	 * 
	 * @param statement The statement.
	 * @param statementType The statement type interface.
	 */

	public static void getStatementType(final Statement statement, final StatementTypeInterface statementType) {
		switch(statement.getStatementId()) {
			case AlgorithmRootBlock.STATEMENT_ID:
				statementType.isAlgorithmRootBlock((AlgorithmRootBlock)statement);
				break;
			case VariablesBlock.STATEMENT_ID:
				statementType.isVariablesBlock((VariablesBlock)statement);
				break;
			case BeginningBlock.STATEMENT_ID:
				statementType.isBeginningBlock((BeginningBlock) statement);
				break;
			case EndBlock.STATEMENT_ID:
				statementType.isEndBlock((EndBlock)statement);
				break;
			case CreateVariableStatement.STATEMENT_ID:
				statementType.isCreateVariableStatement((CreateVariableStatement)statement);
				break;
			case AssignStatement.STATEMENT_ID:
				statementType.isAssignStatement((AssignStatement)statement);
				break;
			case PromptStatement.STATEMENT_ID:
				statementType.isPromptStatement((PromptStatement)statement);
				break;
			case PrintVariableStatement.STATEMENT_ID:
				statementType.isPrintVariableStatement((PrintVariableStatement)statement);
				break;
			case PrintStatement.STATEMENT_ID:
				statementType.isPrintStatement((PrintStatement)statement);
				break;
			case IfBlock.STATEMENT_ID:
				statementType.isIfBlock((IfBlock)statement);
				break;
			case ElseBlock.STATEMENT_ID:
				statementType.isElseBlock((ElseBlock)statement);
				break;
			case ForLoop.STATEMENT_ID:
				statementType.isForLoop((ForLoop)statement);
				break;
			case WhileLoop.STATEMENT_ID:
				statementType.isWhileLoop((WhileLoop)statement);
				break;
			case LineComment.STATEMENT_ID:
				statementType.isLineComment((LineComment)statement);
				break;
			case BlockComment.STATEMENT_ID:
				statementType.isBlockComment((BlockComment)statement);
				break;
			default:
				statementType.isUnknownStatement(statement);
				break;
		}
	}

	/**
	 * Statement type interface.
	 */

	public interface StatementTypeInterface {

		/**
		 * Called if the submitted statement is an algorithm root block.
		 *
		 * @param statement The algorithm root block.
		 */

		void isAlgorithmRootBlock(final AlgorithmRootBlock statement);

		/**
		 * Called if the submitted statement is a variables block.
		 * 
		 * @param statement The variables block.
		 */

		void isVariablesBlock(final VariablesBlock statement);

		/**
		 * Called if the submitted statement is a beginning block.
		 *
		 * @param statement The beginning block.
		 */
		
		void isBeginningBlock(final BeginningBlock statement);

		/**
		 * Called if the submitted statement is an end block.
		 *
		 * @param statement The end block.
		 */
		
		void isEndBlock(final EndBlock statement);

		/**
		 * Called if the submitted statement is a create variable statement.
		 *
		 * @param statement The create variable statement.
		 */

		void isCreateVariableStatement(final CreateVariableStatement statement);

		/**
		 * Called if the submitted statement is a assign statement.
		 *
		 * @param statement The assign statement.
		 */
		
		void isAssignStatement(final AssignStatement statement);

		/**
		 * Called if the submitted statement is a prompt statement.
		 *
		 * @param statement The prompt statement.
		 */

		void isPromptStatement(final PromptStatement statement);

		/**
		 * Called if the submitted statement is a print variable statement.
		 *
		 * @param statement The print variable statement.
		 */
		
		void isPrintVariableStatement(final PrintVariableStatement statement);

		/**
		 * Called if the submitted statement is a print statement.
		 *
		 * @param statement The print statement.
		 */
		
		void isPrintStatement(final PrintStatement statement);

		/**
		 * Called if the submitted statement is a if block.
		 *
		 * @param statement The if block.
		 */

		void isIfBlock(final IfBlock statement);

		/**
		 * Called if the submitted statement is a else block.
		 *
		 * @param statement The else block.
		 */
		
		void isElseBlock(final ElseBlock statement);

		/**
		 * Called if the submitted statement is a for loop.
		 *
		 * @param statement The for loop.
		 */

		void isForLoop(final ForLoop statement);

		/**
		 * Called if the submitted statement is a while loop.
		 *
		 * @param statement The while loop.
		 */
		
		void isWhileLoop(final WhileLoop statement);

		/**
		 * Called if the submitted statement is a line comment.
		 *
		 * @param statement The line comment.
		 */

		void isLineComment(final LineComment statement);

		/**
		 * Called if the submitted statement is a block comment.
		 *
		 * @param statement The block comment.
		 */
		
		void isBlockComment(final BlockComment statement);

		/**
		 * Called if the submitted statement is an unknown block.
		 *
		 * @param statement The unknown statement.
		 */

		void isUnknownStatement(final Statement statement);

	}

}