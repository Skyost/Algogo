package xyz.algogo.desktop.editor.history.command.textarea;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.history.command.EditorCommand;

import javax.swing.event.UndoableEditEvent;

/**
 * Represents an undoable text area event command. This allows to support text area event undo and/or redo.
 */

public class UndoableTextAreaEventCommand extends EditorCommand {

	/**
	 * The event.
	 */

	private UndoableEditEvent event;

	/**
	 * Creates a new undoable text area event command.
	 *
	 * @param editor The editor.
	 * @param event The event.
	 */

	public UndoableTextAreaEventCommand(final EditorFrame editor, final UndoableEditEvent event) {
		super(editor);

		this.event = event;
	}

	@Override
	public final void execute() {}

	@Override
	public final void unExecute() {
		event.getEdit().undo();
	}

	@Override
	public final boolean canUnExecute() {
		return event.getEdit().canUndo();
	}

	@Override
	public final void reExecute() {
		event.getEdit().redo();
	}

	@Override
	public final boolean canReExecute() {
		return event.getEdit().canRedo();
	}

	/**
	 * Returns the event.
	 *
	 * @return The event.
	 */

	public final UndoableEditEvent getEvent() {
		return event;
	}

	/**
	 * Sets the event.
	 *
	 * @param event The event.
	 */

	public final void setEvent(final UndoableEditEvent event) {
		this.event = event;
	}

}
