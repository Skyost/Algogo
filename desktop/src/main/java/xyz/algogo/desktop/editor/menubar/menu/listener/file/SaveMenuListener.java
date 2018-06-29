package xyz.algogo.desktop.editor.menubar.menu.listener.file;

import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * File &rarr; Save... listener.
 */

public class SaveMenuListener extends FileDialogMenuListener {

	/**
	 * Creates a new File &rarr; Save... listener.
	 *
	 * @param editor The editor.
	 */

	public SaveMenuListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public void actionPerformed(final ActionEvent actionEvent) {
		try {
			final EditorFrame editor = this.getEditor();
			if(editor.getCurrentPath() == null) {
				new SaveAsMenuListener(this.getEditor()).actionPerformed(null);
				return;
			}

			editor.save(new File(editor.getCurrentPath()));
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

}
