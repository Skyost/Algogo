package xyz.algogo.desktop.editor.component.tree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

/**
 * The algorithm tree selection model.
 */

public class AlgorithmTreeSelectionModel extends DefaultTreeSelectionModel {

	@Override
	public final void clearSelection() {
		boolean notify = this.selection == null || this.rowMapper == null;
		super.clearSelection();

		if(notify) {
			this.fireValueChanged(new TreeSelectionEvent(this, new TreePath[0], new boolean[0], null, null));
		}
	}

}