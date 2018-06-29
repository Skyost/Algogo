package xyz.algogo.desktop.editor.history.command.tree;

import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeNode;
import xyz.algogo.desktop.editor.history.command.EditorCommand;

/**
 * Represents an "add node" command.
 */

public class AddNodeCommand extends EditorCommand {

	/**
	 * The parent node.
	 */

	private AlgorithmTreeNode parent;

	/**
	 * The node.
	 */

	private AlgorithmTreeNode node;

	/**
	 * The insert index.
	 */

	private int insertIndex;

	/**
	 * Creates a new "add node" command.
	 *
	 * @param editor The editor.
	 * @param parent The parent node.
	 * @param node The node.
	 */

	public AddNodeCommand(final EditorFrame editor, final AlgorithmTreeNode parent, final AlgorithmTreeNode node) {
		this(editor, parent, node, -1);
	}

	/**
	 * Creates a new "add node" command.
	 *
	 * @param editor The editor.
	 * @param parent The parent node.
	 * @param node The node.
	 * @param insertIndex The insert index.
	 */

	public AddNodeCommand(final EditorFrame editor, final AlgorithmTreeNode parent, final AlgorithmTreeNode node, final int insertIndex) {
		super(editor);

		this.parent = parent;
		this.node = node;
		this.insertIndex = insertIndex;
	}

	@Override
	public final void execute() {
		if(insertIndex == -1) {
			parent.add(node);
		}
		else {
			parent.insert(node, insertIndex);
		}

		final Statement statement = node.getUserObject();
		if(statement.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)statement).hasElseBlock()) {
			parent.insert(new AlgorithmTreeNode(((IfBlock)node.getUserObject()).getElseBlock()), parent.getIndex(node) + 1);
		}

		reloadAndSelect(node);
	}

	@Override
	public final void unExecute() {
		final int index = parent.getIndex(node);
		parent.remove(index);

		final Statement statement = node.getUserObject();
		if(statement.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)statement).hasElseBlock()) {
			parent.remove(index + 1);
		}

		reloadAndSelect(parent);
	}

	@Override
	public final boolean canUnExecute() {
		return parent.getChildAt(insertIndex == -1 ? parent.getChildCount() - 1 : insertIndex) == node;
	}

	/**
	 * Returns the parent node.
	 *
	 * @return The parent node.
	 */

	public final AlgorithmTreeNode getParent() {
		return parent;
	}

	/**
	 * Sets the parent node.
	 *
	 * @param parent The parent node.
	 */

	public final void setParent(final AlgorithmTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * Returns the node.
	 *
	 * @return The node.
	 */

	public final AlgorithmTreeNode getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 *
	 * @param node The node.
	 */

	public final void setNode(final AlgorithmTreeNode node) {
		this.node = node;
	}

	/**
	 * Returns the insert index or <em>-1</em> if you want to insert it at the end of the parent.
	 *
	 * @return The insert index.
	 */

	public final Integer getInsertIndex() {
		return insertIndex;
	}

	/**
	 * Sets the insert index.
	 *
	 * @param insertIndex The insert index  or <em>-1</em> if you want to insert it at the end of the parent.
	 */

	public final void setInsertIndex(final Integer insertIndex) {
		this.insertIndex = insertIndex;
	}

}