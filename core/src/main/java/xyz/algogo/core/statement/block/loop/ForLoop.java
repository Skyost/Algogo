package xyz.algogo.core.statement.block.loop;

import java.math.BigDecimal;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.variable.Variable;
import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.exception.InvalidIdentifierException;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;

/**
 * Represents a FOR loop.
 */

public class ForLoop extends BlockStatement {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 11;

	/**
	 * The variable identifier.
	 */

	private String identifier;

	/**
	 * The start expression.
	 */

	private Expression start;

	/**
	 * The end expression.
	 */

	private Expression end;

	/**
	 * Creates a new FOR loop.
	 *
	 * @param identifier The variable identifier.
	 * @param start The start expression.
	 * @param end The end expression.
	 * @param statements Children statements.
	 */

	public ForLoop(final String identifier, final Expression start, final Expression end, final Statement... statements) {
		super(statements);

		this.identifier = identifier;
		this.start = start;
		this.end = end;
	}

	/**
	 * Returns the variable identifier.
	 *
	 * @return The variable identifier.
	 */

	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the variable identifier.
	 *
	 * @param identifier The variable identifier.
	 */

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns the start expression.
	 *
	 * @return The start expression.
	 */

	public Expression getStart() {
		return start;
	}

	/**
	 * Sets the start expression.
	 *
	 * @param start The start expression.
	 */

	public void setStart(final Expression start) {
		this.start = start;
	}

	/**
	 * Returns the end expression.
	 *
	 * @return The end expression.
	 */

	public Expression getEnd() {
		return end;
	}

	/**
	 * Sets the end expression.
	 *
	 * @param end The end expression.
	 */

	public void setEnd(final Expression end) {
		this.end = end;
	}

	@Override
	public Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Variable variable = evaluator.getVariable(identifier);
		if(variable == null) {
			return new InvalidIdentifierException(identifier);
		}

		if(variable.getType() != VariableType.NUMBER) {
			return new InvalidIdentifierException(identifier, identifier + " is not a number.");
		}

		final Atom startAtom = start.evaluate(evaluator, context);
		final Atom endAtom = end.evaluate(evaluator, context);

		if(!NumberAtom.hasNumberType(startAtom) || !NumberAtom.hasNumberType(endAtom)) {
			return new ParseException("Start and End must return a number.");
		}

		final BigDecimal start = (BigDecimal)startAtom.getValue();
		final BigDecimal end = (BigDecimal)endAtom.getValue();

		if(start.compareTo(end) <= 0) {
			for(BigDecimal i = start; i.compareTo(end) <= 0 && !context.isStopped(); i = i.add(BigDecimal.ONE, context.getMathContext())) {
				final Exception ex = forLoop(evaluator, context, variable, i);
				if(ex != null) {
					return ex;
				}
			}

			return null;
		}

		for(BigDecimal i = start; i.compareTo(end) >= 0 && !context.isStopped(); i = i.subtract(BigDecimal.ONE, context.getMathContext())) {
			final Exception ex = forLoop(evaluator, context, variable, i);
			if(ex != null) {
				return ex;
			}
		}

		return null;
	}

	@Override
	public int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public ForLoop copy() {
		return new ForLoop(identifier, start.copy(), end.copy(), copyStatements());
	}

	/**
	 * Runs the FOR loop body.
	 *
	 * @param evaluator The current expression evaluator.
	 * @param context The current evaluation context.
	 * @param variable The variable.
	 * @param i The current value.
	 *
	 * @return Whether an exception occurs.
	 */

	private Exception forLoop(final ExpressionEvaluator evaluator, final EvaluationContext context, final Variable variable, final BigDecimal i) {
		variable.setValue(i);

		final Exception ex = super.evaluate(evaluator, context);
		if(ex != null) {
			return ex;
		}

		return null;
	}

}