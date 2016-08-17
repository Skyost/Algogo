package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.AlgorithmTree;

public class EditPasteListener extends AlgorithmEditorActionListener {
	
	public EditPasteListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		final List<AlgoLine> clipboard = editor.getClipboard();
		if(clipboard.size() == 0) {
			return;
		}
		editor.addAlgorithmToStack();
		DefaultMutableTreeNode changed = null;
		for(final AlgoLine line : clipboard) {
			if(changed == null) {
				changed = editor.addNode(line.copy(), false);
			}
			else {
				changed.add(AlgorithmTree.asMutableTreeNode(line));
			}
		}
		editor.algorithmChanged(true, true, true, changed, new TreePath(((DefaultMutableTreeNode)changed.getChildAt(changed.getChildCount() - clipboard.size())).getPath()));
	}

}