package xyz.algogo.desktop.editor.menubar.menu.listener.file;

import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.menubar.menu.listener.EditorMenuListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * An editor menu listener that can prompt user to open a file.
 */

public abstract class FileDialogMenuListener extends EditorMenuListener {

	/**
	 * The file filter.
	 */

	private FileNameExtensionFilter filter;

	/**
	 * Creates a new file dialog menu listener.
	 *
	 * @param editor The editor.
	 */

	public FileDialogMenuListener(final EditorFrame editor) {
		this(editor, new FileNameExtensionFilter(editor.getAppLanguage().getString("menu.file.extension.agg2"), "agg2"));
	}

	/**
	 * Creates a new file dialog menu listener.
	 *
	 * @param editor The editor.
	 * @param filter The file filter.
	 */

	public FileDialogMenuListener(final EditorFrame editor, final FileNameExtensionFilter filter) {
		super(editor);

		this.filter = filter;
	}

	/**
	 * Prompts the user to select a file.
	 *
	 * @param open Whether you want to open or save a file.
	 *
	 * @return The chose file.
	 */

	protected File chooseFile(final boolean open) {
		final JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(filter);

		final int result = open ? chooser.showOpenDialog(this.getEditor()) : chooser.showSaveDialog(this.getEditor());
		if(result != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		File file = chooser.getSelectedFile();
		if(!open) {
			file = correctPath(file);

			if(file.exists()) {
				final AppLanguage appLanguage = this.getEditor().getAppLanguage();
				if(JOptionPane.showConfirmDialog(this.getEditor(), appLanguage.getString("editor.dialog.overwrite.message"), appLanguage.getString("editor.dialog.overwrite.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					return file;
				}

				return null;
			}
		}

		return file;
	}

	/**
	 * Correct a file path (adds extension if needed).
	 *
	 * @param file The file.
	 *
	 * @return The corrected file.
	 */

	private File correctPath(final File file) {
		final String name = file.getName();
		for(final String extension : filter.getExtensions()) {
			if(name.endsWith(extension)) {
				return file;
			}
		}

		return new File(file.getPath() + "." + filter.getExtensions()[0]);
	}

	/**
	 * Returns the file filter.
	 *
	 * @return The file filter.
	 */

	public final FileNameExtensionFilter getFilter() {
		return filter;
	}

	/**
	 * Sets the file filter.
	 *
	 * @param filter The file filter.
	 */

	public final void setFilter(final FileNameExtensionFilter filter) {
		this.filter = filter;
	}

}