package xyz.algogo.desktop.editor.menubar.menu.listener.edit;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.menubar.menu.EditEditorMenu;
import xyz.algogo.desktop.editor.menubar.menu.listener.EditorMenuListener;

import java.awt.event.ActionEvent;

/**
 * Edit &rarr; Copy listener.
 */

public class CopyMenuListener extends EditorMenuListener {

	/**
	 * The Edit menu.
	 */

	private EditEditorMenu editMenu;

	/**
	 * Creates a new Edit &rarr; Copy listener.
	 *
	 * @param editor The editor.
	 * @param editMenu The Edit menu.
	 */

	public CopyMenuListener(final EditorFrame editor, final EditEditorMenu editMenu) {
		super(editor);

		this.editMenu = editMenu;
	}

	@Override
	public void actionPerformed(final ActionEvent actionEvent) {
		this.getEditor().getMainPane().getCurrentComponent().copySelection(editMenu);
		editMenu.getPasteMenuItem().setEnabled(true);
	}

	/**
	 * Returns the Edit menu.
	 *
	 * @return The Edit menu.
	 */

	public final EditEditorMenu getEditMenu() {
		return editMenu;
	}

	/**
	 * Sets the Edit menu.
	 *
	 * @param editMenu The Edit menu.
	 */

	public final void setEditMenu(final EditEditorMenu editMenu) {
		this.editMenu = editMenu;
	}

}