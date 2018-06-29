package xyz.algogo.desktop.editor.history.command;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.EditorMainPane;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeNode;

import javax.swing.tree.TreePath;

/**
 * Represents an executable, un-executable and re-executable command.
 */

public abstract class EditorCommand {

	/**
	 * The editor.
	 */

	private EditorFrame editor;

	/**
	 * Creates a new editor command.
	 *
	 * @param editor The editor.
	 */

	protected EditorCommand(final EditorFrame editor) {
		this.editor = editor;
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
	 * Executes the action.
	 */

	public abstract void execute();

	/**
	 * Re-executes the action.
	 */

	public void reExecute() {
		execute();
	}

	/**
	 * Returns whether the action can be re-executed.
	 *
	 * @return Whether the action can be re-executed.
	 */

	public boolean canReExecute() {
		return true;
	}

	/**
	 * Un-executes the action.
	 */

	public abstract void unExecute();

	/**
	 * Returns whether the action can be un-executed.
	 *
	 * @return Whether the action can be un-executed.
	 */

	public abstract boolean canUnExecute();

	/**
	 * Reloads the tree and select the specified node.
	 *
	 * @param node The node.
	 */

	protected void reloadAndSelect(final AlgorithmTreeNode node) {
		final EditorMainPane mainPane = this.getEditor().getMainPane();
		mainPane.getAlgorithmTreeModel().reload(mainPane.getAlgorithmTree(), node.getParent());
		mainPane.getAlgorithmTree().setSelectionPath(new TreePath(node.getPath()));
	}

}