package xyz.algogo.desktop.editor.component.tree;

import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * An algorithm tree node.
 */

public class AlgorithmTreeNode extends DefaultMutableTreeNode {

	/**
	 * The held statement.
	 */

	Statement statement;

	/**
	 * Creates a new algorithm tree node.
	 *
	 * @param statement The statement to hold.
	 */

	public AlgorithmTreeNode(final Statement statement) {
		this.statement = statement;
	}

	/**
	 * Creates a new algorithm tree node.
	 *
	 * @param statement The block statement to hold.
	 */

	public AlgorithmTreeNode(final BlockStatement statement) {
		this.statement = statement;

		for(final Statement child : statement.listStatements()) {
			if(child instanceof BlockStatement) {
				addWithoutAlteringStatement(new AlgorithmTreeNode((BlockStatement)child));

				if(child.getStatementId() == IfBlock.STATEMENT_ID) {
					final IfBlock ifBlock = (IfBlock)child;
					if(!ifBlock.hasElseBlock()) {
						continue;
					}

					addWithoutAlteringStatement(new AlgorithmTreeNode(ifBlock.getElseBlock()));
				}
				continue;
			}

			addWithoutAlteringStatement(new AlgorithmTreeNode(child));
		}
	}

	@Override
	public final Statement getUserObject() {
		return statement;
	}

	@Override
	public final void setUserObject(final Object object) {
		if(!(object instanceof Statement)) {
			throw new IllegalArgumentException("Object must be an instance of Statement.");
		}

		this.statement = (Statement)object;
	}

	/**
	 * Adds a node without adding its statement to the currently held statement.
	 *
	 * @param node The node.
	 */

	private void addWithoutAlteringStatement(final AlgorithmTreeNode node) {
		super.insert(node, this.getChildCount());
	}

	@Override
	public final void insert(final MutableTreeNode node, final int index) {
		if(!(node instanceof AlgorithmTreeNode)) {
			throw new IllegalArgumentException("Node must be an instance of AlgorithmTreeNode.");
		}

		if(!getAllowsChildren()) {
			throw new IllegalArgumentException("Cannot insert child to this node.");
		}

		final Statement statement = ((AlgorithmTreeNode)node).statement;
		if(statement.getStatementId() != ElseBlock.STATEMENT_ID) {
			((BlockStatement)this.statement).insertStatement(statement, index);
		}

		super.insert(node, index);
	}

	@Override
	public final boolean getAllowsChildren() {
		return statement instanceof BlockStatement;
	}

	@Override
	public final void setAllowsChildren(final boolean allows) {}

	@Override
	public final boolean isRoot() {
		return statement instanceof AlgorithmRootBlock;
	}

	@Override
	public final boolean isLeaf() {
		return !getAllowsChildren();
	}

	@Override
	public final void remove(final int index) {
		if(!getAllowsChildren()) {
			throw new IllegalArgumentException("Cannot remove child from this node.");
		}

		final Statement statement = getChildAt(index).statement;
		if(statement.getStatementId() != ElseBlock.STATEMENT_ID) {
			((BlockStatement)this.statement).removeStatement(index);
		}

		super.remove(index);
	}

	@Override
	public final void remove(final MutableTreeNode node) {
		if(!(node instanceof AlgorithmTreeNode)) {
			throw new IllegalArgumentException("Node must be an instance of AlgorithmTreeNode.");
		}

		if(!getAllowsChildren()) {
			throw new IllegalArgumentException("Cannot remove child from this node.");
		}

		super.remove(node);
	}

	@Override
	public final AlgorithmTreeNode getParent() {
		return (AlgorithmTreeNode)super.getParent();
	}

	@Override
	public final AlgorithmTreeNode getChildAt(int index) {
		return (AlgorithmTreeNode)super.getChildAt(index);
	}

	/**
	 * Moves this node up.
	 */

	public final void up() {
		up(null);
	}

	/**
	 * Moves this node up.
	 *
	 * @param tree The corresponding tree.
	 */

	public final void up(final AlgorithmTree tree) {
		final AlgorithmTreeNode parent = getParent();
		final int index = parent.getIndex(this);

		if(index == 0) {
			return;
		}

		final AlgorithmTreeNode above = parent.getChildAt(index - 1);
		parent.remove(index);
		parent.insert(this, index - (above.getUserObject().getStatementId() == ElseBlock.STATEMENT_ID ? 2 : 1));

		final Statement statement = getUserObject();
		if(statement.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)statement).hasElseBlock()) {
			parent.getChildAt(index + 1).up();
		}

		if(tree != null) {
			tree.getModel().reload(tree, parent);
			tree.setSelectionPath(new TreePath(this.getPath()));
		}
	}

	/**
	 * Moves this node down.
	 */

	public final void down() {
		down(null);
	}

	/**
	 * Moves this node down.
	 *
	 * @param tree The corresponding tree.
	 */

	public final void down(final AlgorithmTree tree) {
		final AlgorithmTreeNode parent = getParent();
		final int index = parent.getIndex(this);

		if(index == parent.getChildCount() - 1) {
			return;
		}

		final Statement statement = getUserObject();
		if(statement.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)statement).hasElseBlock()) {
			if(index == parent.getChildCount() - 2) {
				return;
			}

			parent.getChildAt(index + 1).down();
		}

		final Statement under = parent.getChildAt(index + 1).getUserObject();
		parent.remove(index);
		parent.insert(this, index + (under.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)under).hasElseBlock() ? 2 : 1));

		if(tree != null) {
			tree.getModel().reload(tree, parent);
			tree.setSelectionPath(new TreePath(this.getPath()));
		}
	}

}