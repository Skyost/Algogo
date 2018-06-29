package xyz.algogo.desktop.editor.history.command;

import xyz.algogo.desktop.editor.AlgorithmCredits;
import xyz.algogo.desktop.editor.EditorFrame;

/**
 * Represents a "credits change" command.
 */

public class CreditsChangeCommand extends EditorCommand {

	/**
	 * The new credits.
	 */

	private AlgorithmCredits credits;

	/**
	 * The old credits.
	 */

	private final AlgorithmCredits oldCredits;

	/**
	 * Creates a new "credits change" command.
	 *
	 * @param editor The editor.
	 * @param credits The new credits.
	 */

	public CreditsChangeCommand(final EditorFrame editor, final AlgorithmCredits credits) {
		super(editor);

		this.credits = credits;
		this.oldCredits = editor.getCredits().copy();
	}

	@Override
	public final void execute() {
		this.getEditor().getCredits().setCredits(credits);
	}

	@Override
	public final void unExecute() {
		this.getEditor().getCredits().setCredits(oldCredits);
	}

	@Override
	public final boolean canUnExecute() {
		return true;
	}

	/**
	 * Returns the new credits.
	 *
	 * @return The new credits.
	 */

	public final AlgorithmCredits getCredits() {
		return credits;
	}

	/**
	 * Sets the new credits.
	 *
	 * @param credits The new credits.
	 */

	public final void setCredits(final AlgorithmCredits credits) {
		this.credits = credits;
	}

}
