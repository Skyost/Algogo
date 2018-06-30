package xyz.algogo.core.statement.simple.variable;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.exception.InvalidIdentifierException;
import xyz.algogo.core.language.Language;

/**
 * Represents an assign statement.
 */

public class AssignStatement extends VariableStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 5;

	/**
	 * Value to assign.
	 */

	private Expression value;

	/**
	 * Creates a new assign statement.
	 *
	 * @param identifier The variable identifier.
	 * @param value The value.
	 */

	public AssignStatement(final String identifier, final Expression value) {
		super(identifier);

		this.value = value;
	}

	/**
	 * Returns the value.
	 *
	 * @return The value.
	 */

	public final Expression getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value The value.
	 */

	public final void setValue(final Expression value) {
		this.value = value;
	}

	@Override
	public final Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		if(!evaluator.hasVariable(this.getIdentifier())) {
			return new InvalidIdentifierException(this.getIdentifier());
		}

		evaluator.getVariable(this.getIdentifier()).setValue(value.evaluate(evaluator, context).getValue());
		return null;
	}

	@Override
	public final Exception validate() {
		return value == null ? new NullPointerException("Value cannot be null.") : null;
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateAssignStatement(this);
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final AssignStatement copy() {
		return new AssignStatement(this.getIdentifier(), value.copy());
	}

}