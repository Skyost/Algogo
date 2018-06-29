package xyz.algogo.desktop.editor.component.tree;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.VariableType;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;
import xyz.algogo.core.statement.block.root.BeginningBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.core.statement.block.root.VariablesBlock;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * The algorithm tree model.
 */

public class AlgorithmTreeModel extends DefaultTreeModel {

	/**
	 * Creates a new algorithm tree model.
	 */

	public AlgorithmTreeModel() {
		super(null);
	}

	@Override
	public final AlgorithmTreeNode getRoot() {
		return (AlgorithmTreeNode)super.getRoot();
	}

	@Override
	public final void setRoot(final TreeNode node) {
		if(!(node instanceof AlgorithmTreeNode)) {
			throw new IllegalArgumentException("Node must be an instance of AlgorithmTreeNode.");
		}

		if(!((AlgorithmTreeNode)node).isRoot()) {
			throw new IllegalArgumentException("Node cannot be root.");
		}

		super.setRoot(node);
	}

	/**
	 * Returns the beginning node.
	 *
	 * @return The beginning node.
	 */

	public final AlgorithmTreeNode getVariablesNode() {
		return getNodeByType(VariablesBlock.STATEMENT_ID);
	}

	/**
	 * Returns the variables block.
	 *
	 * @return The variables block.
	 */

	public final VariablesBlock getVariablesBlock() {
		return ((AlgorithmRootBlock)this.getRoot().statement).getVariablesBlock();
	}

	/**
	 * Returns whether the variables node has children nodes.
	 *
	 * @return Whether the variables node has children nodes.
	 */

	public final boolean hasVariables() {
		return getVariablesBlock().getStatementCount() > 0;
	}

	/**
	 * Returns whether the variables node has children nodes.
	 *
	 * @param type The type.
	 *
	 * @return Whether the variables node has children nodes.
	 */

	public final boolean hasVariables(final VariableType type) {
		return buildVariablesList(type).length > 0;
	}

	/**
	 * Creates a variables list.
	 *
	 * @return A list containing all variables name.
	 */

	public final String[] buildVariablesList() {
		return buildVariablesList(null);
	}

	/**
	 * Creates a variables list.
	 *
	 * @param type Variable type.
	 *
	 * @return A list containing all variables name.
	 */

	public final String[] buildVariablesList(final VariableType type) {
		final List<String> variables = new ArrayList<>();
		for(final Statement statement : getVariablesBlock().listStatementsById(CreateVariableStatement.STATEMENT_ID)) {
			final CreateVariableStatement createVariableStatement = (CreateVariableStatement)statement;
			if(type != null && createVariableStatement.getType() != type) {
				continue;
			}
			variables.add(createVariableStatement.getIdentifier());
		}
		return variables.toArray(new String[variables.size()]);
	}

	/**
	 * Returns the beginning node.
	 *
	 * @return The beginning node.
	 */

	public final AlgorithmTreeNode getBeginningNode() {
		return getNodeByType(BeginningBlock.STATEMENT_ID);
	}

	/**
	 * Returns the beginning block.
	 *
	 * @return The beginning block.
	 */

	public final BeginningBlock getBeginningBlock() {
		return ((AlgorithmRootBlock)this.getRoot().statement).getBeginningBlock();
	}

	/**
	 * Puts an algorithm content into this model.
	 *
	 * @param algorithm The algorithm.
	 * @param tree The algorithm tree.
	 */

	public final void fromAlgorithm(final Algorithm algorithm, final AlgorithmTree tree) {
		this.setRoot(new AlgorithmTreeNode(algorithm.getRootBlock()));
		this.reload(tree);
	}

	/**
	 * Creates an algorithm from this model content.
	 *
	 * @return The algorithm.
	 */

	public final Algorithm toAlgorithm() {
		final Algorithm algorithm = new Algorithm();
		algorithm.getRootBlock().clearStatements();
		algorithm.getRootBlock().addStatements(((AlgorithmRootBlock)this.getRoot().statement).listStatements());
		return algorithm;
	}

	/**
	 * Reloads this model and expand all its nodes.
	 *
	 * @param tree The corresponding tree.
	 */

	public final void reload(final AlgorithmTree tree) {
		reload(tree, this.getRoot());
	}

	/**
	 * Reloads this model and expand all children nodes of the provided node.
	 *
	 * @param tree The corresponding tree.
	 * @param node The node.
	 */

	public final void reload(final AlgorithmTree tree, final AlgorithmTreeNode node) {
		super.reload(node);
		tree.expandAllNodes();
	}

	/**
	 * Sets default nodes (VARIABLES, BEGINNING, END) and reload.
	 */

	void setDefaultNodes() {
		final AlgorithmTreeNode root = new AlgorithmTreeNode(new AlgorithmRootBlock());
		root.add(new AlgorithmTreeNode(new VariablesBlock()));
		root.add(new AlgorithmTreeNode(new BeginningBlock()));
		root.add(new AlgorithmTreeNode(new EndBlock()));

		this.setRoot(root);
	}

	/**
	 * Returns the first node that matches the selected type.
	 *
	 * @return The first node that matches the selected type.
	 */

	private AlgorithmTreeNode getNodeByType(final int type) {
		final AlgorithmTreeNode root = this.getRoot();
		final int n = root.getChildCount();
		for(int i = 0; i < n; i++) {
			final AlgorithmTreeNode node = root.getChildAt(i);
			if(node.statement.getStatementId() == type) {
				return node;
			}
		}

		return null;
	}

}