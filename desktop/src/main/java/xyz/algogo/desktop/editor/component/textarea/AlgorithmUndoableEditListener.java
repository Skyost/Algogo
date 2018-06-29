package xyz.algogo.desktop.editor.component.textarea;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.history.command.textarea.UndoableTextAreaEventCommand;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/**
 * A simple undoable edit listener that is registered in the editor history.
 */

public class AlgorithmUndoableEditListener implements UndoableEditListener {

	/**
	 * The text area.
	 */

	private AlgorithmTextArea textArea;

	/**
	 * Creates a new undoable edit listener.
	 *
	 * @param textArea The target text area.
	 */

	AlgorithmUndoableEditListener(final AlgorithmTextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	public final void undoableEditHappened(final UndoableEditEvent event) {
		final EditorFrame editor = textArea.getEditor();
		editor.getEditorHistory().execute(new UndoableTextAreaEventCommand(editor, event));
	}

	/**
	 * Returns the text area.
	 *
	 * @return The text area.
	 */

	public final AlgorithmTextArea getTextArea() {
		return textArea;
	}

	/**
	 * Sets the text area.
	 *
	 * @param textArea The text area.
	 */

	public final void setTextArea(final AlgorithmTextArea textArea) {
		this.textArea = textArea;
	}

}