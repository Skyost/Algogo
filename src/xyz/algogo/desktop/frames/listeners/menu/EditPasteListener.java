package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.AlgorithmTree;

public class EditPasteListener extends AlgorithmEditorActionListener {
	
	public EditPasteListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		final TreePath[] paths = editor.getCurrentTreeComponent().getSelectionPaths();
		if(paths == null) {
			return;
		}
		editor.addAlgorithmToStack();
		for(final DefaultMutableTreeNode node : editor.getClipboard()) {
			editor.addNode(AlgorithmTree.getAttachedAlgoLine(node).clone());
		}
	}

}