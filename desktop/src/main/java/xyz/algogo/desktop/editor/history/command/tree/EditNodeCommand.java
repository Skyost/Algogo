package xyz.algogo.desktop.editor.history.command.tree;

import xyz.algogo.core.statement.Statement;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeNode;
import xyz.algogo.desktop.editor.history.command.EditorCommand;

/**
 * Represents an "edit node" command.
 */

public class EditNodeCommand extends EditorCommand {

	/**
	 * The node to edit.
	 */

	private AlgorithmTreeNode node;

	/**
	 * A copy of the original statement.
	 */

	private Statement copy;

	/**
	 * A copy of the edited statement.
	 */

	private Statement edited;

	/**
	 * Creates a new edit node command.
	 *
	 * @param editor The editor.
	 * @param node The node.
	 */

	public EditNodeCommand(final EditorFrame editor, final AlgorithmTreeNode node) {
		super(editor);

		setNode(node);
	}

	@Override
	public final void execute() {
		Statement.getStatementType(node.getUserObject(), new EditNodeType(this.getEditor(), node));
		edited = node.getUserObject().copy();
	}

	@Override
	public final void reExecute() {
		node.setUserObject(edited);
		reloadAndSelect(node);
	}

	@Override
	public final void unExecute() {
		node.setUserObject(copy);
		reloadAndSelect(node);
	}

	@Override
	public final boolean canUnExecute() {
		return true;
	}

	/**
	 * Returns the node to edit.
	 *
	 * @return The node to edit.
	 */

	public final AlgorithmTreeNode getNode() {
		return node;
	}

	/**
	 * Sets the node to edit.
	 *
	 * @param node The node to edit.
	 */

	public final void setNode(final AlgorithmTreeNode node) {
		this.node = node;
		this.copy = node.getUserObject().copy();
	}

}