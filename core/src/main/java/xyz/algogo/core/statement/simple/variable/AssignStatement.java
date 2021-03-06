package xyz.algogo.core.statement.simple.variable;

import java.math.BigDecimal;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.variable.Variable;
import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.exception.InvalidIdentifierException;
import xyz.algogo.core.exception.InvalidVariableValueException;

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

	public Expression getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value The value.
	 */

	public void setValue(final Expression value) {
		this.value = value;
	}

	@Override
	public Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		if(!evaluator.hasVariable(this.getIdentifier())) {
			return new InvalidIdentifierException(this.getIdentifier());
		}

		final Variable variable = evaluator.getVariable(this.getIdentifier());
		final Atom atom = this.value.evaluate(evaluator, context);
		if(atom == null || (variable.getType() == VariableType.NUMBER && !(atom.getValue() instanceof BigDecimal)) || (variable.getType() == VariableType.STRING && !(atom.getValue() instanceof String))) {
			return new InvalidVariableValueException(this.getIdentifier());
		}

		evaluator.getVariable(this.getIdentifier()).setValue(atom.getValue());
		return null;
	}

	@Override
	public Exception validate() {
		return value == null ? new NullPointerException("Value cannot be null.") : null;
	}

	@Override
	public int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public AssignStatement copy() {
		return new AssignStatement(this.getIdentifier(), value.copy());
	}

}