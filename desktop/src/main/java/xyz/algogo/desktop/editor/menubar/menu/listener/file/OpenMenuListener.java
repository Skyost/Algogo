package xyz.algogo.desktop.editor.menubar.menu.listener.file;

import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * File &rarr; Open... listener.
 */

public class OpenMenuListener extends FileDialogMenuListener {

	/**
	 * Creates a new File &rarr; Open... listener.
	 *
	 * @param editor The editor.
	 */

	public OpenMenuListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent actionEvent) {
		try {
			final File file = chooseFile(true);
			if(file == null) {
				return;
			}

			this.getEditor().open(file);
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

}
