package xyz.algogo.desktop.editor.menubar.menu.listener.edit;

import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.menubar.menu.listener.EditorMenuListener;

import java.awt.event.ActionEvent;

/**
 * Edit &rarr; Paste listener.
 */

public class PasteMenuListener extends EditorMenuListener {

	/**
	 * Creates a new Edit &rarr; Paste listener.
	 *
	 * @param editor The editor.
	 */

	public PasteMenuListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public void actionPerformed(final ActionEvent actionEvent) {
		try {
			this.getEditor().getMainPane().getCurrentComponent().pasteClipboard();
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

}