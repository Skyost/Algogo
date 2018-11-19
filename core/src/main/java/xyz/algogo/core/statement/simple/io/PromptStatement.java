package xyz.algogo.core.statement.simple.io;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.exception.InvalidIdentifierException;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.statement.simple.variable.VariableStatement;

public class PromptStatement extends VariableStatement {

	public static final int STATEMENT_ID = 6;

	private String message;

	public PromptStatement(final String identifier, final String message) {
		super(identifier);

		this.message = message;
	}

	public final String getMessage() {
		return message;
	}

	public final void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public final Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		try {
			if(!evaluator.hasVariable(this.getIdentifier())) {
				return new InvalidIdentifierException(this.getIdentifier());
			}

			final Object input = context.getInputListener().input(this, this.getIdentifier(), message);
			if(input == null || input.toString().trim().isEmpty()) {
				return null;
			}

			final Atom atom = evaluator.evaluate(Expression.parse(input.toString()), context);
			if(atom == null) {
				return new ParseException("Invalid input.");
			}

			evaluator.getVariable(this.getIdentifier()).setValue(atom.getValue());
		}
		catch(final Exception ex) {
			return ex;
		}

		return null;
	}

	@Override
	public final Exception validate() {
		return null;
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final PromptStatement copy() {
		return new PromptStatement(this.getIdentifier(), message);
	}

}