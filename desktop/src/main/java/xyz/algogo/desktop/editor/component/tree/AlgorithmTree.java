package xyz.algogo.desktop.editor.component.tree;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.language.AlgogoLanguage;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.root.BeginningBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.core.statement.block.root.VariablesBlock;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.EditorMainComponent;
import xyz.algogo.desktop.editor.history.command.tree.*;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * A tree that is used to display algorithm.
 */

public class AlgorithmTree extends JTree implements EditorMainComponent {

	static {
		final Icon empty = new ImageIcon();
		UIManager.put("Tree.collapsedIcon", empty);
		UIManager.put("Tree.expandedIcon", empty);
	}

	/**
	 * The editor.
	 */

	private EditorFrame editor;

	/**
	 * Creates a new algorithm tree.
	 *
	 * @param editor The editor.
	 * @param model The tree model.
	 */

	public AlgorithmTree(final EditorFrame editor, final AlgorithmTreeModel model) {
		super(model);

		final AlgorithmTreeSelectionModel selectionModel = new AlgorithmTreeSelectionModel();
		selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		this.editor = editor;

		this.setShowsRootHandles(true);
		this.setRootVisible(false);
		this.setCellRenderer(new AlgorithmTreeRenderer(new AlgorithmLocalization(editor.getAppLanguage())));

		this.setSelectionModel(selectionModel);
	}

	@Override
	public final AlgorithmTreeModel getModel() {
		return (AlgorithmTreeModel)super.getModel();
	}

	@Override
	public final void setModel(final TreeModel model) {
		if(!(model instanceof AlgorithmTreeModel)) {
			throw new IllegalArgumentException("Model must be an instance of AlgorithmTreeModel.");
		}

		super.setModel(model);
	}

	@Override
	public final Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public final void newAlgorithm() {
		final AlgorithmTreeModel model = this.getModel();
		model.setDefaultNodes();

		model.reload(this);
	}

	@Override
	public final String getContent() {
		return toAlgorithm().toLanguage(new AlgogoLanguage());
	}

