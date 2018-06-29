package xyz.algogo.desktop.editor.export.languages;

import xyz.algogo.core.language.PHPLanguage;
import xyz.algogo.desktop.editor.EditorFrame;

/**
 * The PHP export target.
 */

public class PHPExportTarget extends LanguageExportTarget {

	/**
	 * Creates a new PHP export target.
	 *
	 * @param editor The editor.
	 */

	public PHPExportTarget(final EditorFrame editor) {
		super(editor, new PHPLanguage(), "menu.file.export.php");
	}

}