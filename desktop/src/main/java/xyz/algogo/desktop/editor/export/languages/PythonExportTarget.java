package xyz.algogo.desktop.editor.export.languages;

import xyz.algogo.core.language.PythonLanguage;
import xyz.algogo.desktop.editor.EditorFrame;

/**
 * The Python export target.
 */

public class PythonExportTarget extends LanguageExportTarget {

	/**
	 * Creates a new Python export target.
	 *
	 * @param editor The editor.
	 */

	public PythonExportTarget(final EditorFrame editor) {
		super(editor, new PythonLanguage(), "menu.file.export.py");
	}

}