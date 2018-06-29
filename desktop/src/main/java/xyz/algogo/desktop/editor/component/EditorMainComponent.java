package xyz.algogo.desktop.editor.component;

import xyz.algogo.core.Algorithm;

import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Common methods for all editor main components (tree / textarea).
 */

public interface EditorMainComponent {

	/**
	 * Creates a new algorithm.
	 */

	void newAlgorithm();

	/**
	 * Returns current component content.
	 *
	 * @return Current component content.
	 */

	String getContent();

	/**
	 * Opens string content.
	 *
	 * @param content String content.
	 */

	void open(final String content);

	/**
	 * Saves component content to a file.
	 *
	 * @param file The file.
	 *
	 * @throws IOException If any I/O exception occurs.
	 */

	default void save(final File file) throws IOException {
		Files.write(Paths.get(file.getPath()), getContent().getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Deletes selected content.
	 */

	void deleteSelection();

	/**
	 * Copy selected content.
	 *
	 * @param clipboardOwner The current clipboard owner.
	 */

	void copySelection(final ClipboardOwner clipboardOwner);

	/**
	 * Pastes the clipboard content.
	 *
	 * @throws IOException If any I/O exception occurs.
	 * @throws UnsupportedFlavorException If the specified flavor is not supported.
	 */

	void pasteClipboard() throws IOException, UnsupportedFlavorException;

	/**
	 * Creates an algorithm from the component content.
	 *
	 * @return The algorithm.
	 */

	Algorithm toAlgorithm();

}