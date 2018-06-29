package xyz.algogo.desktop.editor.export.languages;

import xyz.algogo.core.language.JavaScriptLanguage;
import xyz.algogo.desktop.editor.EditorFrame;

/**
 * The JavaScript export target.
 */

public class JavaScriptExportTarget extends LanguageExportTarget {

	/**
	 * Creates a new JavaScript export target.
	 *
	 * @param editor The editor.
	 */

	public JavaScriptExportTarget(final EditorFrame editor) {
		super(editor, new JavaScriptLanguage(), "menu.file.export.js");
	}

}