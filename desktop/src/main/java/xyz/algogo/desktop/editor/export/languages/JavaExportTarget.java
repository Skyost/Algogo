package xyz.algogo.desktop.editor.export.languages;

import xyz.algogo.core.language.JavaLanguage;
import xyz.algogo.desktop.editor.EditorFrame;

import java.io.File;

/**
 * The Java export target.
 */

public class JavaExportTarget extends LanguageExportTarget {

	/**
	 * Creates a new Java export target.
	 *
	 * @param editor The editor.
	 */

	public JavaExportTarget(final EditorFrame editor) {
		super(editor, new JavaLanguage(null), "menu.file.export.java");
	}

	@Override
	public final void export(final File file) {
		final JavaLanguage language = (JavaLanguage)this.getLanguage();
		language.setClassName(file.getName().replace(" ", "").replace("-", ""));

		super.export(file);
	}

}