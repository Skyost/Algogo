package xyz.algogo.desktop.editor.history.command.tree;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeNode;
import xyz.algogo.desktop.editor.history.command.EditorCommand;

/**
 * Represents an "up node" command.
 */

public class UpNodeCommand extends EditorCommand {

	/**
	 * The node to up.
	 */

	private AlgorithmTreeNode node;

	/**
	 * Creates a up node command.
	 *
	 * @param editor The editor.
	 * @param node The node.
	 */

	public UpNodeCommand(final EditorFrame editor, final AlgorithmTreeNode node) {
		super(editor);

		this.node = node;
	}

	@Override
	public final void execute() {
		node.up(this.getEditor().getMainPane().getAlgorithmTree());
	}

	@Override
	public final void unExecute() {
		node.down(this.getEditor().getMainPane().getAlgorithmTree());
	}

	@Override
	public final boolean canUnExecute() {
		return true;
	}

}