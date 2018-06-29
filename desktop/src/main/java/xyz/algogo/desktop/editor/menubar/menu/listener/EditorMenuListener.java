package xyz.algogo.desktop.editor.menubar.menu.listener;

import xyz.algogo.desktop.editor.EditorFrame;

import java.awt.event.ActionListener;

/**
 * Represents an editor menu listener.
 */

public abstract class EditorMenuListener implements ActionListener {

	/**
	 * The editor.
	 */

	private EditorFrame editor;

	/**
	 * Creates a new editor menu listener.
	 *
	 * @param editor The editor.
	 */

	public EditorMenuListener(final EditorFrame editor) {
		this.editor = editor;
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 */

	public EditorFrame getEditor() {
		return editor;
	}

	/**
	 * Sets the editor.
	 *
	 * @param editor The editor.
	 */

	public void setEditor(final EditorFrame editor) {
		this.editor = editor;
	}

}