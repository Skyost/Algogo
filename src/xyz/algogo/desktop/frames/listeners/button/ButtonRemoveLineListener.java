package xyz.algogo.desktop.frames.listeners.button;

import java.awt.event.ActionEvent;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.AlgorithmTree;

public class ButtonRemoveLineListener extends AlgorithmEditorActionListener {
	
	public ButtonRemoveLineListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		for(final TreePath path : editor.getCurrentTreeComponent().getSelectionPaths()) {
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			if(!AlgorithmTree.getAttachedAlgoLine(node).isKeyword()) {
				editor.removeNode(node);
			}
		}
	}

}