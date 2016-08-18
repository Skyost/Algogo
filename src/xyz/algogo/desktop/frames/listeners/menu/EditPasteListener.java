package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.desktop.dialogs.ErrorDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.AlgorithmTree;
import xyz.algogo.desktop.utils.AlgoLineListSelection;

public class EditPasteListener extends AlgorithmEditorActionListener {
	
	public EditPasteListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		final Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		if(!contents.isDataFlavorSupported(AlgoLineListSelection.getAlgoLineListSelectionFlavor())) {
			return;
		}
		try {
			final AlgoLineListSelection clipboard = (AlgoLineListSelection)contents.getTransferData(AlgoLineListSelection.getAlgoLineListSelectionFlavor());
			if(clipboard.size() == 0) {
				return;
			}
			editor.addAlgorithmToStack();
			DefaultMutableTreeNode changed = null;
			DefaultMutableTreeNode last = null;
			for(final AlgoLine line : clipboard) {
				final DefaultMutableTreeNode node = AlgorithmTree.asMutableTreeNode(line.copy());
				last = node;
				if(changed == null) {
					changed = editor.addNode(node, false);
				}
				else {
					changed.add(node);
				}
			}
			editor.algorithmChanged(true, true, true, changed, new TreePath(last.getPath()));
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(editor, ex);
		}
	}

}