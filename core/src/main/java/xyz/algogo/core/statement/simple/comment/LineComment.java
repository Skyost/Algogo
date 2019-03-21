package xyz.algogo.core.statement.simple.comment;

/**
 * Represents a line comment.
 */

public class LineComment extends Comment {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 13;

	/**
	 * Creates a new line comment.
	 *
	 * @param content The line content.
	 */

	public LineComment(final String content) {
		super(content);
	}

	@Override
	public int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public LineComment copy() {
		return new LineComment(this.getContent());
	}

}