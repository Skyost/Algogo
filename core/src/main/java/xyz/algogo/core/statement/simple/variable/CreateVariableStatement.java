package xyz.algogo.core.statement.simple.variable;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.variable.Variable;
import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.exception.InvalidIdentifierException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a create variable statement.
 */

public class CreateVariableStatement extends VariableStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 4;

	/**
	 * The variable type.
	 */

	private VariableType type;

	/**
	 * Creates a new create variable statement.
	 *
	 * @param identifier The variable identifier.
	 * @param type The variable type.
	 */

	public CreateVariableStatement(final String identifier, final VariableType type) {
		super(identifier);

		this.type = type;
	}

	/**
	 * Returns the variable type.
	 *
	 * @return The variable type.
	 */

	public final VariableType getType() {
		return type;
	}

	/**
	 * Sets the variable type.
	 *
	 * @param type The variable type.
	 */

	public final void setType(final VariableType type) {
		this.type = type;
	}

	@Override
	public final Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		if(evaluator.hasVariable(this.getIdentifier())) {
			return new InvalidIdentifierException(this.getIdentifier());
		}

		evaluator.putVariable(new Variable(this.getIdentifier(), type));
		return null;
	}

	@Override
	public final Exception validate() {
		final Matcher matcher = Pattern.compile("([a-zA-Z_][a-zA-Z_0-9]*)").matcher(this.getIdentifier());

		try {
			if(!matcher.find() || !matcher.group().equals(this.getIdentifier())) {
				throw new Exception();
			}
		}
		catch(final Exception ex) {
			return new InvalidIdentifierException(this.getIdentifier());
		}

		return type == null ? new NullPointerException("Type cannot be null.") : null;
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final CreateVariableStatement copy() {
		return new CreateVariableStatement(this.getIdentifier(), type);
	}

}