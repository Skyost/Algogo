package xyz.algogo.desktop.frames.listeners.button;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import xyz.algogo.core.Instruction;
import xyz.algogo.desktop.dialogs.AddLineDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.AlgoLineUtils;
import xyz.algogo.desktop.utils.AlgorithmTree;

public class ButtonEditLineListener extends AlgorithmEditorActionListener {
	
	public ButtonEditLineListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		DefaultMutableTreeNode selected = (DefaultMutableTreeNode)editor.getCurrentTreeComponent().getSelectionPaths()[0].getLastPathComponent();
		if(AlgorithmTree.getAttachedAlgoLine(selected).getInstruction() == Instruction.ELSE) {
			final DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selected.getParent();
			selected = (DefaultMutableTreeNode)parent.getChildAt(parent.getIndex(selected) - 1);
		}
		final Set<String> variables = AlgoLineUtils.getVariables(editor.getAlgorithm().getVariables()).keySet();
		AddLineDialog.listenerForInstruction(editor, editor, selected, null, variables.toArray(new String[variables.size()])).actionPerformed(event);
	}

}