package xyz.algogo.desktop.editor.export.languages;

import xyz.algogo.core.language.Language;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.export.ExportTarget;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Represents an Algogo Core language export target.
 */

public class LanguageExportTarget extends ExportTarget {

	/**
	 * The language.
	 */

	private Language language;

	/**
	 * Creates a new language export target.
	 *
	 * @param editor The editor.
	 * @param language The language.
	 */

	public LanguageExportTarget(final EditorFrame editor, final Language language) {
		this(editor, language, language.getName());
	}

	/**
	 * Creates a new language export target.
	 *
	 * @param editor The editor.
	 * @param language The language.
	 * @param key The language name key.
	 */

	public LanguageExportTarget(final EditorFrame editor, final Language language, final String key) {
		super(editor, key, language.getExtension());

		this.language = language;
	}

	@Override
	public void export(final File file) {
		try {
			final String content = this.getEditor().getMainPane().getAlgorithmTree().toAlgorithm().toLanguage(language);
			Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

	/**
	 * Returns the corresponding language.
	 *
	 * @return The corresponding language.
	 */

	public final Language getLanguage() {
		return language;
	}

	/**
	 * Sets the corresponding language.
	 *
	 * @param language The corresponding language.
	 */

	final void setLanguage(final Language language) {
		this.language = language;
		this.setExtension(language.getExtension());
	}

}