package xyz.algogo.desktop.editor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The default editor closing listener.
 */

public class EditorClosingListener extends WindowAdapter {

	/**
	 * The editor.
	 */

	private final EditorFrame editor;

	/**
	 * Creates a new default editor closing listener.
	 *
	 * @param editor The editor.
	 */

	EditorClosingListener(final EditorFrame editor) {
		this.editor = editor;
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 */

	public final EditorFrame getEditor() {
		return editor;
	}

	@Override
	public final void windowClosing(final WindowEvent windowEvent) {
		editor.askBeforeAction(editor::dispose);
	}

}