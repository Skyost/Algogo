package xyz.algogo.desktop.editor;

import java.io.File;

/**
 * Interface of an editor I/O listener.
 */

public interface EditorIOListener {

	/**
	 * Called when the editor loads an algorithm from a file.
	 *
	 * @param file The file.
	 */

	void algorithmLoaded(final File file);

	/**
	 * Called when the editor saves an algorithm to a file.
	 *
	 * @param file The file.
	 */

	void algorithmSaved(final File file);

}