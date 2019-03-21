package xyz.algogo.core.statement.simple.comment;

/**
 * Represents a block comment.
 */

public class BlockComment extends Comment {

	/**
	 * The statement ID.
	 */

	public static final int STATEMENT_ID = 14;

	/**
	 * Creates a new block comment.
	 *
	 * @param content The comment content.
	 */

	public BlockComment(final String content) {
		super(content);
	}

	@Override
	public int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public BlockComment copy() {
		return new BlockComment(this.getContent());
	}

}