package xyz.algogo.desktop.editor.menubar.menu.listener.file;

import xyz.algogo.desktop.editor.EditorFrame;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * File &rarr; Save As... listener.
 */

public class SaveAsMenuListener extends SaveMenuListener {

	/**
	 * Creates a new File &rarr; Save As... listener.
	 *
	 * @param editor The editor.
	 */

	public SaveAsMenuListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent actionEvent) {
		final File file = chooseFile(false);
		if(file == null) {
			return;
		}

		this.getEditor().setCurrentPath(file.getPath());
		super.actionPerformed(actionEvent);
	}

}
