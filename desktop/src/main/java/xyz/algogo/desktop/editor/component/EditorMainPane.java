package xyz.algogo.desktop.editor.component;

import org.fife.ui.rtextarea.Gutter;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.textarea.AlgorithmTextArea;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTree;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Editors main pane : where the main components (text area & tree) are displayed.
 */

public class EditorMainPane extends JScrollPane {

	/**
	 * Toggle listeners.
	 */

	private final HashSet<EditorMainPaneToggleListener> listeners = new HashSet<>();

	/**
	 * The editor.
	 */

	private EditorFrame editor;

	/**
	 * The algorithm tree.
	 */

	private AlgorithmTree tree;

	/**
	 * The algorithm text area.
	 */

	private AlgorithmTextArea textArea;

	/**
	 * Text area gutter.
	 */

	private final Gutter gutter;

	/**
	 * The current shown component.
	 */

	private EditorMainComponent currentComponent;

	/**
	 * Creates a new editor main pane.
	 *
	 * @param editor The editor.
	 */

	public EditorMainPane(final EditorFrame editor) {
		this.editor = editor;

		this.tree = new AlgorithmTree(editor, new AlgorithmTreeModel());
		this.textArea = new AlgorithmTextArea(editor);
		this.gutter = new Gutter(textArea);

		this.setViewportView(tree);
	}

	/**
	 * Checks whether the algorithm tree is currently shown.
	 *
	 * @return Whether the algorithm tree is currently shown.
	 */

	public boolean isShowingTree() {
		return currentComponent == tree;
	}

	/**
	 * Toggles the currently shown main component.
	 */

	public void toggleCurrentComponent() {
		final EditorMainComponent oldComponent = currentComponent;

		final String content = currentComponent == null ? "" : currentComponent.getContent();

		this.setViewportView(!isShowingTree() || currentComponent == null ? tree : textArea);
		this.setRowHeaderView(isShowingTree() ? null : gutter);
		tree.getSelectionModel().clearSelection();

		currentComponent.open(content);

		editor.getEditorHistory().clearUndoHistory();
		editor.getEditorHistory().clearRedoHistory();

		for(final EditorMainPaneToggleListener listener : listeners) {
			listener.onToggle(oldComponent, currentComponent);
		}
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

		tree.setEditor(editor);
		textArea.setEditor(editor);
	}

	/**
	 * Returns the currently shown main component.
	 *
	 * @return The currently shown main component.
	 */

	public final EditorMainComponent getCurrentComponent() {
		return currentComponent;
	}

	/**
	 * Returns the algorithm tree.
	 *
	 * @return The algorithm tree.
	 */

	public final AlgorithmTree getAlgorithmTree() {
		return tree;
	}

	/**
	 * Sets the algorithm tree.
	 *
	 * @param tree The tree.
	 */

	public final void setAlgorithmTree(final AlgorithmTree tree) {
		this.tree = tree;
	}

	/**
	 * Returns the algorithm tree model.
	 *
	 * @return The algorithm tree model.
	 */

	public final AlgorithmTreeModel getAlgorithmTreeModel() {
		return tree.getModel();
	}

	/**
	 * Sets the algorithm tree model.
	 *
	 * @param model The tree model.
	 */

	public final void setAlgorithmTreeModel(final AlgorithmTreeModel model) {
		this.tree.setModel(model);
	}

	/**
	 * Returns the algorithm text area.
	 *
	 * @return The algorithm text area.
	 */

	public final AlgorithmTextArea getAlgorithmTextArea() {
		return textArea;
	}

	/**
	 * Sets the algorithm text area.
	 *
	 * @param textArea The text area.
	 */

	public final void setAlgorithmTextArea(final AlgorithmTextArea textArea) {
		this.textArea = textArea;
	}

	/**
	 * Registers a toggle listener.
	 *
	 * @param listener The toggle listener.
	 */

	public final void addToggleListener(final EditorMainPaneToggleListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a toggle listener.
	 *
	 * @param listener The toggle listener.
	 */

	public final void removeToggleListener(final EditorMainPaneToggleListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Clears all toggle listeners.
	 */

	public final void clearToggleListeners() {
		listeners.clear();
	}

	@Override
	public final Dimension getPreferredSize() {
		return new Dimension(500, 400);
	}

	@Override
	public final void setViewportView(final Component component) {
		if(!(component instanceof EditorMainComponent)) {
			throw new IllegalArgumentException("Component must be an instance of EditorMainComponent.");
		}

		currentComponent = (EditorMainComponent)component;
		super.setViewportView(component);
	}

}