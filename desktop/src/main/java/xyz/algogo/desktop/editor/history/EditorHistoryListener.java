package xyz.algogo.desktop.editor.history;

import xyz.algogo.desktop.editor.history.command.EditorCommand;

/**
 * History listener interface.
 */

public interface EditorHistoryListener {

	/**
	 * Triggered when a command has been executed.
	 *
	 * @param history The editor history.
	 * @param command The command.
	 */

	void commandExecuted(final EditorHistory history, final EditorCommand command);

	/**
	 * Triggered when a command has been undone.
	 *
	 * @param history The editor history.
	 * @param command The command.
	 */

	void commandUndone(final EditorHistory history, final EditorCommand command);

	/**
	 * Triggered when a command has been redone.
	 *
	 * @param history The editor history.
	 * @param command The command.
	 */

	void commandRedone(final EditorHistory history, final EditorCommand command);

	/**
	 * Triggered when the undo history has been cleared.
	 *
	 * @param history The editor history.
	 */

	void undoHistoryCleared(final EditorHistory history);

	/**
	 * Triggered when the redo history has been cleared.
	 *
	 * @param history The editor history.
	 */

	void redoHistoryCleared(final EditorHistory history);

}