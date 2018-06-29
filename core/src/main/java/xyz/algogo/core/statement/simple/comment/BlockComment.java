package xyz.algogo.core.statement.simple.comment;

import xyz.algogo.core.language.Language;

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
	public final String toLanguage(final Language language) {
		return language.translateBlockComment(this);
	}

	@Override
	public final int getStatementId() {
		return STATEMENT_ID;
	}

	@Override
	public final BlockComment copy() {
		return new BlockComment(this.getContent());
	}

}