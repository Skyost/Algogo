package xyz.algogo.desktop.editor;

import xyz.algogo.core.Algorithm;

/**
 * This class describes how algorithm credits are handled by the program.
 */

public class AlgorithmCredits {

	/**
	 * Algorithm title.
	 */

	private String title;

	/**
	 * Algorithm author.
	 */

	private String author;

	/**
	 * Creates new algorithm credits.
	 */

	public AlgorithmCredits(final EditorFrame editor) {
		this(editor.getAppLanguage().getString("editor.credits.untitled"), editor.getAppLanguage().getString("editor.credits.anonymous"));
	}

	/**
	 * Creates new algorithm credits.
	 *
	 * @param title Algorithm title.
	 * @param author Algorithm author.
	 */

	public AlgorithmCredits(final String title, final String author) {
		this.title = title;
		this.author = author;
	}

	/**
	 * Returns the algorithm title.
	 *
	 * @return The algorithm title.
	 */

	public final String getTitle() {
		return title;
	}

	/**
	 * Sets the algorithm title.
	 *
	 * @param title The title.
	 */

	public final void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Returns the algorithm author.
	 *
	 * @return The algorithm author.
	 */

	public final String getAuthor() {
		return author;
	}

	/**
	 * Sets the algorithm author.
	 *
	 * @param author The author.
	 */

	public final void setAuthor(final String author) {
		this.author = author;
	}

	/**
	 * Applies credits from an algorithm.
	 *
	 * @param algorithm The algorithm.
	 */

	public final void setCredits(final Algorithm algorithm) {
		setCredits(algorithm.getTitle(), algorithm.getAuthor());
	}

	/**
	 * Applies credits from another instance of this class.
	 *
	 * @param credits The credits.
	 */

	public final void setCredits(final AlgorithmCredits credits) {
		setCredits(credits.title, credits.author);
	}

	/**
	 * Applies specified credits.
	 *
	 * @param title Algorithm title.
	 * @param author Algorithm author.
	 */

	public final void setCredits(final String title, final String author) {
		setTitle(title);
		setAuthor(author);
	}

	/**
	 * Applies credits to an algorithm.
	 *
	 * @param algorithm The algorithm.
	 */

	public final void apply(final Algorithm algorithm) {
		algorithm.setTitle(title);
		algorithm.setAuthor(author);
	}

	/**
	 * Copies this instance.
	 *
	 * @return A copy of this instance.
	 */

	public final AlgorithmCredits copy() {
		return new AlgorithmCredits(title, author);
	}

}