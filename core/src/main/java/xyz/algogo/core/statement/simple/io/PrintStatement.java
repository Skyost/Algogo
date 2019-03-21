package xyz.algogo.core.statement.simple.io;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.statement.simple.SimpleStatement;

/**
 * Represents a print statement.
 */

public class PrintStatement extends SimpleStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 8;

	/**
	 * The message.
	 */

	private String message;

	/**
	 * Whether a line break should be appended.
	 */

	private boolean lineBreak;

	/**
	 * Creates a new print statement.
	 *
	 * @param message The message.
	 * @param lineBreak Whether a line break should be appended.
	 */

	public PrintStatement(final String message, final boolean lineBreak) {
		this.message = message;
		this.lineBreak = lineBreak;
	}

	/**
	 * Returns the message.
	 *
	 * @return The message.
	 */

	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message The message.
	 */

	public void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * Returns whether a line break should be appended.
	 *
	 * @return Whether a line break should be appended.
	 */

	public boolean shouldLineBreak() {
		return lineBreak;
	}

	/**
	 * Sets whether a line break should be appended.
	 *
	 * @param lineBreak Whether a line break should be appended.
	 */

	public void setShouldLineBreak(final boolean lineBreak) {
		this.lineBreak = lineBreak;
	}

	@Override
	public Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		String message = this.message;
		if(lineBreak) {
			if(message == null) {
				message = "";
			}
			message += System.getProperty("line.separator");
		}

		context.getOutputListener().output(this, message);
		return null;
	}

	@Override
	public Exception validate() {
		return null;
	}

	@Override
	public int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public PrintStatement copy() {
		return new PrintStatement(message, lineBreak);
	}

}