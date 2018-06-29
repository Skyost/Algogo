package xyz.algogo.desktop.editor.menubar.menu.listener.edit;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.menubar.menu.EditEditorMenu;

import java.awt.event.ActionEvent;

/**
 * Edit &rarr; Cut listener.
 */

public class CutMenuListener extends CopyMenuListener {

	/**
	 * Creates a new Edit &rarr; Cut listener.
	 *
	 * @param editor The editor.
	 * @param editMenu The Edit menu.
	 */

	public CutMenuListener(final EditorFrame editor, final EditEditorMenu editMenu) {
		super(editor, editMenu);
	}

	@Override
	public final void actionPerformed(final ActionEvent actionEvent) {
		super.actionPerformed(actionEvent);

		this.getEditor().getMainPane().getCurrentComponent().deleteSelection();
	}

}