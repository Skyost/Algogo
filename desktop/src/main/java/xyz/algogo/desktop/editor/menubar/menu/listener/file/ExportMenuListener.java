package xyz.algogo.desktop.editor.menubar.menu.listener.file;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.export.ExportTarget;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * File &rarr; Export &rarr; <em>Target</em> listener.
 */

public class ExportMenuListener extends FileDialogMenuListener {

	/**
	 * The corresponding target.
	 */

	private ExportTarget target;

	/**
	 * Creates a new File &rarr; Export &rarr; <em>Target</em> listener.
	 *
	 * @param editor The editor.
	 * @param target The corresponding target.
	 */

	public ExportMenuListener(final EditorFrame editor, final ExportTarget target) {
		super(editor, new FileNameExtensionFilter(target.getName() + " (*." + target.getExtension() + ")", target.getExtension()));

		this.target = target;
	}

	@Override
	public final void actionPerformed(final ActionEvent actionEvent) {
		final File file = chooseFile(false);
		if(file == null) {
			return;
		}

		target.export(file);
	}

	/**
	 * Returns the corresponding target.
	 *
	 * @return The corresponding target.
	 */

	public final ExportTarget getTarget() {
		return target;
	}

	/**
	 * Sets the corresponding target.
	 *
	 * @param target The target.
	 */

	public final void setTarget(final ExportTarget target) {
		this.target = target;
	}

}
