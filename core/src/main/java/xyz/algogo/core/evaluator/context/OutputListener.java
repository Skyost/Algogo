package xyz.algogo.core.evaluator.context;

import xyz.algogo.core.statement.Statement;

/**
 * Output listener interface.
 */

public interface OutputListener {

	/**
	 * Output some content for the current user.
	 *
	 * @param source The source statement.
	 * @param content The content.
	 */

	void output(final Statement source, final String content);

}