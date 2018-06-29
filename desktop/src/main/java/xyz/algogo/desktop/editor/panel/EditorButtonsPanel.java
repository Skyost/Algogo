package xyz.algogo.desktop.editor.panel;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.dialog.AddLineDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.EditorMainComponent;
import xyz.algogo.desktop.editor.component.EditorMainPane;
import xyz.algogo.desktop.editor.component.EditorMainPaneToggleListener;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTree;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeNode;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the edition buttons panel (add line, edit line, ...).
 */

public class EditorButtonsPanel extends JPanel implements EditorMainPaneToggleListener {

	/**
	 * The editor.
	 */

	private EditorFrame editor;

	/**
	 * The add line button.
	 */

	private final JButton addLine;

	/**
	 * Creates a new editor buttons panel.
	 *
	 * @param editor The editor.
	 */

	public EditorButtonsPanel(final EditorFrame editor) {
		this.editor = editor;

		final AppLanguage language = editor.getAppLanguage();
		editor.getMainPane().addToggleListener(this);
		this.setLayout(new GridLayout(0, 1));

		final AlgorithmTree tree = editor.getMainPane().getAlgorithmTree();
		addLine = new JButton(language.getString("editor.panel.addLine"));
		addLine.setIcon(FontIcon.of(MaterialDesign.MDI_PLUS));
		addLine.addActionListener(actionEvent -> new AddLineDialog(editor).getParentUI().setVisible(true));
		final JButton deleteLine = new JButton(language.getString("editor.panel.deleteLine"));
		deleteLine.setIcon(FontIcon.of(MaterialDesign.MDI_DELETE));
		deleteLine.addActionListener(actionEvent -> tree.removeNode((AlgorithmTreeNode)tree.getSelectionPath().getLastPathComponent()));
		final JButton editLine = new JButton(language.getString("editor.panel.editLine"));
		editLine.setIcon(FontIcon.of(MaterialDesign.MDI_PENCIL));
		editLine.addActionListener(actionEvent -> tree.editNode((AlgorithmTreeNode)tree.getSelectionPath().getLastPathComponent()));

		final JButton up = new JButton(language.getString("editor.panel.up"));
		up.setIcon(FontIcon.of(MaterialDesign.MDI_CHEVRON_UP));
		up.addActionListener(actionEvent -> tree.up((AlgorithmTreeNode)tree.getSelectionPath().getLastPathComponent()));
		final JButton down = new JButton(language.getString("editor.panel.down"));
		down.setIcon(FontIcon.of(MaterialDesign.MDI_CHEVRON_DOWN));
		down.addActionListener(actionEvent -> tree.down((AlgorithmTreeNode)tree.getSelectionPath().getLastPathComponent()));

		this.add(addLine);
		this.add(deleteLine);
		this.add(editLine);
		this.add(Box.createRigidArea(new Dimension(150, 10)));
		this.add(up);
		this.add(down);

		tree.addTreeSelectionListener(treeSelectionEvent -> toggleButtons(tree));
		toggleButtons(tree);
	}

	@Override
	public final void onToggle(final EditorMainComponent oldComponent, final EditorMainComponent newComponent) {
		toggleButtons(editor.getMainPane().getAlgorithmTree());
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
	 * Toggles buttons according to the algorithm tree state.
	 *
	 * @param tree The algorithm tree.
	 */

	private void toggleButtons(final AlgorithmTree tree) {
		final boolean enable = tree.shouldEnableEditButtons();
		for(final Component component : this.getComponents()) {
			if(!(component instanceof AbstractButton)) {
				continue;
			}

			component.setEnabled(enable);
		}

		final EditorMainPane mainPane = editor.getMainPane();
		addLine.setEnabled(mainPane.isShowingTree());
	}

}