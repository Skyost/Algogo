package xyz.algogo.desktop.editor.component;

/**
 * Represents an editor main pane toggle listener.
 */

public interface EditorMainPaneToggleListener {

	/**
	 * Triggered when the main component has been toggled.
	 *
	 * @param oldComponent The old component.
	 * @param newComponent The new component.
	 */

	void onToggle(final EditorMainComponent oldComponent, final EditorMainComponent newComponent);

}