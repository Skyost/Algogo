package xyz.algogo.desktop.editor.export;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.export.languages.JavaExportTarget;
import xyz.algogo.desktop.editor.export.languages.JavaScriptExportTarget;
import xyz.algogo.desktop.editor.export.languages.PHPExportTarget;
import xyz.algogo.desktop.editor.export.languages.PythonExportTarget;

import java.io.File;

/**
 * Represents an export target. It can be a programming language or an image or anything else !
 */

public abstract class ExportTarget {

	/**
	 * The line separator.
	 */

	protected String LINE_SEPARATOR = System.lineSeparator();

	/**
	 * The editor.
	 */

	private EditorFrame editor;

	/**
	 * The export target name.
	 */

	private String name;

	/**
	 * The extension.
	 */

	private String extension;

	/**
	 * Creates a new export target.
	 *
	 * @param editor The editor.
	 * @param key The language name key.
	 * @param extension The export target extension.
	 */

	public ExportTarget(final EditorFrame editor, final String key, final String extension) {
		this.editor = editor;
		this.name = editor.getAppLanguage().getString(key);
		this.extension = extension;
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 */

	public final EditorFrame getEditor() {
		return editor;
	}

	/**
	 * Sets the editor.
	 *
	 * @param editor The editor.
	 */

	public final void setEditor(final EditorFrame editor) {
		this.editor = editor;
	}

	/**
	 * Returns the name.
	 *
	 * @return The name.
	 */

	public final String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name The name.
	 */

	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the export target extension.
	 *
	 * @return The extension.
	 */

	public final String getExtension() {
		return extension;
	}

	/**
	 * Sets the export target extension.
	 *
	 * @param extension The extension.
	 */

	public final void setExtension(final String extension) {
		this.extension = extension;
	}

	/**
	 * Exports the current content to a specified file.
	 *
	 * @param file The file.
	 */

	public abstract void export(final File file);

	/**
	 * Returns all available export targets.
	 *
	 * @param editor The editor.
	 *
	 * @return All available export targets.
	 */

	public static ExportTarget[] getExportTargets(final EditorFrame editor) {
		return new ExportTarget[]{new JavaExportTarget(editor), new JavaScriptExportTarget(editor), new PythonExportTarget(editor), new PHPExportTarget(editor), new HTMLExportTarget(editor), new ImageExportTarget(editor)};
	}

}