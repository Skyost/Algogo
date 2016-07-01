package xyz.algogo.desktop.frames.listeners.button;

import java.awt.event.ActionEvent;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.AlgorithmTree;

public class ButtonDownListener extends AlgorithmEditorActionListener {
	
	public ButtonDownListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		final DefaultMutableTreeNode selected = (DefaultMutableTreeNode)editor.getCurrentTreeComponent().getSelectionPaths()[0].getLastPathComponent();
		final DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selected.getParent();
		if(AlgorithmTree.down(parent, parent.getIndex(selected))) {
			editor.algorithmChanged(true, true, parent, new TreePath(selected.getPath()));
		}
	}

}