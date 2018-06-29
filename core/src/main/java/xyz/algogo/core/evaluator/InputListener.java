package xyz.algogo.core.evaluator;

import xyz.algogo.core.statement.Statement;

/**
 * Input listener interface.
 */

public interface InputListener {

	/**
	 * Asks user for input.
	 *
	 * @param source Source statement.
	 * @param arguments Arguments provided by statement.
	 *
	 * @return The user input.
	 */

	Object input(final Statement source, final Object... arguments);

}