	@Override
	public final void open(final String content) {
		try {
			final Algorithm algorithm = Algorithm.parse(content);
			this.getModel().fromAlgorithm(algorithm, this);

			editor.getCredits().setCredits(algorithm);
		}
		catch(final ParseException ex) {
			final AppLanguage language = editor.getAppLanguage();
			JOptionPane.showMessageDialog(editor, language.getString("editor.dialog.parseError.message", ex.getLine(), ex.getErrorMessage()), language.getString("editor.dialog.parseError.title"), JOptionPane.ERROR_MESSAGE);
			editor.getMainPane().toggleCurrentComponent();
			editor.getMainPane().getAlgorithmTextArea().setText(content);
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, editor);
		}
	}

	@Override
	public final void deleteSelection() {
		removeNode((AlgorithmTreeNode)this.getSelectionPath().getLastPathComponent());
	}

	@Override
	public final void copySelection(final ClipboardOwner clipboardOwner) {
		final AlgorithmTreeNode node = (AlgorithmTreeNode)this.getSelectionPath().getLastPathComponent();

		final StatementTransfer transfer = new StatementTransfer();
		transfer.add(node.getUserObject().copy());

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transfer, clipboardOwner);
	}

	@Override
	public final void pasteClipboard() throws IOException, UnsupportedFlavorException {
		final Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		if(!contents.isDataFlavorSupported(StatementTransfer.FLAVOR)) {
			return;
		}

		final StatementTransfer transfer = (StatementTransfer)contents.getTransferData(StatementTransfer.FLAVOR);
		if(transfer.isEmpty()) {
			return;
		}

		final Statement statement = transfer.get(0).copy();
		addStatement(statement);
	}

	@Override
	public final Algorithm toAlgorithm() {
		final Algorithm algorithm = this.getModel().toAlgorithm();
		editor.getCredits().apply(algorithm);

		return algorithm;
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
	 * Adds a statement to the currently selected node.
	 *
	 * @param statement The statement.
	 */

	public final void addStatement(final Statement statement) {
		final boolean createVariable = statement.getStatementId() == CreateVariableStatement.STATEMENT_ID;

		AlgorithmTreeNode rootParent = createVariable ? this.getModel().getVariablesNode() : this.getModel().getBeginningNode();
		final AlgorithmTreeNode node = new AlgorithmTreeNode(statement);

		final int index = this.getMinSelectionRow();
		if(index == -1) {
			insertNode(rootParent, node);
			return;
		}

		final AlgorithmTreeNode selected = (AlgorithmTreeNode)this.getSelectionPath().getLastPathComponent();
		final Statement selectedObject = selected.getUserObject();

		if(selectedObject instanceof BlockStatement) {
			if(createVariable || selectedObject.getStatementId() == VariablesBlock.STATEMENT_ID || selectedObject.getStatementId() == EndBlock.STATEMENT_ID) {
				insertNode(rootParent, node);
				return;
			}

			insertNode(selected, node);
			return;
		}

		if(selectedObject.getStatementId() == CreateVariableStatement.STATEMENT_ID) {
			insertNode(rootParent, node, createVariable ? index : -1);
			return;
		}

		if(createVariable) {
			insertNode(rootParent, node);
			return;
		}

		final AlgorithmTreeNode parent = selected.getParent();
		insertNode(parent, node, parent.getIndex(selected) + 1);
	}

	/**
	 * Edits a node.
	 *
	 * @param node The node to edit.
	 */

	public final void editNode(final AlgorithmTreeNode node) {
		editor.getEditorHistory().execute(new EditNodeCommand(editor, node));
	}

	/**
	 * Removes a node.
	 *
	 * @param node The node to remove.
	 */

	public final void removeNode(final AlgorithmTreeNode node) {
		editor.getEditorHistory().execute(new RemoveNodeCommand(editor, node));
	}

	/**
	 * Adds a node.
	 *
	 * @param parent Node parent.
	 * @param node The node.
	 */

	private void insertNode(final AlgorithmTreeNode parent, final AlgorithmTreeNode node) {
		insertNode(parent, node, -1);
	}

	/**
	 * Inserts the node.
	 *
	 * @param parent Node parent.
	 * @param node The node.
	 * @param index The insert index.
	 */

	private void insertNode(final AlgorithmTreeNode parent, final AlgorithmTreeNode node, final int index) {
		editor.getEditorHistory().execute(new AddNodeCommand(editor, parent, node, index));
	}

	/**
	 * Moves a node upward.
	 *
	 * @param node The node.
	 */

	public final void up(final AlgorithmTreeNode node) {
		editor.getEditorHistory().execute(new UpNodeCommand(editor, node));
	}

	/**
	 * Moves a node downward.
	 *
	 * @param node The node.
	 */

	public final void down(final AlgorithmTreeNode node) {
		editor.getEditorHistory().execute(new DownNodeCommand(editor, node));
	}

	/**
	 * Returns whether edit buttons (add lines, remove line, ...) should be enabled.
	 *
	 * @return Whether edit buttons should be enabled.
	 */

	public final boolean shouldEnableEditButtons() {
		if(!editor.getMainPane().isShowingTree()) {
			return false;
		}

		final TreePath selection = this.getSelectionPath();
		boolean enable = false;

		if(selection != null) {
			final int statementId = ((AlgorithmTreeNode)selection.getLastPathComponent()).getUserObject().getStatementId();
			enable = statementId != VariablesBlock.STATEMENT_ID && statementId != BeginningBlock.STATEMENT_ID && statementId != EndBlock.STATEMENT_ID && statementId != ElseBlock.STATEMENT_ID;
		}

		return enable;
	}

	/**
	 * Expand all tree nodes.
	 */

	public final void expandAllNodes() {
		for(int i = 0; i < this.getRowCount(); i++) {
			if(this.isExpanded(i)) {
				continue;
			}
			this.expandRow(i);
		}
	}

}