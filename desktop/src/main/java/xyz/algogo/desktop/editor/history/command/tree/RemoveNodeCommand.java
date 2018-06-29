package xyz.algogo.desktop.editor.history.command.tree;

import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeNode;
import xyz.algogo.desktop.editor.history.command.EditorCommand;

/**
 * Represents a "remove node" command.
 */

public class RemoveNodeCommand extends EditorCommand {

	/**
	 * The node to remove.
	 */

	private AlgorithmTreeNode node;

	/**
	 * The parent node.
	 */

	private AlgorithmTreeNode parent;

	/**
	 * The node index.
	 */

	private int index;

	/**
	 * Creates a new "remove node" command.
	 *
	 * @param editor The editor.
	 * @param node The node to remove.
	 */

	public RemoveNodeCommand(final EditorFrame editor, final AlgorithmTreeNode node) {
		super(editor);

		setNode(node);
	}

	@Override
	public final void execute() {
		final Statement statement = node.getUserObject();
		if(statement.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)statement).hasElseBlock()) {
			parent.remove(index + 1);
		}

		parent.remove(node);
		reloadAndSelect(parent);
	}

	@Override
	public final void unExecute() {
		parent.insert(node, index);

		final Statement statement = node.getUserObject();
		if(statement.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)statement).hasElseBlock()) {
			parent.insert(node, index + 1);
		}

		reloadAndSelect(node);
	}

	@Override
	public final boolean canUnExecute() {
		return node.getParent() == null;
	}

	/**
	 * Returns the node to remove.
	 *
	 * @return The node to remove.
	 */

	public final AlgorithmTreeNode getNode() {
		return node;
	}

	/**
	 * Sets the node to remove.
	 *
	 * @param node The node to remove.
	 */

	public final void setNode(final AlgorithmTreeNode node) {
		this.node = node;
		this.parent = node.getParent();
		this.index = parent.getIndex(node);
	}

}