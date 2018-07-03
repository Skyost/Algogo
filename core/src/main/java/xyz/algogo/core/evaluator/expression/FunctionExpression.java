package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a function expression.
 */

public class FunctionExpression extends Expression {

	/**
	 * The function identifier.
	 */

	private String identifier;

	/**
	 * The function arguments.
	 */

	private Expression[] arguments;

	/**
	 * Creates a new function expression.
	 *
	 * @param identifier The function identifier.
	 * @param arguments The function arguments.
	 */

	public FunctionExpression(final String identifier, final Expression... arguments) {
		this.identifier = identifier;
		this.arguments = arguments;
	}

	/**
	 * Returns the function identifier.
	 *
	 * @return The function identifier.
	 */

	public final String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the function identifier.
	 *
	 * @param identifier The function identifier.
	 */

	public final void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Returns the function arguments.
	 *
	 * @return The function arguments.
	 */

	public final Expression[] getArguments() {
		return arguments;
	}

	/**
	 * Sets the function arguments.
	 *
	 * @param arguments The function arguments.
	 */

	public final void setArguments(final Expression... arguments) {
		this.arguments = arguments;
	}

	@Override
	public final Atom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		if(!evaluator.hasFunction(identifier)) {
			return NumberAtom.ZERO;
		}

		final List<Atom> arguments = new ArrayList<>();
		for(final Expression argument : this.arguments) {
			arguments.add(argument.evaluate(evaluator, context));
		}

		return evaluator.getFunction(identifier).evaluate(context, arguments.toArray(new Atom[arguments.size()]));
	}

	@Override
	public final FunctionExpression copy() {
		final List<Expression> copy = new ArrayList<>();
		for(final Expression argument : arguments) {
			copy.add(argument.copy());
		}

		return new FunctionExpression(identifier, copy.toArray(new Expression[copy.size()]));
	}

}