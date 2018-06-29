package xyz.algogo.desktop.editor.history.command.tree;

import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;
import xyz.algogo.core.statement.block.root.BeginningBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.core.statement.block.root.VariablesBlock;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.io.PrintStatement;
import xyz.algogo.core.statement.simple.io.PrintVariableStatement;
import xyz.algogo.core.statement.simple.io.PromptStatement;
import xyz.algogo.core.statement.simple.variable.AssignStatement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;
import xyz.algogo.desktop.AlgorithmDesktopLineEditor;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTree;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeNode;

import javax.swing.tree.TreePath;

/**
 * Little utility class that allows to open the statement editor according to the specified node.
 */

public class EditNodeType implements Statement.StatementTypeInterface {

	/**
	 * The editor.
	 */

	private EditorFrame editorFrame;

	/**
	 * The node to edit.
	 */

	private AlgorithmTreeNode node;

	/**
	 * The line editor.
	 */

	private AlgorithmDesktopLineEditor lineEditor;

	/**
	 * Creates a new EditNodeType instance.
	 *
	 * @param editor The editor.
	 * @param node The node.
	 */

	public EditNodeType(final EditorFrame editor, final AlgorithmTreeNode node) {
		this(editor, node, new AlgorithmDesktopLineEditor(editor, editor));
	}

	/**
	 * Creates a new EditNodeType instance.
	 *
	 * @param editorFrame The editor.
	 * @param node The node.
	 * @param lineEditor The line editor.
	 */

	public EditNodeType(final EditorFrame editorFrame, final AlgorithmTreeNode node, final AlgorithmDesktopLineEditor lineEditor) {
		this.editorFrame = editorFrame;
		this.node = node;
		this.lineEditor = lineEditor;
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 */
	
	public final EditorFrame getEditorFrame() {
		return editorFrame;
	}

	/**
	 * Sets the editor.
	 *
	 * @param editorFrame The editor.
	 */
	
	public final void setEditorFrame(final EditorFrame editorFrame) {
		this.editorFrame = editorFrame;
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
	}

	/**
	 * Returns the line editor.
	 *
	 * @return The line editor.
	 */

	public final AlgorithmDesktopLineEditor getLineEditor() {
		return lineEditor;
	}

	/**
	 * Sets the line editor.
	 *
	 * @param lineEditor The line editor.
	 */

	public final void setLineEditor(final AlgorithmDesktopLineEditor lineEditor) {
		this.lineEditor = lineEditor;
	}

	@Override
	public final void isCreateVariableStatement(final CreateVariableStatement statement) {
		final String oldIdentifier = statement.getIdentifier();
		lineEditor.createVariableStatement(statement);
		final String newIdentifier = statement.getIdentifier();

		if(oldIdentifier.equals(newIdentifier)) {
			handleNode(statement);
		}
		else {
			handleNode(statement, false);
			changeIdentifiers(oldIdentifier, newIdentifier);
			editorFrame.getMainPane().getAlgorithmTreeModel().reload(editorFrame.getMainPane().getAlgorithmTree());
		}
	}

	@Override
	public final void isAssignStatement(final AssignStatement statement) {
		handleNode(lineEditor.assignStatement(statement));
	}

	@Override
	public final void isPromptStatement(final PromptStatement statement) {
		handleNode(lineEditor.promptStatement(statement));
	}

	@Override
	public final void isPrintVariableStatement(final PrintVariableStatement statement) {
		handleNode(lineEditor.printVariableStatement(statement));
	}

	@Override
	public final void isPrintStatement(final PrintStatement statement) {
		handleNode(lineEditor.printStatement(statement));
	}

	@Override
	public final void isIfBlock(final IfBlock statement) {
		final boolean hasElseBlock = statement.hasElseBlock();
		lineEditor.ifElseBlock(statement);

		final ElseBlock elseBlock = statement.getElseBlock();
		if(hasElseBlock && elseBlock == null) {
			final AlgorithmTreeNode parent = node.getParent();
			parent.remove(parent.getIndex(node) + 1);
		}

		else if(!hasElseBlock && elseBlock != null) {
			final AlgorithmTreeNode parent = node.getParent();
			parent.insert(new AlgorithmTreeNode(elseBlock), parent.getIndex(node) + 1);
		}

		handleNode(statement);
	}

	@Override
	public final void isForLoop(final ForLoop statement) {
		handleNode(lineEditor.forLoop(statement));
	}

	@Override
	public final void isWhileLoop(final WhileLoop statement) {
		handleNode(lineEditor.whileLoop(statement));
	}

	@Override
	public final void isLineComment(final LineComment statement) {
		handleNode(lineEditor.lineComment(statement));
	}

	@Override
	public final void isBlockComment(final BlockComment statement) {
		handleNode(lineEditor.blockComment(statement));
	}

	@Override
	public final void isAlgorithmRootBlock(final AlgorithmRootBlock statement) {}

	@Override
	public final void isVariablesBlock(final VariablesBlock statement) {}

	@Override
	public final void isBeginningBlock(final BeginningBlock statement) {}

	@Override
	public final void isEndBlock(final EndBlock statement) {}

	@Override
	public final void isElseBlock(final ElseBlock statement) {}

	@Override
	public final void isUnknownStatement(final Statement statement) {}

	/**
	 * Handles the node according to the edited statement.
	 *
	 * @param edited The edited statement.
	 */

	private void handleNode(final Statement edited) {
		handleNode(edited, true);
	}

	/**
	 * Handles the node according to the edited statement.
	 *
	 * @param edited The edited statement.
	 * @param reload Whether the tree should be reloaded.
	 */

	private void handleNode(final Statement edited, final boolean reload) {
		if(edited == null) {
			return;
		}

		node.setUserObject(edited);

		final AlgorithmTree tree = editorFrame.getMainPane().getAlgorithmTree();
		if(reload) {
			tree.getModel().reload(tree, node.getParent());
		}
		tree.setSelectionPath(new TreePath(node.getPath())); // TODO: Does not work
	}

	/**
	 * Changes all identifiers according to the specified new identifier.
	 *
	 * @param oldIdentifier The old identifier.
	 * @param newIdentifier The new identifier.
	 */

	private void changeIdentifiers(final String oldIdentifier, final String newIdentifier) {
		changeIdentifiers(oldIdentifier, newIdentifier, editorFrame.getMainPane().getAlgorithmTreeModel().getBeginningBlock());
	}

	/**
	 * Changes all identifiers according to the specified new identifier.
	 *
	 * @param oldIdentifier The old identifier.
	 * @param newIdentifier The new identifier.
	 * @param statement Parent statement.
	 */

	private void changeIdentifiers(final String oldIdentifier, final String newIdentifier, final Statement statement) {
		if(statement.getStatementId() == AssignStatement.STATEMENT_ID) {
			final AssignStatement assignStatement = (AssignStatement)statement;

			if(assignStatement.getIdentifier().equals(oldIdentifier)) {
				assignStatement.setIdentifier(newIdentifier);
			}
		}

		if(statement instanceof BlockStatement) {
			for(final Statement child : ((BlockStatement)statement).listStatements()) {
				changeIdentifiers(oldIdentifier, newIdentifier, child);
			}
		}
	}

}