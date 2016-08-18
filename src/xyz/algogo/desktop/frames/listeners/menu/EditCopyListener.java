package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Instruction;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.AlgoLineUtils;
import xyz.algogo.desktop.utils.AlgorithmTree;
import xyz.algogo.desktop.utils.AlgoLineListSelection;

public class EditCopyListener extends AlgorithmEditorActionListener {
	
	private final JMenuItem paste;
	private final boolean cut;
	
	public EditCopyListener(final EditorFrame editor, final JMenuItem paste, final boolean cut) {
		super(editor);
		this.paste = paste;
		this.cut = cut;
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		if(cut) {
			editor.addAlgorithmToStack();
		}
		final AlgoLineListSelection clipboard = new AlgoLineListSelection();
		
		final List<TreePath> paths = Arrays.asList(editor.getCurrentTreeComponent().getSelectionPaths());
		final HashSet<DefaultMutableTreeNode> toRemove = new HashSet<DefaultMutableTreeNode>();
		for(final TreePath path : paths) {
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
			final AlgoLine parentLine = AlgorithmTree.getAttachedAlgoLine(parentNode);
			if(paths.contains(path.getParentPath()) && (parentLine != null && !parentLine.isKeyword())) { // Then, its already added to the clipboard.
				continue;
			}
			final AlgoLine line = AlgorithmTree.getAttachedAlgoLine(node);
			if(line.isKeyword()) {
				continue;
			}
			clipboard.add(line.copy());
			if(cut) {
				toRemove.add(node);
			}
			if(AlgoLineUtils.ifFollowedByElse(line)) {
				final DefaultMutableTreeNode elsee = (DefaultMutableTreeNode)parentNode.getChildAt(parentNode.getIndex(node) + 1);
				if(paths.contains(new TreePath(elsee.getPath()))) {
					continue;
				}
				clipboard.add(AlgorithmTree.getAttachedAlgoLine(elsee).copy());
				if(cut) {
					toRemove.add(elsee);
				}
			}
			else if(line.getInstruction() == Instruction.ELSE) {
				final DefaultMutableTreeNode iff = (DefaultMutableTreeNode)parentNode.getChildAt(parentNode.getIndex(node) - 1);
				if(paths.contains(new TreePath(iff.getPath()))) {
					continue;
				}
				clipboard.add(clipboard.size() - 1, AlgorithmTree.getAttachedAlgoLine(iff).copy());
				if(cut) {
					toRemove.add(iff);
				}
			}
		}
		if(cut) {
			if(toRemove.size() > 0) {
				for(final DefaultMutableTreeNode node : toRemove) {
					editor.removeNode(node, false);
				}
				editor.algorithmChanged(true, true, true, null);
			}
			else {
				editor.popFromStack();
			}
		}
		paste.setEnabled(true);
		
		if(clipboard.size() > 0) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(clipboard, editor);
		}
	}

}