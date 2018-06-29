package xyz.algogo.desktop.editor.history;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.history.command.EditorCommand;
import xyz.algogo.desktop.utils.SizedStack;

import java.util.Arrays;
import java.util.HashSet;

/**
 * A class that represents an editor history and which allows to execute, un-execute and re-execute actions.
 */

public class EditorHistory {

	/**
	 * History items.
	 */

	private static final int HISTORY_ITEMS = 20;

	/**
	 * The undo history.
	 */

	private SizedStack<EditorCommand> undoHistory = new SizedStack<>(HISTORY_ITEMS);

	/**
	 * The redo history.
	 */

	private SizedStack<EditorCommand> redoHistory = new SizedStack<>(HISTORY_ITEMS);

	/**
	 * Whether the current algorithm has been changed.
	 */

	private boolean algorithmChanged = false;

	/**
	 * The editor.
	 */

	private final EditorFrame editor;

	/**
	 * History listeners.
	 */

	private final HashSet<EditorHistoryListener> listeners = new HashSet<>();

	/**
	 * Creates a new editor history.
	 *
	 * @param editor The editor.
	 * @param listeners I/O listeners.
	 */

	public EditorHistory(final EditorFrame editor, final EditorHistoryListener... listeners) {
		this.editor = editor;
		this.listeners.addAll(Arrays.asList(listeners));
	}

	/**
	 * Executes a command.
	 *
	 * @param command The command.
	 */

	public final void execute(final EditorCommand command) {
		command.execute();
		undoHistory.push(command);
		clearRedoHistory();

		algorithmChanged = true;
		editor.refreshTitle();

		for(final EditorHistoryListener listener : listeners) {
			listener.commandExecuted(this, command);
		}
	}

	/**
	 * Undo the action that is on top of the stack.
	 */

	public final void undo() {
		if(!canUndo()) {
			return;
		}

		final EditorCommand command = undoHistory.pop();
		command.unExecute();

		redoHistory.push(command);
		editor.refreshTitle();

		for(final EditorHistoryListener listener : listeners) {
			listener.commandUndone(this, command);
		}
	}

	/**
	 * Returns whether the last action can be undone.
	 *
	 * @return Whether the last action can be undone.
	 */

	public final boolean canUndo() {
		if(undoHistory.isEmpty()) {
			return false;
		}

		final EditorCommand command = undoHistory.peek();
		return command.canUnExecute();
	}

	/**
	 * Clears all undo history.
	 */

	public final void clearUndoHistory() {
		undoHistory.clear();

		for(final EditorHistoryListener listener : listeners) {
			listener.undoHistoryCleared(this);
		}
	}

	/**
	 * Redo the action that is on top of the stack.
	 */

	public final void redo() {
		if(!canRedo()) {
			return;
		}

		final EditorCommand command = redoHistory.pop();
		command.reExecute();

		undoHistory.push(command);
		editor.refreshTitle();

		for(final EditorHistoryListener listener : listeners) {
			listener.commandRedone(this, command);
		}
	}

	/**
	 * Returns whether the last action can be redone.
	 *
	 * @return Whether the last action can be redone.
	 */

	public final boolean canRedo() {
		if(redoHistory.isEmpty()) {
			return false;
		}

		final EditorCommand command = redoHistory.peek();
		return command.canReExecute();
	}

	/**
	 * Clears all redo history.
	 */

	public final void clearRedoHistory() {
		redoHistory.clear();

		for(final EditorHistoryListener listener : listeners) {
			listener.redoHistoryCleared(this);
		}
	}

	/**
	 * Returns whether the algorithm has changed.
	 *
	 * @return Whether the algorithm has changed.
	 */

	public final boolean hasAlgorithmChanged() {
		return algorithmChanged;
	}

	/**
	 * Sets whether the algorithm has changed.
	 *
	 * @param algorithmChanged Whether the algorithm has changed.
	 */

	public final void setAlgorithmChanged(final boolean algorithmChanged) {
		this.algorithmChanged = algorithmChanged;
	}

	/**
	 * Adds a history listener.
	 *
	 * @param listener The history listener.
	 */

	public final void addListener(final EditorHistoryListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a history listener.
	 *
	 * @param listener The history listener.
	 */

	public final void removeListener(final EditorHistoryListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Clears all history listeners.
	 */

	public final void clearListeners() {
		listeners.clear();
	}

}