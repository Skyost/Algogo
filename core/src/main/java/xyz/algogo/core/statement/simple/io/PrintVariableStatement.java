package xyz.algogo.core.statement.simple.io;

import xyz.algogo.core.evaluator.*;
import xyz.algogo.core.exception.InvalidIdentifierException;
import xyz.algogo.core.language.Language;

import java.math.BigDecimal;

/**
 * Represents a print variable statement.
 */

public class PrintVariableStatement extends PrintStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 7;

	/**
	 * The variable identifier.
	 */

	private String identifier;

	/**
	 * Creates a new print variable statement.
	 *
	 * @param identifier The variable identifier.
	 * @param message The message.
	 * @param lineBreak Whether a line break should be appended.
	 */

	public PrintVariableStatement(final String identifier, final String message, final boolean lineBreak) {
		super(message, lineBreak);

		this.identifier = identifier;
	}

	/**
	 * Returns the variable identifier.
	 *
	 * @return The variable identifier.
	 */

	public final String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the variable identifier.
	 *
	 * @param identifier The identifier.
	 */

	public final void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	@Override
	public final Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Variable variable = evaluator.getVariable(identifier);
		if(variable == null) {
			return new InvalidIdentifierException(this.getIdentifier());
		}

		final OutputListener listener = evaluator.getOutputListener();
		if(this.getMessage() != null) {
			final Exception ex = super.evaluate(evaluator, context);
			if(ex != null) {
				return ex;
			}
		}

		String variableValue = variable.getType() == VariableType.NUMBER ? ((BigDecimal)variable.getValue()).toPlainString() : (String)variable.getValue();
		if(this.shouldLineBreak()) {
			variableValue += System.getProperty("line.separator");
		}

		listener.output(this, variableValue);
		return null;
	}

	@Override
	public final Exception validate() {
		return null;
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translatePrintVariableStatement(this);
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final PrintVariableStatement copy() {
		return new PrintVariableStatement(identifier, this.getMessage(), this.shouldLineBreak());
	}

}