package xyz.algogo.core.evaluator.function;

import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

/**
 * Represents an abstract function.
 */

public abstract class Function {

	/**
	 * Function identifier.
	 */

	private final String identifier;

	/**
	 * Creates a new function.
	 *
	 * @param identifier Function identifier.
	 */

	public Function(final String identifier) {
		this.identifier = identifier;
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
	 * Evaluates the current function.
	 *
	 * @param context The evaluation context.
	 * @param arguments Arguments to pass to the function.
	 *
	 * @return The result.
	 */

	public abstract Atom evaluate(final EvaluationContext context, final Atom... arguments);

}