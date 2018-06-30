package xyz.algogo.core.statement.simple.comment;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.statement.simple.SimpleStatement;

/**
 * Represents a comment.
 */

public abstract class Comment extends SimpleStatement {

	/**
	 * The comment content.
	 */

	private String content;

	/**
	 * Creates a new comment.
	 *
	 * @param content The comment content.
	 */

	public Comment(final String content) {
		this.content = content;
	}

	/**
	 * Returns the comment content.
	 *
	 * @return The comment content.
	 */

	public final String getContent() {
		return content;
	}

	/**
	 * Sets the comment content.
	 *
	 * @param content The content.
	 */

	public final void setContent(final String content) {
		this.content = content;
	}

	@Override
	public Exception validate() {
		return null;
	}

	@Override
	public Exception evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		return null;
	}

}