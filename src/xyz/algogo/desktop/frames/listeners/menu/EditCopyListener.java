package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.AlgorithmTree;

public class EditCopyListener extends AlgorithmEditorActionListener {
	
	private final JMenuItem paste;
	
	public EditCopyListener(final EditorFrame editor, final JMenuItem paste) {
		super(editor);
		this.paste = paste;
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		final AlgorithmTree tree = editor.getCurrentTreeComponent();
		final TreePath[] paths = tree.getSelectionPaths();
		if(paths == null || paths.length < 1) {
			return;
		}
		final DefaultMutableTreeNode selected = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
		if(selected.equals(tree.variables) || selected.equals(tree.beginning) || selected.equals(tree.end)) {
			return;
		}
		final List<DefaultMutableTreeNode> clipboard = editor.getClipboard();
		clipboard.clear();
		for(final TreePath path : paths) {
			clipboard.add((DefaultMutableTreeNode)((DefaultMutableTreeNode)path.getLastPathComponent()).clone());
		}
		paste.setEnabled(true);
	}

}